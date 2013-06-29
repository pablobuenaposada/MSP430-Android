package lib;


public class DigitalInput{
	private Board board;
	private int pin;
	
	public enum Pin{
    	_16(16),_17(17),_21(21),_22(22),_23(23),_24(24),_25(25),_26(26),_27(27);
    	
    	private int pin;
    	
    	Pin(int pin){
    		this.pin=pin;
    	}
    	
    	public int getPin(){
    		return this.pin;
    	}
    }
	
	public DigitalInput(Board board, DigitalInput.Pin pin){
		this.board=board;
		this.pin=pin.getPin();
		board.communicate(Board.Mode.SEND_READ,"CDI"+this.pin+"/");			
	}
	
	public synchronized boolean read(){			
		if (board.communicate(Board.Mode.SEND_READ,"DI"+pin+"/").contains("1/")){
			return true;	    		  
    	}
    	else{
    		return false;
    	}			
	}
}
