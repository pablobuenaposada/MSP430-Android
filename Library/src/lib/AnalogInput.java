package lib;


public class AnalogInput{
	private Board board;
	private int pin;
	
	public enum Pin{
    	_65(65),_66(66),_67(67),_74(74);
    	
    	private int pin;
    	
    	Pin(int pin){
    		this.pin=pin;
    	}
    	
    	public int getPin(){
    		return this.pin;
    	}
    }
	
	public AnalogInput(Board board, AnalogInput.Pin pin){
		this.board=board;
		this.pin=pin.getPin();			
		this.board.communicate(Board.Mode.SEND_READ,"CAI"+this.pin+"/");			
	}
	
	public synchronized int read(){			
		String rawValue = board.communicate(Board.Mode.SEND_READ,"AI"+pin+"/");
		
		try{				
			return Integer.parseInt(rawValue.split("/")[0]);							
		}
		catch(java.lang.Throwable e){				
			return -1;
		}
		
	}
}