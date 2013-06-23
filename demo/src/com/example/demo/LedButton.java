package com.example.demo;

import lib.Board;
import lib.DigitalOutput;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

public class LedButton extends Activity {
	
	private DigitalOutput led;
	private DigitalOutput led2;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.led);
		
		Board board = (Board) getIntent().getSerializableExtra("board");
		led = board.createDigitalOutput(DigitalOutput.Pin._10);
		led2 = board.createDigitalOutput(DigitalOutput.Pin._11);
	}	
	
	public void button(View view) {		
		boolean on = ((ToggleButton) view).isChecked();	
		if (on) {
			led.write(true);
		} 
		else {
			led.write(false);
		}		
	}
	
	public void button2(View view) {		
		boolean on = ((ToggleButton) view).isChecked();	
		if (on) {
			led2.write(true);
		} 
		else {
			led2.write(false);
		}		
	}
	
}
