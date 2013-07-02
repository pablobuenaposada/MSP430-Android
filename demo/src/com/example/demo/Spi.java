package com.example.demo;

import lib.Board;
import lib.SPI;
import android.app.Activity;
import android.os.Bundle;

public class Spi extends Activity {
	private SPI spi;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spi);
		
		Board board = (Board) getIntent().getSerializableExtra("board");
		spi = board.createSPI(SPI.Mode.SLAVE);
		spi.send("aaaa");		
	}
}
