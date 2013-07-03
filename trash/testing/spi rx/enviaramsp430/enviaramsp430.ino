#include <SPI.h>

#define MOSI 11 // MSP430F5438A P3.4
#define MISO 12 // MSP430F5438A P3.5
#define SCLK 13 // MSP430F5438A P3.0
#define SS 10 // MSP430F5438A P3.3
char string[32];
  char byteRead;
  
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
  Serial.begin(9600);
  
  
}

void loop() {
  
  

  int availableBytes = Serial.available();
  for(int i=0; i<availableBytes; i++){
    string[i] = Serial.read();
  }
  for(int i=0; i<availableBytes; i++){
      SPI.transfer((byte)string[i]);
       delay(1000);
  }
  
   //digitalWrite(SS, HIGH); // Deselect MSP30
   //delay(1000); // Delay for next cycle
}
