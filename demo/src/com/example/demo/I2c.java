package com.example.demo;

import java.util.ArrayList;

import lib.Board;
import lib.I2C;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

public class I2c extends Activity {
	
	private I2C i2c;
	private tempT tempThread;
	private Board board;
	private TextView tempText;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.i2c);
		tempText = (TextView)findViewById(R.id.temp);
		
		board = (Board) getIntent().getSerializableExtra("board");
		i2c = board.createI2C(0xE4, I2C.Mode.MASTER);
		ArrayList<Integer> a = new ArrayList<Integer>();
		a.add(0x02);		
		i2c.send(a);
		
	}
	
	public void led(View view) {
		i2c = board.createI2C(0xC0, I2C.Mode.MASTER);
		boolean on = ((ToggleButton) view).isChecked();
		ToggleButton blanco= (ToggleButton)findViewById(R.id.toggleBlanco);
		if (on) {			
			ArrayList<Integer> a = new ArrayList<Integer>();
			a.add(0x05);
			if(blanco.isChecked()){
				a.add(0xF0);
			}
			else{
				a.add(0xF1);
			}			
			i2c.send(a);
		}
		else {			
			ArrayList<Integer> a = new ArrayList<Integer>();
			a.add(0x05);
			if(blanco.isChecked()){
				a.add(0xF4);
			}
			else{
				a.add(0xF5);
			}
			i2c.send(a);
		}
	}
	
	public void led2(View view) {
		i2c = board.createI2C(0xC0, I2C.Mode.MASTER);
		boolean on = ((ToggleButton) view).isChecked();
		ToggleButton azul= (ToggleButton)findViewById(R.id.toggleAzul);
		if (on) {			
			ArrayList<Integer> a = new ArrayList<Integer>();
			a.add(0x05);
			if(azul.isChecked()){
				a.add(0xF0);
			}
			else{
				a.add(0xF4);
			}	
			i2c.send(a);
		}
		else {			
			ArrayList<Integer> a = new ArrayList<Integer>();
			a.add(0x05);
			if(azul.isChecked()){
				a.add(0xF1);
			}
			else{
				a.add(0xF5);
			}	
			i2c.send(a);
		}
	}
	
	private void setTemp() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				i2c = board.createI2C(0x92, I2C.Mode.MASTER);
				ArrayList<Integer> a = i2c.read(2);
				float temperatura = (a.get(0)*256)+a.get(1);
				temperatura = (float) ((temperatura/32)*0.125);				
				tempText.setText(String.format("%.02f",temperatura)+" ºC");	
			}
		});
	}
	
	private class tempT extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			while (true){
				if (isCancelled()){
					break;
				}
				setTemp();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;	
		}	
	}
	
	public void onDestroy() {
	     tempThread.cancel(true);	     
	     super.onDestroy();
	}
	
	public void onPause(){		
		tempThread.cancel(true);	    
	    super.onPause();		
	}
	
	public void onResume(){
		tempThread = new tempT();
		tempThread.execute();
		super.onResume();
	}

}
