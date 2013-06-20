package lib;

import java.util.ArrayList;



public class OfflineTask{
	private Board board;
	private int pin;
	private int period;
	private Mode mode;
	private Units units;
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
	
	public enum Units{
		MILLISECONDS,MINUTES,SECONDS;			
	}	
			
	public OfflineTask(Board board, Mode mode, OfflineTask.Pin pin, OfflineTask.Units units, int period, int numSamples){
		this.board = board;
		this.mode = mode;
		this.units = units;
		this.pin = pin.getPin();
		this.period = period;
		this.numSamples = numSamples;
	}
	
	public synchronized void start(){			
		
		if (mode.equals(OfflineTask.Mode.DIGITAL)){
			if (units.equals(OfflineTask.Units.MINUTES)){
				this.board.communicate(Board.Mode.SEND_READ,"COTDI"+pin+"M"+String.format("%05d", period)+String.format("%04d",numSamples)+"/");
			}
			else if (units.equals(OfflineTask.Units.MILLISECONDS)){
				this.board.communicate(Board.Mode.SEND_READ,"COTDI"+pin+"U"+String.format("%05d", period)+String.format("%04d",numSamples)+"/");
			}
			else if (units.equals(OfflineTask.Units.SECONDS)){
				this.board.communicate(Board.Mode.SEND_READ,"COTDI"+pin+"S"+String.format("%05d", period)+String.format("%04d",numSamples)+"/");
			}				
		}
		else if(mode.equals(OfflineTask.Mode.ANALOG)){
			if (units.equals(OfflineTask.Units.MINUTES)){
				this.board.communicate(Board.Mode.SEND_READ,"COTAI"+pin+"M"+String.format("%05d", period)+String.format("%04d",numSamples)+"/");
			}
			else if (units.equals(OfflineTask.Units.MILLISECONDS)){
				this.board.communicate(Board.Mode.SEND_READ,"COTAI"+pin+"U"+String.format("%05d", period)+String.format("%04d",numSamples)+"/");
			}	
			else if (units.equals(OfflineTask.Units.SECONDS)){
				this.board.communicate(Board.Mode.SEND_READ,"COTAI"+pin+"S"+String.format("%05d", period)+String.format("%04d",numSamples)+"/");
			}
		}
	}
	
	public synchronized  ArrayList<Integer> read(){
		String rawList = this.board.communicate(Board.Mode.SEND_READ, "OT/");			
		
		String[] splitList = rawList.split("\\.");			
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		for(int i=0; i<splitList.length-1; i++){
			list.add(Integer.parseInt(splitList[i]));
		}			
		return list;
	}
	 
}
