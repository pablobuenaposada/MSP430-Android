#include <msp430.h>
#include <string.h> //for the strlen() function

/*char command[30];
int cmdPos=0;
int cmdRdy=0;
#define CMD_TIMEOUT 10000
int cmdTime=0;*/

void init(){
	P10SEL |= BIT4+BIT5;                       // P3.4,5 UART option select
}

void set(){
	UCA3CTL1 |= UCSWRST; // **Put state machine in reset**
	UCA3CTL1 |= UCSSEL_1; // CLK = ACLK
	UCA3BR0 = 0x03; // 32k/9600 - 3.41
	UCA3BR1 = 0x00; //
	UCA3MCTL = 0x06; // Modulation
	UCA3CTL1 &= ~UCSWRST; // **Initialize USCI state machine**
	UCA3IE |= UCRXIE; // Enable USCI_A0 TX/RX interrupt
}

void envio(const char *string){
	int index;
    for(index=0; index < strlen(string); index++){
    	UCA3TXBUF = string[index];
    	while (!(UCA3IFG & UCTXIFG));  // USCI_A0 TX buffer ready?
    }
}

/*#pragma vector=USCI_A3_VECTOR
__interrupt void USCI_A3_ISR(void){

    switch(__even_in_range(UCA3IV,4)){
		case 2:
			cmdTime = 0;
			if (UCA3RXBUF != '/'){
				command[cmdPos]=UCA3RXBUF;
				cmdPos++;
			}
			else{
				cmdPos=0;
				cmdRdy=1;
			}
			break;
		default: break;
    }
}*/
