#include <msp430.h>
#include <math.h>
#include <string.h> //for the strlen() function
#include "resources.h" //to get global variables

/*variable definitions for offline task mode*/
int offlineSize;
int offlineArray[100];
int offlinePos = 0;
int offlinePort;
int offlinePin;
char offlineMode;
char offlineUnits;
unsigned int offlineCountLimit;
unsigned int unitsElapsed=0;
unsigned secondsElapsed=0;
/*------------------------------------------*/
char command3[30];
int cmdPos3=0;
/*------------------------------------------*/
char bufferSPI[30];
int bufferSPIPos=0;

void sendString(const char *string){
	int index;
    for(index=0; index < strlen(string); index++){
    	UCA0TXBUF = string[index];
    	while (!(UCA0IFG & UCTXIFG));  // USCI_A0 TX buffer ready?
    }
}

int char2Int(char c){
	int number = c - '0';
	number = number + 0;
	return number;
}

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

void setupAnalogInputPort(int port, int pin){
	if(port == 6){
		pin=pin+1;
		P6DIR &= ~(int)(pow(2,pin-1));
		P6SEL &= ~(int)(pow(2,pin-1));

	}
	else if(port == 7){
		pin=pin+1;
		P7DIR &= ~(int)(pow(2,pin-1));
		P7SEL &= ~(int)(pow(2,pin-1));
	}
}

void setupAnalogInput(int port, int pin){

	ADC12CTL0 = ADC12SHT0_3 + ADC12ON;
	ADC12CTL1 = ADC12SHP;
	ADC12MCTL0 = ADC12SREF_0;


	if(port == 6){
		if (pin == 7){
			ADC12MCTL0 |= ADC12INCH_7;
		}
	}
	else if (port == 7){
		if(pin == 4){
			ADC12MCTL0 |= ADC12INCH_12;
		}
		else if(pin == 5){
			ADC12MCTL0 |= ADC12INCH_13;
		}
		else if(pin == 6){
			ADC12MCTL0 |= ADC12INCH_14;
		}
		else if(pin == 7){
			ADC12MCTL0 |= ADC12INCH_15;
		}
	}
	ADC12CTL0 |= ADC12ENC;
}

void setupPWM(int port, int pin, int period, int duty){

	if(port == 4){
		if(pin == 1){
			TB0CCR1 = duty;
			TB0CCTL1 = OUTMOD_7;
		}
		else if (pin == 2){
			TB0CCR2 = duty;
			TB0CCTL2 = OUTMOD_7;
		}
		else if (pin == 3){
			TB0CCR3 = duty;
			TB0CCTL3 = OUTMOD_7;
		}
		else if (pin == 5){
			TB0CCR5 = duty;
			TB0CCTL5 = OUTMOD_7;
		}
		else if (pin == 6){
			TB0CCR6 = duty;
			TB0CCTL6 = OUTMOD_7;
		}
		pin=pin+1;
		P4DIR |= (int)(pow(2,pin-1));
		P4SEL |= (int)(pow(2,pin-1));
		TB0CCR0 = period;
		TB0CTL = TBSSEL_2 + MC_1;
	}
	else if(port == 7){
		if(pin == 3){
			TA1CCR2 = duty;
			TA1CCTL2 = OUTMOD_7;
			pin=pin+1;
			P7DIR |= (int)(pow(2,pin-1));
			P7SEL |= (int)(pow(2,pin-1));
			TA1CCR0 = period;
			TA1CTL = TASSEL_2 + MC_1;
		}
	}
	else if(port == 8){
		if(pin == 6){
			TA1CCR2 = duty;
			TA1CCTL2 = OUTMOD_7;
			pin=pin+1;
			P8DIR |= (int)(pow(2,pin-1));
			P8SEL |= (int)(pow(2,pin-1));
			TA1CCR0 = period;
			TA1CTL = TASSEL_2 + MC_1;
		}
		else if(pin == 2){
			TA0CCR2 = duty;
			TA0CCTL2 = OUTMOD_7;
			pin=pin+1;
			P8DIR |= (int)(pow(2,pin-1));
			P8SEL |= (int)(pow(2,pin-1));
			TA0CCR0 = period;
			TA0CTL = TASSEL_2 + MC_1;
		}
		else if(pin == 1){
			TA0CCR1 = duty;
			TA0CCTL1 = OUTMOD_7;
			pin=pin+1;
			P8DIR |= (int)(pow(2,pin-1));
			P8SEL |= (int)(pow(2,pin-1));
			TA0CCR0 = period;
			TA0CTL = TASSEL_2 + MC_1;
		}
	}

}

void setupOfflineTask(char mode,int port,int pin,char units,int countLimit,int samples){

	if(mode == 'D'){
		setupDigitalInput(port,pin);
	}
	else if(mode == 'A'){
		setupAnalogInput(port,pin);
	}
	offlineMode = mode;
	offlinePort = port;
	offlinePin = pin;
	offlinePos=0;
	offlineSize=samples;
	offlineCountLimit = countLimit;
	offlineUnits = units;
	unitsElapsed =0;
	secondsElapsed = 0;

	if (units == 'M'){
		setupTimerA0(32768); //32KHZ ACLK SO 32768 are 1 second
	}
	else if (units == 'S'){
		setupTimerA0(32768);  //32KHZ ACLK SO 32768 are 1 second
	}
	else if(units == 'U'){
		setupTimerA0(33);  //32KHZ ACLK SO 33 are 1 millisecond
	}
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

	ADC12CTL0 |= ADC12SC;// Start conversions
	while (!(ADC12IFG & 0x0001));
	return (int)ADC12MEM0;

}

void setPWMPeriod(int port, int pin, int period){

	if(port == 4){
		TB0CCR0 = period; //PWM period
	}
}
void setPWMDuty(int port, int pin, int duty){

	if(port == 4){
		if(pin == 1){
			TB0CCR1 = duty;
		}
		else if(pin == 2){
			TB0CCR2 = duty;
		}
		else if(pin == 3){
			TB0CCR3 = duty;
		}
		else if(pin == 5){
			TB0CCR5 = duty;
		}
		else if(pin == 6){
			TB0CCR6 = duty;
		}
	}
}

void doOfflineTask(){

	if(offlineUnits == 'M' && secondsElapsed < 60){
		secondsElapsed += 1;
	}
	else if (offlineUnits == 'S' && unitsElapsed < offlineCountLimit){
		unitsElapsed += 1;
	}
	else if(offlineUnits == 'U' && unitsElapsed < offlineCountLimit){
		unitsElapsed += 1;
	}

	if(offlineUnits == 'M' && secondsElapsed >= 60){
		unitsElapsed += 1;
	}

	if (unitsElapsed >= offlineCountLimit & (offlinePos < offlineSize)){
		secondsElapsed = 0;
		unitsElapsed = 0;
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
	else if(offlinePos >= offlineSize){
		stopTimerA0();
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

void initUart(){
	P3SEL |= BIT4+BIT5;                       // P3.4,5 UART option select
}

void setUart19200bauds(){
	UCA0CTL1 |= UCSWRST;                      // **Put state machine in reset**
    UCA0CTL1 |= UCSSEL_2;                     // CLK = ACLK
	UCA0BR0 = 52;                           // 32k/9600 - 3.41
	UCA0BR1 = 0;                           //
	UCA0MCTL =UCBRS0;                          // Modulation
	UCA0CTL1 &= ~UCSWRST;                     // **Initialize USCI state machine**
	UCA0IE |= UCRXIE;                // Enable USCI_A0 TX/RX interrupt
}

void setUart9600bauds(){
	UCA0CTL1 |= UCSWRST; // **Put state machine in reset**
	UCA0CTL1 |= UCSSEL_1; // CLK = ACLK
	UCA0BR0 = 0x03; // 32k/9600 - 3.41
	UCA0BR1 = 0x00; //
	UCA0MCTL = 0x06; // Modulation
	UCA0CTL1 &= ~UCSWRST; // **Initialize USCI state machine**
	UCA0IE |= UCRXIE; // Enable USCI_A0 TX/RX interrupt
}

void cleanUart(){
	//memset(command, 0, 30); //clean command because it has been processed
	cmdTime = 0;
	cmdPos=0; //starting point of the command pointer
	cmdRdy=0; //no command ready to be processed
}

void setupB3SPI(){
	P10SEL |= BIT1+BIT3; // P10.1,2,0 option select
	UCB3CTL1 |= UCSWRST; // **Put state machine in reset**
	UCB3CTL0 |= UCSYNC+UCCKPL+UCMSB; // 3-pin, 8-bit SPI slave, Clock polarity high, MSB
	UCB3CTL1 &= ~UCSWRST; // **Initialize USCI state machine**
	UCB3IE |= UCRXIE;
}

char *rxB3SPI(int size){

	const char* commandPointer = bufferSPI;
	char *received = (char*) malloc(size);
	strncpy(received, commandPointer, size);
	bufferSPIPos=0;
	return received;
}

int getB3SPIReceivedSize(){
	return bufferSPIPos;
}

#pragma vector=USCI_B3_VECTOR
__interrupt void USCI_B3_ISR(void){
	switch(__even_in_range(UCB3IV,4)){
		case 0:break; // Vector 0 - no interrupt
		case 2: // Vector 2 - RXIFG
			if(bufferSPIPos < 30){
				bufferSPI[bufferSPIPos] = (char)UCB3RXBUF;
				bufferSPIPos++;
			}
			break;
		case 4:break; // Vector 4 - TXIFG
		default: break;
	}
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

#pragma vector=TIMER0_A0_VECTOR
	__interrupt void Timer0_A0 (void) {		// Timer0 A0 interrupt service routine
		doOfflineTask();
}

#pragma vector=USCI_A0_VECTOR
__interrupt void USCI_A0_ISR(void){

    switch(__even_in_range(UCA0IV,4)){
		case 2:
			cmdTime = 0;
			command[cmdPos]=UCA0RXBUF;
			if (UCA0RXBUF != '/'){
				cmdPos++;
			}
			else{
				//cmdPos=0;
				cmdRdy=1;
			}
			break;
		default: break;
    }
}
