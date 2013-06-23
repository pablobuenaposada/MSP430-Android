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

	private DigitalInput button1;
	private DigitalInput button2;
	private ToggleButton togbtn1;
	private ToggleButton togbtn2;
	private btn1 boton1Thread;
	private btn2 boton2Thread;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buttons);
		
		Board board = (Board) getIntent().getSerializableExtra("board");
		button1 = board.createDigitalInput(DigitalInput.Pin._26);
        button2 = board.createDigitalInput(DigitalInput.Pin._27);
    	togbtn1 = (ToggleButton)findViewById(R.id.toggleButton3);
    	togbtn2 = (ToggleButton)findViewById(R.id.toggleButton4);
    	
    	/*boton1Thread = new btn1();
    	boton1Thread.execute();
    	boton2Thread = new btn2();
    	boton2Thread.execute();*/
    	
    	
	}
	
	private void setTogbtn1(final boolean state){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				togbtn1.setChecked(state);
			}
		});
		
	}
	
	private void setTogbtn2(final boolean state){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				togbtn2.setChecked(state);
			}
		});
		
	}
	
	private class btn1 extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			while (true){
				Log.e("aaaaa", "aaaaaaa");
				if (isCancelled()){
					break;
				}
				if (button1.read()){
					setTogbtn1(true);	
				}
				else{
					setTogbtn1(false);	
				}	
				try {
					Thread.sleep(10);
				} 
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;	
		}	
	}
	
	private class btn2 extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			while (true){
				if (isCancelled()){
					break;
				}
				if (button2.read()){
					setTogbtn2(true);	
				}
				else{
					setTogbtn2(false);	
				}	
				try {
					Thread.sleep(10);
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
	     boton1Thread.cancel(true);	
	     boton2Thread.cancel(true);	
	     super.onDestroy();
	}
	
	public void onPause(){
		Log.e("bbbbbbbbbbb","bbbb");
		boton1Thread.cancel(true);	
	    boton2Thread.cancel(true);
	    super.onPause();		
	}
	
	public void onResume(){
		boton1Thread = new btn1();
		boton2Thread = new btn2();
    	boton1Thread.execute();    	
    	boton2Thread.execute();
    	super.onResume();
	}
	
	
}
