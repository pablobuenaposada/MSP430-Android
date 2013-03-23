#include <msp430.h>

char message[4]="hola";

void sendString(char* string){
	int index;
    for(index=0; index < strlen(string); index++){
    	UCA0TXBUF = string[index];
    	while (!(UCA0IFG & UCTXIFG));  // USCI_A0 TX buffer ready?
    }
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
  UCA0IE |= UCTXIE + UCRXIE;                // Enable USCI_A0 TX/RX interrupt
  __bis_SR_register(LPM3_bits + GIE);       // Enter LPM3 w/ interrupts enabled
  //__no_operation();                         // For debugger
}


#pragma vector=USCI_A0_VECTOR
__interrupt void USCI_A0_ISR(void){

    switch(__even_in_range(UCA0IV,4)){
		case 0: break;                          // Vector 0 - no interrupt
		case 2:        						  	// Vector 2 - RXIFG
			if (UCA0RXBUF == 'a'){
				P1OUT = 0x01;
			}
			else if (UCA0RXBUF == 'b'){
				P1OUT = 0x02;
			}
			else{
				P1OUT = 0x00;
			}

			break;
    case 4:                                 // Vector 4 - TXIFG
      __delay_cycles(5000);                 // Add small gap between TX'ed bytes
      sendString(message);
      _delay_cycles(500000);
      break;
    default: break;
  }
}
