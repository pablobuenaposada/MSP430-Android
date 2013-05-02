#ifndef RESOURCES_H
#define RESOURCES_H
#endif

void setupDigitalOutput(int port, int pin);
void setupDigitalInput(int port, int pin);
void setupAnalogInput(int port, int pin);
void setupPWM(int port, int pin, int period, int duty);

void setDigitalOutput(int port, int pin, char value);
int getDigitalInput(int port, int pin);
int getAnalogInput(int port, int pin);

void setPWMPeriod(int port, int pin, int period);
void setPWMDuty(int port, int pin, int duty);


void setupOfflineTask(char mode, int port, int pin, int countLimit, int samples);
void doOfflineTask();
int *getOfflineTask();
int getOfflineTaskSize();
