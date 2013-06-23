package com.example.demo;



import lib.AnalogInput;
import lib.Board;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Pot extends Activity {
	
	private ProgressBar progressBar;
	private ProgressBar progressBar2;
	private AnalogInput pot;
	private AnalogInput pot2;
	private pot pot1Thread;
	private pot2 pot2Thread;
	private TextView potText;
	private TextView pot2Text;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pot);
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
		potText = (TextView)findViewById(R.id.textView5);
		pot2Text = (TextView)findViewById(R.id.textView6);
		
		Board board = (Board) getIntent().getSerializableExtra("board");
		pot = board.createAnalogInput(AnalogInput.Pin._74);
		pot2 = board.createAnalogInput(AnalogInput.Pin._67);
		
		
		//pot1Thread = new pot();
		//boton2Thread = new btn2();
		//pot1Thread.execute(); 
		
	}
	
	private void setPot(final String str) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				float voltage = (float) (Float.parseFloat(str)*0.0007496948);
				potText.setText("ADC:"+str+"  "+String.format("%.02f",voltage)+"v");
				progressBar.setProgress(Integer.parseInt(str));
			}
		});
	}
	
	private void setPot2(final String str) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				float voltage = (float) (Float.parseFloat(str)*0.0007496948);
				pot2Text.setText("ADC:"+str+"  "+String.format("%.02f",voltage)+"v");
				progressBar2.setProgress(Integer.parseInt(str));
			}
		});
	}
	
	
	
	private class pot extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			while (true){
				if (isCancelled()){
					break;
				}
				setPot(String.valueOf(pot.read()));	
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;	
		}	
	}
	
	private class pot2 extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			while (true){
				if (isCancelled()){
					break;
				}
				setPot2(String.valueOf(pot2.read()));	
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;	
		}	
	}
	
	public void onDestroy() {
	     pot1Thread.cancel(true);	
	     pot2Thread.cancel(true);	
	     super.onDestroy();
	}
	
	public void onPause(){
		Log.e("bbbbbbbbbbb","bbbb");
		pot1Thread.cancel(true);	
	    pot2Thread.cancel(true);
	    super.onPause();		
	}
	
	public void onResume(){
		pot1Thread = new pot();
		pot2Thread = new pot2();
		pot1Thread.execute();    	
		pot2Thread.execute();
		super.onResume();
	}
}