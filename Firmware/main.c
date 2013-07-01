#include <msp430.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define MAIN_FILE
#include "resources.h"
#define CMD_TIMEOUT 5000


int main(void){

  WDTCTL = WDTPW+WDTHOLD;                   // Stop watchdog timer
  P7SEL |= 0x03;                            // Port select XT1

  do{
    UCSCTL7 &= ~(XT2OFFG + XT1LFOFFG + DCOFFG);
                                            // Clear XT2,XT1,DCO fault flags
    SFRIFG1 &= ~OFIFG;                      // Clear fault flags
    __delay_cycles(100000);                 // Delay for Osc to stabilize
  }while (SFRIFG1&OFIFG);                   // Test oscillator fault flag

  initUart();
  setUart19200bauds();
  cleanUart();

  __bis_SR_register(SCG0);       // Enter LPM3 w/ interrupts enabled
  _enable_interrupt();
  __no_operation();                         // For debugger

  while(1){

	  if (cmdRdy == 1){

		  if(command[0] == 'N'){ //the device notify us that it established a new connection
			  resetPorts();
			  sendString("/"); //end of output message
		  }
		  else if(command[0] == 'C'){
			  if(command[1] == 'D'){
				  if(command[2] == 'O'){ //SETUP DIGITAL OUTPUT
					  int port = char2Int(command[3]);
					  int pin = char2Int(command[4]);
					  setupDigitalOutput(port,pin);
				  }
				  else if(command[2] == 'I'){ //SETUP DIGITAL INPUT
					  int port = char2Int(command[3]);
					  int pin = char2Int(command[4]);
					  setupDigitalInput(port,pin);
				  }
			  }

			  else if(command[1] == 'A'){
				  if(command[2] == 'I'){ //SETUP ANALOG INPUT
					  int port = char2Int(command[3]);
			  		  int pin = char2Int(command[4]);
			  		  setupAnalogInputPort(port,pin);
				  }
			  }

			  else if (command[1] == 'P' & command[2] == 'W' & command[3] == 'M'){ //SETUP PWM
				  int port = char2Int(command[4]);
				  int pin = char2Int(command[5]);
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

				  setupPWM(port,pin,period,duty);
			  }

			  else if(command[1] == 'O' & command[2] == 'T'){
				  if(command[3] == 'D' & command[4] == 'I'){ //SET OFFLINE TASK DIGITAL INPUT
					  int port = char2Int(command[5]);
					  int pin = char2Int(command[6]);

					  int countLimit = 0;
					  int i=0;
					  for(i=0; i<5; i++){
						  countLimit += (char2Int(command[8+i])*pow(10,5-1-i));
					  }

					  int offlineSize=0;
					  for(i=0; i<4; i++){
						  offlineSize += (char2Int(command[13+i])*pow(10,4-1-i));
					  }

					  setupOfflineTask('D',port,pin,command[7],countLimit,offlineSize);

				  }
				  else if(command[3] == 'A' & command[4] == 'I'){ //SET OFFLINE TASK ANALOG INPUT
					  int port = char2Int(command[5]);
					  int pin = char2Int(command[6]);

					  int countLimit = 0;
					  int i=0;
					  for(i=0; i<5; i++){
						  countLimit += (char2Int(command[8+i])*pow(10,5-1-i));
					  }

					  int offlineSize=0;
					  for(i=0; i<4; i++){
						  offlineSize += (char2Int(command[13+i])*pow(10,4-1-i));
					  }

					  setupOfflineTask('I',port,pin,command[7],countLimit,offlineSize);
				  }
			  }
			  else if(command[1] == 'U' & command[2] == 'A' & command[3] == 'R' & command[4] == 'T'){
				  if(command[5] == 'A' & command[6] == '3'){
					  setupUart9600A3();
				  }
			  }
			  else if(command[1] == 'S' & command[2] == 'P' & command[3] == 'I'){
				  if(command[4] == 'B' & command[5] == '3'){
					  if(command[6] == 'M'){
						  setupSPIB3Master();
					  }
					  else if(command[6] == 'S'){
						  setupSPIB3Slave();
					  }
				  }
			  }
			  else if(command[1] == 'I' & command[2] == '2' & command[3] == 'C'){
				  if(command[4] == 'B' & command[5] == '1'){
					  if(command[6] == 'M'){
						  int address=(char2Int(command[7])<<4)+(char2Int(command[8]));
						  setupI2CB1Master(address);
					  }
				  	  else if(command[6] == 'S'){
				  	  }
				  }
			  }
			  sendString("/"); //end of output message
		  }

		  else if(command[0] == 'D'){
			  if(command[1] == 'O'){ //SET DIGITAL OUTPUT VALUE
				  int port = char2Int(command[2]);
				  int pin = char2Int(command[3]);

				  setDigitalOutput(port,pin,command[4]);
			  }
			  else if(command[1] == 'I'){ //GET DIGITAL INPUT VALUE
				  int port = char2Int(command[2]);
				  int pin = char2Int(command[3]);

				  int value = getDigitalInput(port,pin);
				  if (value == 1){
					  sendString("1");
				  }
				  else{
					  sendString("0");
				  }
			  }
			  sendString("/"); //end of output message
		  }

		  else if(command[0] == 'A'){ //GET ANALOG INPUT VALUE
			  if(command[1] == 'I'){
				  int port = char2Int(command[2]);
				  int pin = char2Int(command[3]);

				  char adcValue[4];
				  sprintf(adcValue,"%d",getAnalogInput(port,pin));
				  sendString(adcValue);
			  }
			  sendString("/"); //end of output message
		  }

		  else if(command[0] == 'P' & command[1] == 'W' & command[2] == 'M'){ //SET PERIOD OR DUTY CYCLE OF PWM

			  int port = char2Int(command[3]);
			  int pin = char2Int(command[4]);

			  const char* from = command;
			  char *to = (char*) malloc(cmdPos-6);
			  memcpy(to, from+6, cmdPos-6);
			  int value = atoi(to);
			  free(to);
			  if (command[5] == 'P' | command[5] == 'p'){
				  setPWMPeriod(port,pin,value);
			  }
			  else if(command[5] == 'D' | command[5] == 'd'){
				  setPWMDuty(port,pin,value);
			  }
			  if(command[5] == 'P' | command[5] == 'D'){
				  sendString("/"); //end of output message
			  }
		  }

		  else if(command[0] == 'O' & command[1] == 'T'){ //GET OFFLINE TASK RESULTS

			  int samples = getOfflineTaskSize();
			  int *offlineArray = getOfflineTask();
			  int i;
			  for(i=0; i < samples; i++){
				  char c[4];
				  sprintf(c, "%d",offlineArray[i]);
				  sendString(c);
				  sendString(".");
			  }
			  sendString("/"); //end of output message
		  }

		  else if(command[0] == 'U' & command[1] == 'A' & command[2] == 'R' & command[3] == 'T'){
			  if(command[4] == 'A' & command[5] == '3'){
				  if(command[6] == 'T'){

					  int i=7;
					  while(command[i] != '/'){
						  char tx[1];
						  sprintf(tx, "%c",command[i]);
						  txA3(tx);
						  i++;
					  }
				  }
				  else if(command[6] == 'R'){

					  int size = getA3ReceivedSize();
					  char *recieved = rxA3(size);
					  int i;
					  for(i=0; i < size; i++){
						  char c[1];
						  sprintf(c, "%c",recieved[i]);
						  sendString(c);
					  }

				  }
			  }
			  sendString("/"); //end of output message
		  }

		  else if(command[0] == 'I' & command[1] == '2' & command[2] == 'C'){
			  if(command[3] == 'B' & command[4] == '1'){
				  if(command[5] == 'T'){
					  int i=6;
		  			  while(command[i] != '/'){
		  				  i=i+1;
		  			  }
		  			  int numValues = (i-6)/2;
		  			  int * data = (int*) malloc (numValues*2);
		  			  int x;
		  			  i=6;
		  			  for(x=0; x<numValues; x++){
		  				data[x] = (char2Int(command[i])<<4)+(char2Int(command[i+1]));
		  				i=i+2;
		  			  }
		  			  txI2CB1(data);
				  }
		  		  else if(command[5] == 'R'){
		  			int i=6;
		  			while(command[i] != '/'){
		  				i=i+1;
		  			}
		  			int numDigits=i-6;
		  			int numBytes=0;
		  			for(i=0;i< numDigits;i++){
		  				numBytes += (char2Int(command[6+i])*pow(10,numDigits-1-i));
		  			}
		  			int *data = rxI2CB1(numBytes);

		  			for(i=0; i < numBytes; i++){
		  				char c[4];
		  				sprintf(c, "%d",data[i]);
		  				sendString(c);
		  				sendString(".");
		  			}
		  		  }
			  }
		      sendString("/"); //end of output message
		  }

		  else if(command[0] == 'S' & command[1] == 'P' & command[2] == 'I'){
			  if(command[3] == 'B' & command[4] == '3'){
				  if(command[5] == 'R'){
					  int size = getB3SPIReceivedSize();
					  char *recieved = rxB3SPI(size);
					  int i;
					  for(i=0; i < size; i++){
						  char c[1];
						  sprintf(c, "%c",recieved[i]);
						  sendString(c);
					  }
				  }
				  else if(command[5] == 'T'){
					  int i=6;
					  while(command[i] != '/'){
						  char tx[1];
						  sprintf(tx, "%c",command[i]);
						  txSPIB3(tx);
						  i++;
					  }
				  }
			  }
			  sendString("/"); //end of output message
		  }
		  else{
			  sendString("/"); //end of output message
		  }


		  cleanUart();

	  }
	  else{
		  if(cmdTime > CMD_TIMEOUT){
			  cleanUart();
		  }
		  else{
			  cmdTime +=1;
		  }
	  }


  } //end while

} //end main








