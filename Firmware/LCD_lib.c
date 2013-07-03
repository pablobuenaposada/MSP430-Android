/******************************************************************************
 * Rutines de inicialització i ús del lcd i2c
 *
 * P2.5 està connectat al pin de reset del LCD
 *
 * La configuració de la alimentació del LCD és a 3.3V mitjantçant un circuit
 * de boost.
 *
 ******************************************************************************/


#include "I2C_LCD.h"
#include <msp430.h>

static const char lines[]={0x00, 0x40};

void lcd_init(void){

    //iniciem el port del reset del i2c
    P2DIR |= BIT7;
    P2OUT &= ~BIT7;
    __delay_cycles(delay_1m5);
    P2OUT |= BIT7;
    __delay_cycles(delay_1m5);
 
    char buffer[15];

    //Donem temps al booster a estar preparat ~ 7.5ms
    __delay_cycles(delay_1m5); 
    __delay_cycles(delay_1m5); 
    __delay_cycles(delay_1m5); 
    __delay_cycles(delay_1m5); 
    __delay_cycles(delay_1m5); 

    //Seqüència de inicialització
    //
    buffer[0] = 0x00;       //Li diem que enviarem una sèrie de comandes
    buffer[1] = 0x38;       //Function set
    buffer[2] = 0x39;       //Function set amb les funcions de la IT 1 (instruction table)
    buffer[3] = 0x14;       //Freqüència del oscilador intern
    buffer[4] = 0x72;       //Power on, activem booter i setegem contrast
    buffer[5] = 0x50;       //Part alta del contrast
    buffer[6] = 0x6D;       //Circuit de follower, tensions de bias
    buffer[7] = 0x0C;       //Display on sense cursor i sense posició de cursor
    buffer[8] = 0x01;       //Clear

    i2c_write(LCD_ADDRESS, buffer, 9);

}


//Enviem una dada a la DDRAM
void lcd_write(char valor){
    char buffer[2];
    
    buffer[0] = 0x40;
    buffer[1] = valor;
    i2c_write(LCD_ADDRESS, buffer, 2);
}

//Enveim el contingut de un array
void lcd_print(char *string, int tamany){
    int i = 0;

    for (i = 0; i < tamany; i++){
        lcd_write(string[i]);
    }
}

//Enviem una comanda
void lcd_command(char valor){
    char buffer[2];

    buffer[0] = 0x00;
    buffer[1] = valor;

    i2c_write(LCD_ADDRESS, buffer, 2);
}

//Enviem un conjunt de comandes
void lcd_commands(char *string, int tamany){
    int i = 0;

    for (i = 0; i < tamany; i++){
        lcd_write(string[i]);
    }
}

//Setejem el valor de AC amb la forma (linea, caracter), on linea pot ser 0 o 1.
void lcd_setCursor(int line, char x){
    lcd_command( 0x80 + lines[line] + x);
}
