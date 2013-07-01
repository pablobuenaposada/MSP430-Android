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
	}
	
	public void led(View view) {
		i2c = board.createI2C(0xC0, I2C.Mode.MASTER);
		boolean on = ((ToggleButton) view).isChecked();	
		if (on) {			
			ArrayList<Integer> a = new ArrayList<Integer>();
			a.add(0x05);
			a.add(0xF0);
			i2c.send(a);
		}
		else {			
			ArrayList<Integer> a = new ArrayList<Integer>();
			a.add(0x05);
			a.add(0xF5);
			i2c.send(a);
		}
	}
	
	private void setTemp() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				i2c = board.createI2C(0x92, I2C.Mode.MASTER);
				ArrayList<Integer> a = i2c.read(2);
				String b = "";
				for(int i=0; i < a.size(); i++){
					b = b + a.get(i).toString()+".";
				}
				//b=b+"total"+a.size();
				tempText.setText(b);	
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
	     //tempThread.cancel(true);	     
	     super.onDestroy();
	}
	
	public void onPause(){		
		//tempThread.cancel(true);	    
	    super.onPause();		
	}
	
	public void onResume(){
		//tempThread = new tempT();
		//tempThread.execute();
		super.onResume();
	}

}
