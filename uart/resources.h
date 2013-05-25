#ifndef RESOURCES_H
#define RESOURCES_H

#ifdef  MAIN_FILE
	char command[30];
	int cmdPos;
	int cmdRdy;
	int cmdTime;
#else
	extern char command[30];
	extern int cmdPos;
	extern int cmdRdy;
	extern int cmdTime;
#endif

#endif




void setupDigitalOutput(int port, int pin);
void setupDigitalInput(int port, int pin);
void setupAnalogInputPort(int port, int pin);
void setupPWM(int port, int pin, int period, int duty);

void setDigitalOutput(int port, int pin, char value);
int getDigitalInput(int port, int pin);
int getAnalogInput(int port, int pin);

void setPWMPeriod(int port, int pin, int period);
void setPWMDuty(int port, int pin, int duty);

void setupOfflineTask(char mode, int port, int pin, char units, int countLimit, int samples);
int *getOfflineTask();
int getOfflineTaskSize();

void setupUart9600A3();
void txA3(const char *string);
char *rxA3(int size);
int getA3ReceivedSize();

void setupSPIB3Master();
void setupSPIB3Slave();
void txSPIB3(const char *string);
char *rxB3SPI(int size);
int getB3SPIReceivedSize();

void initUart();
void setUart19200bauds();
void setUart9600();
void cleanUart();

void sendString(const char *string);
int char2Int(char c);

