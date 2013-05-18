package com.example.btmsp;

public class Serial{
	private Board board;

	public Serial(Board board){
		this.board = board;
		this.board.communicate(Board.Mode.SEND_READ,"SUARTA3/");
	}
	
	public synchronized void send(String tx){
		this.board.communicate(Board.Mode.SEND_READ,"UARTA3T"+tx+"/");			
	}
	
	public synchronized String read(){
		String received = this.board.communicate(Board.Mode.SEND_READ,"UARTA3R/");
		return received.substring(0, received.length()-1);
	}
}
