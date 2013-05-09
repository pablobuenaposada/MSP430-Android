package com.example.btmsp;

import java.util.ArrayList;
import android.content.Intent;

public class Board {
	
	private BluetoothComm communication = null;	
	private MainActivity context = null;	
	
	public Board(MainActivity context, String BTaddress){
		this.context=context;
		communication = new BluetoothComm(this,BTaddress);
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

	public void noBluetoothSupported() {
		context.finish();		
	}

	public void askEnableBluetooth() {
		Intent enableIntent = new Intent(android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE);
        context.startActivityForResult(enableIntent, 2);		
	}
	
	public class DigitalOutput{
	      private int pin;
	      private Board board;
	      
	      public DigitalOutput(Board board,int pin){
	    	  this.board=board;
	    	  this.pin=pin;
	    	  board.communicate('r',"SDO"+pin+"/");	    	  
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
	
	public class DigitalInput{
		private Board board;
		private int pin;
		
		public DigitalInput(Board board, int pin){
			this.board=board;
			this.pin=pin;
			board.communicate('r',"SDI"+pin+"/");			
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
	
	public class AnalogInput{
		private Board board;
		private int pin;
		
		public AnalogInput(Board board, int pin){
			this.board=board;
			this.pin=pin;			
			this.board.communicate('r',"SAI"+pin+"/");			
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
	
	public class PWM{
		private Board board;
		private int pin;
		//private int period;
		//private int duty;
		
		public PWM (Board board, int pin, int period, int duty){
			this.board = board;
			this.pin = pin;
			//this.period = period;
			//this.duty = duty;			
			this.board.communicate('r',"SPWM"+pin+String.valueOf(period).length()+String.valueOf(duty).length()+period+duty+"/");			
		}
		
		public synchronized void setDuty(int newDuty){
			this.board.communicate('r',"PWM"+pin+"D"+String.valueOf(newDuty).length()+newDuty+"/");
		}
		
		public synchronized void setPeriod(int newPeriod){
			this.board.communicate('r',"PWM"+pin+"P"+String.valueOf(newPeriod).length()+newPeriod+"/");
		}	
	}
	
	public class OfflineTask{
		private Board board;
		private int pin;
		private int min;
		private char mode;
		private int numSamples;
		
		public OfflineTask(Board board, char mode, int pin, int min, int numSamples){
			this.board = board;
			this.mode = mode;
			this.pin = pin;
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
			
			if (mode == 'd'){
				this.board.communicate('r',"SOTDI"+pin+String.format("%05d", min)+String.format("%04d",numSamples)+"/");
			}
			else if(mode == 'a'){
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
	
	public DigitalOutput createDigitalOutput(int pin){
		return new DigitalOutput(this,pin);		
	}
	
	public DigitalInput createDigitalInput(int pin){
		return new DigitalInput(this,pin);
	}
	
	public AnalogInput createAnalogInput(int pin){
		return new AnalogInput(this,pin);
	}
	
	public PWM createPWM(int pin, int period, int duty){
		return new PWM(this,pin,period,duty);
	}
	
	public OfflineTask createOfflineTask(int pin, char mode, int min, int numSamples){
		return new OfflineTask(this,mode,pin,min,numSamples);
	}	
}

