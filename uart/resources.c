#include <msp430.h>
#include <math.h>

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
			ADC12CTL0 = ADC12ON + ADC12SHT0_2;
			ADC12CTL1 = ADC12SHP;
			ADC12MCTL0 = ADC12SREF_1 + ADC12INCH_7;
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
	if(port == 6){
		if (pin == 7){
			int i;
			int sum=0;
			for(i=0; i<8; i++){
				ADC12CTL0 |= ADC12SC;// Start conversions
				while (!(ADC12IFG & 0x0001));
				sum += (int)ADC12MEM0;
			}
			return sum >> 3;
		}
	}
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

void setupTimer(int countLimit){
	TA0CCR0 = countLimit;				// Count limit (16 bit)
	TA0CCTL0 = CCIE;					// Enable counter interrupts, bit 4=1
	TA0CTL = TASSEL_1 + MC_1; 			// Timer A 0 with ACLK @ 12KHz, count UP
}

void stopTimer(){
	TA0CCTL0 = ~CCIE;
}
