package lib;

public class LCD {
	private Board board;
	
	public LCD(Board board){
		this.board = board;
		this.board.communicate(Board.Mode.SEND_READ,"CLCD/");
	}
	
	public synchronized void write(String tx){
		this.board.communicate(Board.Mode.SEND_READ,"LCD"+tx+"/");			
	}
	
}
