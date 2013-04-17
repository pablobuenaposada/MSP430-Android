#include <msp430.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

char command[20];
int charPos=0;
int cmdRdy=0;


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



  while(1){
	  if (cmdRdy == 1){

		  if(command[0] == 'S'){
			  if(command[1] == 'D'){
				  if(command[2] == 'O'){ //SETUP DIGITAL OUTPUT
					  int port = char2Int(command[3]);
					  int pin = char2Int(command[4])+1;

					  if(port == 1){
						  P1DIR |= (int)(pow(2,pin-1));
						  P1SEL &=  ~(int)(pow(2,pin-1));
						  sendString("/");
					  }
				  }
				  else if(command[2] == 'I'){ //SETUP DIGITAL INPUT
					  int port = char2Int(command[3]);
					  int pin = char2Int(command[4])+1;

					  if(port == 2){
						  P2DIR &= ~(int)(pow(2,pin-1));
					  	  P2SEL &= ~(int)(pow(2,pin-1));
					  	  sendString("/");
					  }
				  }
			  }

			  else if(command[1] == 'A'){
				  if(command[2] == 'I'){ //SETUP ANALOG INPUT
					  int port = char2Int(command[3]);
			  		  int pin = char2Int(command[4]);

			  		  if(port == 6){
			  			  if (pin == 7){
			  				  ADC12CTL0 = ADC12ON + ADC12SHT0_2;
			  				  ADC12CTL1 = ADC12SHP;
			  				  ADC12MCTL0 = ADC12SREF_1 + ADC12INCH_7;
			  				  ADC12CTL0 |= ADC12ENC;
			  				  sendString("/");
			  			  }
			  		  }
				  }
			  }

			  else if (command[1] == 'P' & command[2] == 'W' & command[3] == 'M'){ //SETUP PWM
				  int port = char2Int(command[4]);
				  int pin = char2Int(command[5])+1;
				  int lenPeriod = char2Int(command[6]);
				  int lenDuty = char2Int(command[7]);
				  int i=0;
				  int period=0;
				  int duty=0;

				  for(i=0; i<lenPeriod; i++){
					  period += (char2Int(command[8+i])*pow(10,lenPeriod-1-i));
				  }
				  for(i=0; i<lenDuty; i++){
					  duty += (char2Int(command[8+lenPeriod+i])*pow(10,lenDuty-1-i));
				  }

				  if(port == 4){
					  P4DIR |= (int)(pow(2,pin-1));
					  P4SEL |= (int)(pow(2,pin-1));

					  TB0CCR0 = period; //PWM period
			   	      TB0CCR2 = duty; //PWM duty cycle, time cycle on vs. off

					  TB0CCTL2 = OUTMOD_7; // TA0CCR1 reset/set -- high voltage below count and
				      TB0CTL = TASSEL_2 + MC_1;
				      sendString("/");
				  }
			  }
		  }

		  else if(command[0] == 'D'){
			  if(command[1] == 'O'){ //SET DIGITAL OUTPUT VALUE
				  int port = char2Int(command[2]);
				  int pin = char2Int(command[3])+1;

				  if (port == 1){
					  if (command[4] == 'H'){
						  P1OUT |= (int)(pow(2,pin-1));
						  sendString("/");
					  }
					  else if (command[4] == 'L'){
						  P1OUT &=  ~(int)(pow(2,pin-1));
						  sendString("/");
					  }
					  else if(command[4] == 'R'){

						  if((int)(P1OUT & (int)(pow(2,pin-1))) > 0){
							  sendString("1/");
						  }
						  else{
							  sendString("0/");
						  }
					  }
				  }
			  }
			  else if(command[1] == 'I'){ //GET DIGITAL INPUT VALUE
				  int port = char2Int(command[2]);
				  int pin = char2Int(command[3])+1;

				  if (port == 2){
					  if((int)(P2IN & (int)(pow(2,pin-1))) > 0){
						  sendString("1/");
					  }
					  else{
						  sendString("0/");
					  }
				  }
			  }
		  }

		  else if(command[0] == 'A'){ //GET ANALOG INPUT VALUE
			  if(command[1] == 'I'){
				  int port = char2Int(command[2]);
				  int pin = char2Int(command[3]);

				  if(port == 6){
					  if (pin == 7){
						  char adcValue[5];
						  ADC12CTL0 |= ADC12SC;// Start conversions
						  while (!(ADC12IFG & 0x0001));
						  sprintf(adcValue,"%d",(int)ADC12MEM0);
						  strcat(adcValue,"/");
						  sendString(adcValue);
					  }
				  }
			  }
		  }

		  else if(command[0] == 'P' & command[1] == 'W' & command[2] == 'M'){ //SET PERIOD OR DUTY CYCLE OF PWM
			  int port = char2Int(command[3]);
			  int pin = char2Int(command[4])+1;
			  int lenValue = char2Int(command[6]);
			  int value = 0;
			  int i;

			  for(i=0; i<lenValue; i++){
				  value += (char2Int(command[7+i])*pow(10,lenValue-1-i));
			  }

			  if (command[5] == 'P'){
				  if(port == 4){
					  TB0CCR0 = value; //PWM period
					  sendString("/");
				  }
			  }
			  else if(command[5] == 'D'){
				  if(port == 4){
					  TB0CCR2 = value; //PWM duty cycle, time cycle on vs. off
					  sendString("/");
				  }
			  }
		  }

		  charPos=0;
		  cmdRdy=0;

	  }

  }





}


#pragma vector=USCI_A0_VECTOR
__interrupt void USCI_A0_ISR(void){

    switch(__even_in_range(UCA0IV,4)){
		case 0: break;                          // Vector 0 - no interrupt
		case 2:
			if (UCA0RXBUF != '/'){
				command[charPos]=UCA0RXBUF;
				charPos++;
			}
			else{
				cmdRdy=1;
			}
			break;
		case 4: break;
		default: break;
    }
}


