#include <msp430.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

char command[20];

int i=0;
int cmdRdy=0;
char buf[5];
void sendString(const char *string){
	int index;
    for(index=0; index < strlen(string); index++){
    	UCA0TXBUF = string[index];
    	while (!(UCA0IFG & UCTXIFG));  // USCI_A0 TX buffer ready?
    }
}

int char2Int(const char c){
	int number = c - '0';
	return number;
}

int main(void){

  WDTCTL = WDTPW+WDTHOLD;                   // Stop watchdog timer
  P7SEL |= 0x03;                            // Port select XT1

  do
  {
    UCSCTL7 &= ~(XT2OFFG + XT1LFOFFG + DCOFFG);
                                            // Clear XT2,XT1,DCO fault flags
    SFRIFG1 &= ~OFIFG;                      // Clear fault flags
    __delay_cycles(100000);                 // Delay for Osc to stabilize
  }while (SFRIFG1&OFIFG);                   // Test oscillator fault flag

  P1OUT = 0x000;                            // P1.0/1 setup for LED output
  P1DIR |= BIT0+BIT1;                       //
  P3SEL |= BIT4+BIT5;                       // P3.4,5 UART option select

  UCA0CTL1 |= UCSWRST;                      // **Put state machine in reset**
  UCA0CTL1 |= UCSSEL_1;                     // CLK = ACLK
  UCA0BR0 = 0x03;                           // 32k/9600 - 3.41
  UCA0BR1 = 0x00;                           //
  UCA0MCTL = 0x06;                          // Modulation
  UCA0CTL1 &= ~UCSWRST;                     // **Initialize USCI state machine**
  UCA0IE |= UCRXIE;                // Enable USCI_A0 TX/RX interrupt
  __bis_SR_register(SCG0);       // Enter LPM3 w/ interrupts enabled
  _enable_interrupt();
  __no_operation();                         // For debugger

  P1OUT=0x01;
  while(1){
	  if (cmdRdy == 1){
		  cmdRdy=0;

		  if(command[0] == 'D'){
			  if(command[1] == 'O'){
				  int port = char2Int(command[2]);
				  int pin = char2Int(command[3])+1;

				  if (port == 1){
					  if (command[4] == 'H'){
						  P1OUT |= pin & (1*(int)(pow(2,pin-1)));
					  }
					  else if (command[4] == 'L'){
						  P1OUT &=  ~(pin & (1*(int)(pow(2,pin-1))));
					  }
				  }
			  }
		  }

		  i=0;

	  }

  }





}


#pragma vector=USCI_A0_VECTOR
__interrupt void USCI_A0_ISR(void){

    switch(__even_in_range(UCA0IV,4)){
		case 0: break;                          // Vector 0 - no interrupt
		case 2:
			if (UCA0RXBUF != '/'){
				command[i]=UCA0RXBUF;
				i++;
			}
			else{
				cmdRdy=1;
			}
			break;
    case 4:
    	break;
    default: break;
  }
}
