/******************************************************************************
 * Rutines de inicialització i comunicació del bus i2c
 *
 * El port i2c que es fas ervir és:
 * SDA --> P3.1
 * SCL --> P3.2
 *
 * Es considera que tant MCLK com SMCLK van a 16Mhz
 *
 *****************************************************************************/


#include <msp430.h>
//#include <signal.h>


//Per un MCLK de 2,133MHz, un delay_cicles de aquest valor dóna 1.9ms
#define delay_1m5   24000

void i2c_init(){
   
    // Configurem el port
    P3SEL |= BIT1 + BIT2; 
    
    //Aturem el mòdul
    UCB0CTL1 |= UCSWRST;
    
    //El configurem com a master, sincron i mode i2c, per defecte, està  en
    //single-master mode
    UCB0CTL0 = UCMODE_3 + UCSYNC + UCMST;

    //Configurem la font de clock del bloc i2c, en aquest cas SMCLK, que va a
    //16M, mantenim el reset
    UCB0CTL1 = UCSSEL__SMCLK + UCSWRST;


    //Configurem el preescales del bloc de la USCI. 16M / 160 = 100k
    UCB0BR0 = 160; 
    UCB0BR1 = 0;
}

void i2c_write(unsigned char addr, char *buffer, int ndades){

    int counter = 0;
    
    //Coloquem la adreça de slave en mode write
    UCB0I2CSA = addr;

    //Tornem a possar en marxa el mòdul
    UCB0CTL1 &= ~UCSWRST;

    //Start condition
    UCB0CTL1 |= UCTR + UCTXSTT;
    
    while(counter < ndades){
    
        //Ens aturem fins que el flag UCTXIFG es posi a 1
        while (!(UCB0IFG & UCTXIFG)){
            _NOP();
        }

        //Delay entre transmissions per donar temps al LCD a fer el que hagi 
        //de fer
        __delay_cycles(delay_1m5);
        UCB0TXBUF = buffer[counter];
        counter++;
    }

    //Ens aturem fins que el flag UCTXIFG es posi a 1
    while (!(UCB0IFG & UCTXIFG)){
        _NOP();
    }

    //quan acabem de enviar les dades del buffer, enviem la stop condition
    UCB0CTL1 |= UCTXSTP;
    __delay_cycles(delay_1m5);
}
