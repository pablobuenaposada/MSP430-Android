#include <SPI.h>

#define MOSI 11 // MSP430F5438A P3.4
#define MISO 12 // MSP430F5438A P3.5
#define SCLK 13 // MSP430F5438A P3.0
#define SS 10 // MSP430F5438A P3.3

void setup(){
  Serial.begin(9600);
  
  pinMode(MISO, INPUT);
  
  // turn on SPI in slave mode
  SPCR |= _BV(SPE);

  // now turn on interrupts
  SPI.attachInterrupt();  
}

void loop() {

}

ISR (SPI_STC_vect){
  byte c = SPDR; 
  Serial.println(c);
}
