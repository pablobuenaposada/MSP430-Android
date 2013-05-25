package com.example.btmsp;

public class SPI {
	private Board board;

	public SPI(Board board){
		this.board = board;
		this.board.communicate(Board.Mode.SEND_READ,"SESPIB3/");
	}
	
	public synchronized String read(){
		String received = this.board.communicate(Board.Mode.SEND_READ,"ESPIB3R/");
		return received.substring(0, received.length()-1);
	}
}
