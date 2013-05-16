package com.example.btmsp;

import java.util.ArrayList;

public class Board {
	
	private BluetoothComm communication = null;	
	
	public Board(String BTaddress) throws BluetoothDisabled, NoBluetoothSupported{		
		communication = new BluetoothComm(BTaddress);
		communicate('r',"N/"); //notify that we are a new connection
	}	
    
    private synchronized String communicate(char mode, String toSend){
    	if(mode == 's'){
    		send(toSend);
    		return null;
    	}
    	else if(mode == 'r'){    		
			try {
				return read(toSend);
			} catch (TimeoutException e) {
				return "/";
			}			  		
    	}
    	return null;
    }
    
    private synchronized void send(String data){
    	communication.send(data);
    }
    
    private synchronized String read(String toSend) throws TimeoutException{
    	send(toSend);
    	return communication.read();
    }

	public void destroy() {
		communication.destroy();		
	}		
	
	public static class DigitalOutput{	    
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
	      
	    public DigitalOutput(Board board,com.example.btmsp.Board.DigitalOutput.Pin pin){
	    	this.board=board;
	    	this.pin=pin.getPin();
	    	board.communicate('r',"SDO"+this.pin+"/");	    	  
	    }
	      
	    public synchronized void write(boolean state){
	    	if(state == true){
	    		board.communicate('r',"DO"+pin+"H/");
	    	}
	    	else{
	    		board.communicate('r',"DO"+pin+"L/");
	    	}
	    }      
	}
	
	public static class DigitalInput{
		private Board board;
		private int pin;
		
		public enum Pin{
	    	_26(26),_27(27);
	    	
	    	private int pin;
	    	
	    	Pin(int pin){
	    		this.pin=pin;
	    	}
	    	
	    	public int getPin(){
	    		return this.pin;
	    	}
	    }
		
		public DigitalInput(Board board, com.example.btmsp.Board.DigitalInput.Pin pin){
			this.board=board;
			this.pin=pin.getPin();
			board.communicate('r',"SDI"+this.pin+"/");			
		}
		
		public synchronized boolean read(){			
			if (board.communicate('r',"DI"+pin+"/").contains("1/")){
				return true;	    		  
	    	}
	    	else{
	    		return false;
	    	}			
		}
	}
	
	public static class AnalogInput{
		private Board board;
		private int pin;
		
		public enum Pin{
	    	_67(67),_74(74);
	    	
	    	private int pin;
	    	
	    	Pin(int pin){
	    		this.pin=pin;
	    	}
	    	
	    	public int getPin(){
	    		return this.pin;
	    	}
	    }
		
		public AnalogInput(Board board, com.example.btmsp.Board.AnalogInput.Pin pin){
			this.board=board;
			this.pin=pin.getPin();			
			this.board.communicate('r',"SAI"+this.pin+"/");			
		}
		
		public synchronized int read(){			
			String rawValue = board.communicate('r',"AI"+pin+"/");			
			try{
				return Integer.parseInt(rawValue.substring(0, rawValue.length()-1));			
			}
			catch(Exception e){
				return -1;
			}
		}
	}
	
	public static class PWM{
		private Board board;
		private int pin;
		//private int period;
		//private int duty;
		
		public enum Pin{
	    	_42(42);
	    	
	    	private int pin;
	    	
	    	Pin(int pin){
	    		this.pin=pin;
	    	}
	    	
	    	public int getPin(){
	    		return this.pin;
	    	}
	    }
		
		public PWM (Board board, com.example.btmsp.Board.PWM.Pin pin, int period, int duty){
			this.board = board;
			this.pin = pin.getPin();
			//this.period = period;
			//this.duty = duty;			
			this.board.communicate('r',"SPWM"+this.pin+String.valueOf(period).length()+String.valueOf(duty).length()+period+duty+"/");			
		}
		
		public synchronized void setDuty(int newDuty){
			this.board.communicate('r',"PWM"+pin+"D"+String.valueOf(newDuty).length()+newDuty+"/");
		}
		
		public synchronized void setPeriod(int newPeriod){
			this.board.communicate('r',"PWM"+pin+"P"+String.valueOf(newPeriod).length()+newPeriod+"/");
		}	
	}
	
	public static class OfflineTask{
		private Board board;
		private int pin;
		private int min;
		private Mode mode;
		private int numSamples;
		
		public enum Pin{
	    	_67(67);
	    	
	    	private int pin;
	    	
	    	Pin(int pin){
	    		this.pin=pin;
	    	}
	    	
	    	public int getPin(){
	    		return this.pin;
	    	}
	    }
		
		public enum Mode{
			DIGITAL,ANALOG;			
		}
		
		public OfflineTask(Board board, Mode mode, com.example.btmsp.Board.OfflineTask.Pin pin, int min, int numSamples){
			this.board = board;
			this.mode = mode;
			this.pin = pin.getPin();
			this.min = min;
			this.numSamples = numSamples;
		}
		
		public synchronized void start(){			
			String minZeros="";
			String samplesZeros="";
			
			for(int i=0; i<4-Integer.toString(min).length(); i++){
				minZeros.concat("0");
			}
			
			for(int i=0; i<5-Integer.toString(numSamples).length(); i++){
				samplesZeros.concat("0");
			}
			
			if (mode.equals(OfflineTask.Mode.DIGITAL)){
				this.board.communicate('r',"SOTDI"+pin+String.format("%05d", min)+String.format("%04d",numSamples)+"/");
			}
			else if(mode.equals(OfflineTask.Mode.ANALOG)){
				this.board.communicate('r',"SOTAI"+pin+String.format("%05d", min)+String.format("%04d",numSamples)+"/");
			}
		}
		
		public synchronized  ArrayList<Integer> read(){
			String rawList = this.board.communicate('r', "OT/");			
			
			String[] splitList = rawList.split("\\.");			
			ArrayList<Integer> list = new ArrayList<Integer>();
			
			for(int i=0; i<splitList.length-1; i++){
				list.add(Integer.parseInt(splitList[i]));
			}			
			return list;
		}
		 
	}
	
	public class Serial{
		private Board board;
	
		public Serial(Board board){
			this.board = board;
			this.board.communicate('r',"SUARTA3/");
		}
		
		public synchronized void send(String tx){
			this.board.communicate('r',"UARTA3T"+tx+"/");			
		}
		
		public synchronized String read(){
			String received = this.board.communicate('r',"UARTA3R/");
			return received.substring(0, received.length()-1);
		}
	}
	
	public DigitalOutput createDigitalOutput(DigitalOutput.Pin pin){
		return new DigitalOutput(this,pin);		
	}
	
	public DigitalInput createDigitalInput(DigitalInput.Pin pin){
		return new DigitalInput(this,pin);
	}
	
	public AnalogInput createAnalogInput(AnalogInput.Pin pin){
		return new AnalogInput(this,pin);
	}
	
	public PWM createPWM(PWM.Pin pin, int period, int duty){
		return new PWM(this,pin,period,duty);
	}
	
	public OfflineTask createOfflineTask(OfflineTask.Pin pin, OfflineTask.Mode mode, int min, int numSamples){
		return new OfflineTask(this,mode,pin,min,numSamples);
	}
	
	public Serial createSerial(){
		return new Serial(this);
	}
}

