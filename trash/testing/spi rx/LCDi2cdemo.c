#include <msp430.h>
char rx;
int main(void)
{
  WDTCTL = WDTPW+WDTHOLD;                   // Stop watchdog timer

  while(!(P3IN&0x01));                      // If clock sig from mstr stays low,
                                            // it is not yet in SPI mode
  P3SEL |= 0x31;                            // P3.5,4,0 option select
  UCA0CTL1 |= UCSWRST;                      // **Put state machine in reset**
  UCA0CTL0 |= UCSYNC+UCCKPL+UCMSB;          // 3-pin, 8-bit SPI slave,
                                            // Clock polarity high, MSB
  UCA0CTL1 &= ~UCSWRST;                     // **Initialize USCI state machine**
  UCA0IE |= UCRXIE;                         // Enable USCI_A0 RX interrupt

  __bis_SR_register(LPM4_bits + GIE);       // Enter LPM4, enable interrupts
}

// Echo character
#pragma vector=USCI_A0_VECTOR
__interrupt void USCI_A0_ISR(void)
{
  switch(__even_in_range(UCA0IV,4))
  {
    case 0:break;                             // Vector 0 - no interrupt
    case 2:                                   // Vector 2 - RXIFG
      while (!(UCA0IFG&UCTXIFG));             // USCI_A0 TX buffer ready?
      rx = UCA0RXBUF;
      break;
    case 4:break;                             // Vector 4 - TXIFG
    default: break;
  }
}
