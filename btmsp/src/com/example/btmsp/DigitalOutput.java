package com.example.btmsp;

public class DigitalOutput{	    
    private int pin;
    private Board board;
    
    public enum Pin{
    	_10(10),_11(11);
    	
    	private int pin;
    	
    	Pin(int pin){
    		this.pin=pin;
    	}
    	
    	public int getPin(){
    		return this.pin;
    	}
    }
      
    public DigitalOutput(Board board,DigitalOutput.Pin pin){
    	this.board=board;
    	this.pin=pin.getPin();
    	board.communicate(Board.Mode.SEND_READ,"CDO"+this.pin+"/");	    	  
    }
      
    public synchronized void write(boolean state){
    	if(state == true){
    		board.communicate(Board.Mode.SEND_READ,"DO"+pin+"H/");
    	}
    	else{
    		board.communicate(Board.Mode.SEND_READ,"DO"+pin+"L/");
    	}
    }      
}