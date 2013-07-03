package com.example.demo;

import lib.Board;
import lib.LCD;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Lcd extends Activity {
	
	private EditText entrada;
	private LCD lcd;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lcd);
		
		entrada = (EditText)findViewById(R.id.entradaLCD);		
		Board board = (Board) getIntent().getSerializableExtra("board");
		lcd = board.createLCD();
	}
	
	public void enviarLCD(View view) {
		lcd.write(entrada.getText().toString());
		entrada.setText("");	
	}
	
}
