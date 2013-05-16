#include <msp430.h>
#include <math.h>
#include <string.h> //for the strlen() function


/*variable definitions for offline task mode*/
int offlineSize;
int offlineArray[100];
//int offlineFlag = 0;
int offlinePos = 0;
int offlinePort;
int offlinePin;
char offlineMode;
unsigned int offlineCountLimit;
unsigned int minElapsed=0;
unsigned int secondsElapsed=0;
/*------------------------------------------*/
char command3[30];
int cmdPos3=0;

void setupTimerA0(int countLimit){
	TA0CCR0 = countLimit;				// Count limit (16 bit)
	TA0CCTL0 = CCIE;					// Enable counter interrupts, bit 4=1
	TA0CTL = TASSEL_1 + MC_1; 			// Timer A 0 with ACLK @ 12KHz, count UP
}

void stopTimerA0(){
	TA0CCTL0 = ~CCIE;
}

void setupDigitalOutput(int port, int pin){
	pin=pin+1;
	if(port == 1){
		P1DIR |= (int)(pow(2,pin-1));
		P1SEL &= ~(int)(pow(2,pin-1));
	}
}

void setupDigitalInput(int port, int pin){
	pin=pin+1;
	if(port == 2){
		P2DIR &= ~(int)(pow(2,pin-1));
		P2SEL &= ~(int)(pow(2,pin-1));
		P2REN |= (int)(pow(2,pin-1));
		P2OUT |= (int)(pow(2,pin-1));
	}
}

void setupAnalogInput(int port, int pin){
	if(port == 6){
		if (pin == 7){
			ADC12CTL0 = ADC12SHT0_3 + ADC12ON;
			ADC12CTL1 = ADC12SHP;
			ADC12MCTL0 = ADC12SREF_0 + ADC12INCH_7;
			ADC12CTL0 |= ADC12ENC;
		}
	}
	else if (port == 7){
		if(pin == 4){
			ADC12CTL0 = ADC12SHT0_3 + ADC12ON;
			ADC12CTL1 = ADC12SHP;
			ADC12MCTL0 = ADC12SREF_0 + ADC12INCH_12;
			ADC12CTL0 |= ADC12ENC;
		}
	}
}

void setupPWM(int port, int pin, int period, int duty){
	pin=pin+1;
	if(port == 4){
		P4DIR |= (int)(pow(2,pin-1));
		P4SEL |= (int)(pow(2,pin-1));

		TB0CCR0 = period; //PWM period
		TB0CCR2 = duty; //PWM duty cycle, time cycle on vs. off

		TB0CCTL2 = OUTMOD_7; // TA0CCR1 reset/set -- high voltage below count and
		TB0CTL = TASSEL_2 + MC_1;
	}
}

void setupOfflineTask(char mode,int port,int pin,int countLimit,int samples){

	if(mode == 'D'){
		setupDigitalInput(port,pin);
	}
	else if(mode == 'A'){
		setupAnalogInput(port,pin);
	}
	offlineMode = mode;
	offlinePort = port;
	offlinePin = pin;
	//offlineFlag=1;
	offlinePos=0;
	offlineSize=samples;
	offlineCountLimit = countLimit;
	setupTimerA0(32767);
}

void setDigitalOutput(int port, int pin, char value){
	pin=pin+1;
	if (port == 1){
		if (value == 'H'){
			P1OUT |= (int)(pow(2,pin-1));
		}
		else if (value == 'L'){
			P1OUT &=  ~(int)(pow(2,pin-1));
		}
	}
}

int getDigitalInput(int port, int pin){
	pin=pin+1;

	if (port == 2){
		if((int)(P2IN & (int)(pow(2,pin-1))) > 0){
			return 1;
		}
		else{
			return 0;
		}
	}
	return -1;

}

int getAnalogInput(int port, int pin){

	setupAnalogInput(port,pin);
	if(port == 6){
		if (pin == 7){
			ADC12CTL0 |= ADC12SC;// Start conversions
			while (!(ADC12IFG & 0x0001));
			return (int)ADC12MEM0;
		}
	}
	else if(port == 7){
		if(pin == 4){
			ADC12CTL0 |= ADC12SC;// Start conversions
			while (!(ADC12IFG & 0x0001));
			return (int)ADC12MEM0;
		}
	}
	return 0; //if any problem occurs
}

void setPWMPeriod(int port, int pin, int period){
	pin=pin+1;
	if(port == 4){
		TB0CCR0 = period; //PWM period
	}
}
void setPWMDuty(int port, int pin, int duty){
	pin=pin+1;
	if(port == 4){
		TB0CCR2 = duty; //PWM duty cycle, time cycle on vs. off
	}
}

void doOfflineTask(){

	if(secondsElapsed < 60){
		secondsElapsed += 1;
	}
	else{
		secondsElapsed = 0;
		minElapsed += 1;
		if(minElapsed >= offlineCountLimit & (offlinePos < offlineSize)){
			P1OUT ^= BIT0;
			int value;
			if (offlineMode == 'D'){
				value = getDigitalInput(offlinePort,offlinePin);
			}
			else{
				value = getAnalogInput(offlinePort,offlinePin);
			}
			offlineArray[offlinePos] = value;
			offlinePos += 1;
		}
		else if(minElapsed >= offlineCountLimit & (offlinePos >= offlineSize)){
			stopTimerA0();
		}
	}
}

int *getOfflineTask(){
	return offlineArray;
}

int getOfflineTaskSize(){
	return offlinePos;
}

void setupUart9600A3(){
	P10SEL |= BIT4+BIT5;                       // P10.4,5 UART option select
	UCA3CTL1 |= UCSWRST; // **Put state machine in reset**
	UCA3CTL1 |= UCSSEL_1; // CLK = ACLK
	UCA3BR0 = 0x03; // 32k/9600 - 3.41
	UCA3BR1 = 0x00; //
	UCA3MCTL = 0x06; // Modulation
	UCA3CTL1 &= ~UCSWRST; // **Initialize USCI state machine**
	UCA3IE |= UCRXIE; // Enable USCI_A0 TX/RX interrupt
}

void txA3(const char *string){
	int index;
	for(index=0; index < strlen(string); index++){
		UCA3TXBUF = string[index];
		while (!(UCA3IFG & UCTXIFG));  // USCI_A0 TX buffer ready?
	}
}

char *rxA3(int size){
	//char *aux = command3;
	//memset(command3, 0, 30); //clean command because it has been processed

	const char* commandPointer = command3;
	char *received = (char*) malloc(size);
	strncpy(received, commandPointer, size);
	cmdPos3=0;
	return received;
}

int getA3ReceivedSize(){
	return cmdPos3;
}

#pragma vector=USCI_A3_VECTOR
__interrupt void USCI_A3_ISR(void){
    switch(__even_in_range(UCA3IV,4)){
		case 2:
			if(cmdPos3 < 30){
				command3[cmdPos3]=UCA3RXBUF;
				cmdPos3++;
			}
			break;
		default: break;
    }
}

