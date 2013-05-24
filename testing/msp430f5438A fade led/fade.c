#include <msp430f5438a.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int pwmDirection = 1;

void main(void){

    WDTCTL = WDT_MDLY_32;                 // Watchdog timer ≈32ms


    SFRIE1|=WDTIE;	 					  // enable Watchdog timer interrupts
    P4DIR |= BIT2;                        // Green LED for output

    P4SEL |= BIT2;                        // Green LED Pulse width modulation

    TB0CCR0 = 1000;                       // PWM period

    TB0CCR2 = 1;                          // PWM duty cycle, time cycle on vs. off, on 1/1000 initially

    TB0CCTL2 = OUTMOD_7;                  // TA0CCR1 reset/set -- high voltage below count and
                                          // low voltage when past

    TB0CTL = TASSEL_2 + MC_1;             // Timer A control set to SMCLK clock TASSEL_2, 1MHz
                                          // and count up mode MC_1

    _BIS_SR(LPM0_bits + GIE);             // Enter Low power mode 0

}

#pragma vector=WDT_VECTOR                 // Watchdog Timer interrupt service routine

  __interrupt void watchdog_timer(void) {

    TB0CCR2 += pwmDirection*20;           // Increase/decrease duty cycle, on vs. off time

    if( TB0CCR2 > 200 || TB0CCR2 < 5 )   // Pulse brighter (increasing TA0CCR1) or dimmer
       pwmDirection = -pwmDirection;
}
