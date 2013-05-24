#include <SPI.h>

#define MOSI 11 // MSP430F5438A P3.4
#define MISO 12 // MSP430F5438A P3.5
#define SCLK 13 // MSP430F5438A P3.0
#define SS 10 // MSP430F5438A P3.3

void setup(){
  Serial.begin(9600);
  
  pinMode(SCLK, OUTPUT);
  pinMode(MOSI, OUTPUT);
  pinMode(MISO, INPUT);
  //pinMode(SS, OUTPUT);

  // initialize SPI:
  SPI.begin();
  SPI.setBitOrder(MSBFIRST); // Set order of read/write (Big endian)
  SPI.setDataMode(SPI_MODE2); 
  
}

void loop() {
   //digitalWrite(SS, LOW); // Select MSP430
   //delay(1); // Wait for MOSI to settle
   SPI.transfer(0x11);
   //digitalWrite(SS, HIGH); // Deselect MSP30
   //delay(1000); // Delay for next cycle 
}


