/* YourDuino.com Example Software Sketch
 20 character 4 line I2C Display
 ANOTHER NEW TYPE Marked "LCM1602 IIC  A0 A1 A2"
 A0-A1-A2 are grounded so I2C Address is 0x20  
 terry@yourduino.com */
/*-----( Import needed libraries )-----*/ 
#include <Wire.h>
#include <LCD.h>
#include <LiquidCrystal_I2C.h>  // F Malpartida's NewLiquidCrystal library
//Download: https://bitbucket.org/fmalpartida/new-liquidcrystal/downloads
// Move original LiquidCrystal library elsewhere, copy this in it's place

/*-----( Declare Constants )-----*/
#define I2C_ADDR    0x20  // Define I2C Address for the PCF8574T 
//---(Following are the PCF8574 pin assignments to LCD connections )----
// This are different than earlier/different I2C LCD displays
#define BACKLIGHT_PIN  3
#define En_pin  2
#define Rw_pin  1
#define Rs_pin  0
#define D4_pin  4
#define D5_pin  5
#define D6_pin  6
#define D7_pin  7

#define  LED_OFF  1
#define  LED_ON  0

/*-----( Declare objects )-----*/  
LiquidCrystal_I2C  lcd(I2C_ADDR,En_pin,Rw_pin,Rs_pin,D4_pin,D5_pin,D6_pin,D7_pin);

void setup()   /*----( SETUP: RUNS ONCE )----*/
{
  lcd.begin (20,4);  // initialize the lcd 
// Switch on the backlight
  lcd.setBacklightPin(BACKLIGHT_PIN,POSITIVE);
  lcd.setBacklight(LED_ON);
}// END Setup

void loop()   /*----( LOOP: RUNS OVER AND OVER AGAIN )----*/
{

// Reset the display  
  lcd.clear();
  delay(1000);
  lcd.home();
  
// Print our characters on the LCD
  lcd.backlight();  //Backlight ON if under program control
  lcd.setCursor(3,0); //Start at character 3 on line 0
  lcd.print("Hello, world!");
  delay(1000);
  lcd.setCursor(2,1);
  lcd.print("From YourDuino");
  delay(1000);  
  lcd.setCursor(0,2);
  lcd.print("20 by 4 Line Display");
  lcd.setCursor(0,3);
  delay(2000);   
  lcd.print("http://YourDuino.com");
  delay(8000);
} // END Loop
