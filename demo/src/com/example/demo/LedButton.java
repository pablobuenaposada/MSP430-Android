package com.example.demo;

import lib.Board;
import lib.DigitalOutput;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

public class LedButton extends Activity {
	
	private DigitalOutput led0;
	private DigitalOutput led1;
	private DigitalOutput led2;
	private DigitalOutput led3;
	private DigitalOutput led4;
	private DigitalOutput led5;
	private DigitalOutput led6;
	private DigitalOutput led7;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.led);
		
		Board board = (Board) getIntent().getSerializableExtra("board");
		led0 = board.createDigitalOutput(DigitalOutput.Pin._80);
		led1 = board.createDigitalOutput(DigitalOutput.Pin._81);
		led2 = board.createDigitalOutput(DigitalOutput.Pin._82);
		led3 = board.createDigitalOutput(DigitalOutput.Pin._83);
		led4 = board.createDigitalOutput(DigitalOutput.Pin._84);
		led5 = board.createDigitalOutput(DigitalOutput.Pin._85);
		led6 = board.createDigitalOutput(DigitalOutput.Pin._86);
		led7 = board.createDigitalOutput(DigitalOutput.Pin._87);
	}	
	
	public void button(View view) {		
		boolean on = ((ToggleButton) view).isChecked();	
		if (on) {
			led0.write(true);
		} 
		else {
			led0.write(false);
		}		
	}
	
	public void button1(View view) {		
		boolean on = ((ToggleButton) view).isChecked();	
		if (on) {
			led1.write(true);
		} 
		else {
			led1.write(false);
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
	
	public void button3(View view) {		
		boolean on = ((ToggleButton) view).isChecked();	
		if (on) {
			led3.write(true);
		} 
		else {
			led3.write(false);
		}		
	}
	
	public void button4(View view) {		
		boolean on = ((ToggleButton) view).isChecked();	
		if (on) {
			led4.write(true);
		} 
		else {
			led4.write(false);
		}		
	}
	
	public void button5(View view) {		
		boolean on = ((ToggleButton) view).isChecked();	
		if (on) {
			led5.write(true);
		} 
		else {
			led5.write(false);
		}		
	}
	
	public void button6(View view) {		
		boolean on = ((ToggleButton) view).isChecked();	
		if (on) {
			led6.write(true);
		} 
		else {
			led6.write(false);
		}		
	}
	
	public void button7(View view) {		
		boolean on = ((ToggleButton) view).isChecked();	
		if (on) {
			led7.write(true);
		} 
		else {
			led7.write(false);
		}		
	}
	
}
