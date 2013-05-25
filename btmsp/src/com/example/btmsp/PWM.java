package com.example.btmsp;

public class PWM{
	private Board board;
	private int pin;	
	
	
	public enum Pin{
    	_41(41),_42(42),_43(43),_45(45),_46(46),_73(73),_86(86),_14(14),_12(12),_15(15),_13(13),_82(82),_81(81);
    	
    	private int pin;
    	
    	Pin(int pin){
    		this.pin=pin;
    	}
    	
    	public int getPin(){
    		return this.pin;
    	}
    }
	
	public PWM (Board board, PWM.Pin pin, int period, int duty){			
		this.board = board;
		this.pin = pin.getPin();						
		this.board.communicate(Board.Mode.SEND_READ,"CPWM"+this.pin+String.valueOf(period).length()+String.valueOf(duty).length()+period+duty+"/");			
	}
	
	public synchronized void setDuty(int newDuty){
		this.board.communicate(Board.Mode.SEND_READ,"PWM"+pin+"D"+newDuty+"/");
	}
	
	public synchronized void setDutyNonACK(int newDuty){
		this.board.communicate(Board.Mode.SEND,"PWM"+pin+"d"+newDuty+"/");
	}
	
	public synchronized void setPeriod(int newPeriod){
		this.board.communicate(Board.Mode.SEND_READ,"PWM"+pin+"P"+newPeriod+"/");
	}
	
	public synchronized void setPeriodNonACK(int newPeriod){
		this.board.communicate(Board.Mode.SEND,"PWM"+pin+"p"+newPeriod+"/");
	}
}
