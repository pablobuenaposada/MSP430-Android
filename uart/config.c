#include <msp430.h>

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
