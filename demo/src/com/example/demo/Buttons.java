package com.example.demo;

import lib.Board;
import lib.DigitalInput;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

public class Buttons extends Activity {

	private DigitalInput up;
	private DigitalInput down;
	private DigitalInput right;
	private DigitalInput left;
	private DigitalInput center;
	private DigitalInput p1;
	private DigitalInput p2;
	private ToggleButton upB;
	private ToggleButton downB;
	private ToggleButton rightB;
	private ToggleButton leftB;
	private ToggleButton centerB;
	private ToggleButton p1B;
	private ToggleButton p2B;
	
	private btnT btnThread;
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buttons);
		
		Board board = (Board) getIntent().getSerializableExtra("board");
		up = board.createDigitalInput(DigitalInput.Pin._14);
		down = board.createDigitalInput(DigitalInput.Pin._15);
		right = board.createDigitalInput(DigitalInput.Pin._12);
		left = board.createDigitalInput(DigitalInput.Pin._11);
		center = board.createDigitalInput(DigitalInput.Pin._13);
		p1 = board.createDigitalInput(DigitalInput.Pin._16);
		p2 = board.createDigitalInput(DigitalInput.Pin._17);
		
    	upB = (ToggleButton)findViewById(R.id.up);
    	downB = (ToggleButton)findViewById(R.id.down);
    	rightB = (ToggleButton)findViewById(R.id.right);
    	leftB = (ToggleButton)findViewById(R.id.left);
    	centerB = (ToggleButton)findViewById(R.id.center);
    	p1B = (ToggleButton)findViewById(R.id.p1);
    	p2B = (ToggleButton)findViewById(R.id.p2);
    	
    	/*boton1Thread = new btn1();
    	boton1Thread.execute();
    	boton2Thread = new btn2();
    	boton2Thread.execute();*/
    	
    	
	}
	
	private void setP1(final boolean state){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				p1B.setChecked(state);
			}
		});
		
	}
	
	private void setP2(final boolean state){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				p2B.setChecked(state);
			}
		});
		
	}
	
	private void setUp(final boolean state){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				upB.setChecked(state);
			}
		});
		
	}
	
	private void setDown(final boolean state){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				downB.setChecked(state);
			}
		});
		
	}
	
	private void setRight(final boolean state){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				rightB.setChecked(state);
			}
		});
		
	}
	
	private void setLeft(final boolean state){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				leftB.setChecked(state);
			}
		});
		
	}
	
	private void setCenter(final boolean state){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				centerB.setChecked(state);
			}
		});
		
	}
	
	private class btnT extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			while (true){
				
				if (isCancelled()){
					break;
				}
				if (p1.read()){
					setP1(false);	
				}
				else{
					setP1(true);	
				}
				
				if (p2.read()){
					setP2(false);	
				}
				else{
					setP2(true);	
				}
				
				if (up.read()){
					setUp(false);	
				}
				else{
					setUp(true);	
				}
				
				if (down.read()){
					setDown(false);	
				}
				else{
					setDown(true);	
				}
				
				if (center.read()){
					setCenter(false);	
				}
				else{
					setCenter(true);	
				}
				
				if (right.read()){
					setRight(false);	
				}
				else{
					setRight(true);	
				}
				
				if (left.read()){
					setLeft(false);	
				}
				else{
					setLeft(true);	
				}
				
				try {
					Thread.sleep(2);
				} 
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;	
		}	
	}
	
	
	public void onDestroy() {
	     
	     btnThread.cancel(true);	     
	     super.onDestroy();
	}
	
	public void onPause(){
		
	    btnThread.cancel(true);	    
	    super.onPause();		
	}
	
	public void onResume(){
		
    	btnThread = new btnT();
    	btnThread.execute();    	
    	super.onResume();
	}
	
	
}
