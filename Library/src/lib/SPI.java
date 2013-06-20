package lib;


public class SPI {
	private Board board;
	private SPI.Mode mode;
	
	public enum Mode{
    	MASTER,SLAVE;    	
    }

	public SPI(Board board,SPI.Mode mode){
		this.board = board;
		this.mode = mode;
		if (this.mode.equals(SPI.Mode.MASTER)){
			this.board.communicate(Board.Mode.SEND_READ,"CSPIB3M/");
		}
		else if(this.mode.equals(SPI.Mode.SLAVE)){
			this.board.communicate(Board.Mode.SEND_READ,"CSPIB3S/");
		}
	}
	
	public synchronized String read(){
		String received = this.board.communicate(Board.Mode.SEND_READ,"SPIB3R/");
		return received.substring(0, received.length()-1);
	}
	
	public synchronized void send(String tx){
		this.board.communicate(Board.Mode.SEND_READ,"SPIB3T"+tx+"/");
	}
}
