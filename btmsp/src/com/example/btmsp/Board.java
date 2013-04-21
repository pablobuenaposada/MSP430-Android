package com.example.btmsp;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Board {
	
	private BluetoothComm communication = null;
	public static final int MESSAGE_READ = 1;
	private MainActivity context=null;	
	
	public Board(MainActivity context, String BTaddress){
		this.context=context;
		communication = new BluetoothComm(this,null,BTaddress);	
	}
	
	// The Handler that gets information back from the BluetoothChatService
    /*private final Handler handler = new Handler() {
    	@Override
        public void handleMessage(Message msg) {
           if(msg.what == MESSAGE_READ) {
        	   byte[] readBuf = (byte[]) msg.obj;
               // construct a string from the valid bytes in the buffer
               String message = new String(readBuf, 0, msg.arg1);           	   
           }
    	}
    };*/
    
    private synchronized String communicate(char mode, String toSend){
    	if(mode == 's'){
    		send(toSend);
    		return null;
    	}
    	else if(mode == 'r'){
    		return read(toSend);    		
    	}
    	return null;
    }
    
    private synchronized void send(String data){
    	communication.send(data);
    }
    
    private synchronized String read(String toSend){
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
	      
	      /*public synchronized boolean read(){	    	  
	    	  
	    	  if (board.communicate('r',"DO"+pin+"R/").contains("1/")){
	    		  return true;	    		  
	    	  }
	    	  else{
	    		  return false;
	    	  }
	      }*/
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
			return Integer.parseInt(rawValue.substring(0, rawValue.length()-1));			
		}
	}
	
	public class PWM{
		private Board board;
		private int pin;
		private int period;
		private int duty;
		
		public PWM (Board board, int pin, int period, int duty){
			this.board = board;
			this.pin = pin;
			this.period = period;
			this.duty = duty;			
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
		private int countLimit;
		private char mode;
		private int numSamples;
		
		public OfflineTask(Board board, char mode, int pin, int countLimit, int numSamples){
			this.board = board;
			this.mode = mode;
			this.pin = pin;
			this.countLimit = countLimit;
			this.numSamples = numSamples;
			if (mode == 'd'){
				this.board.communicate('r',"SOTDI"+pin+countLimit+"00"+numSamples+"/");
			}			
		}
		
		public synchronized  ArrayList<Integer> read(){
			String rawList = this.board.communicate('r', "OT/");			
			Log.e("bbb", "a");
			String[] splitList = new String[2]; //rawList.split("\\.");
			splitList[0]="a";
			splitList[1]="b";
			Log.e("AAA", "b");
			ArrayList<Integer> list = new ArrayList<Integer>();
			for(int i=0; i<splitList.length; i++){
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
	
	public OfflineTask createOfflineTask(int pin, char mode, int countLimit, int numSamples){
		return new OfflineTask(this,mode,pin,countLimit,numSamples);
	}
	
	
	

	
}

