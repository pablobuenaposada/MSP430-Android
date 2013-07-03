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
	private ProgressBar progressBar3;
	private ProgressBar progressBar4;
	private AnalogInput pot;
	private AnalogInput pot2;
	private AnalogInput pot3;
	private AnalogInput pot4;
	private AnalogInput pot7;
	private pot pot1Thread;
	private pot2 pot2Thread;
	private pot3 pot3Thread;
	private pot4 pot4Thread; 
	private TextView potText;
	private TextView pot2Text;
	private TextView pot3Text;
	private TextView pot4Text;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pot);
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
		progressBar3 = (ProgressBar) findViewById(R.id.lmBar);
		progressBar4 = (ProgressBar) findViewById(R.id.distBar);
		potText = (TextView)findViewById(R.id.textView5);
		pot2Text = (TextView)findViewById(R.id.textView6);
		pot3Text = (TextView)findViewById(R.id.lmText);
		pot4Text = (TextView)findViewById(R.id.distText);
		
		Board board = (Board) getIntent().getSerializableExtra("board");
		pot = board.createAnalogInput(AnalogInput.Pin._74);
		pot2 = board.createAnalogInput(AnalogInput.Pin._67);
		pot3 = board.createAnalogInput(AnalogInput.Pin._65);
		pot4 = board.createAnalogInput(AnalogInput.Pin._66);
		pot7 = board.createAnalogInput(AnalogInput.Pin._75);
		
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
	
	private void setPot3(final String str) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				float voltage = (float) (Float.parseFloat(str)*0.0007496948);
				pot3Text.setText("ADC:"+str+"  "+String.format("%.02f",voltage)+"v");
				progressBar3.setProgress(Integer.parseInt(str));
			}
		});
	}
	
	private void setPot4(final String str) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				float voltage = (float) (Float.parseFloat(str)*0.0007496948);
				pot4Text.setText("ADC:"+str+"  "+String.format("%.02f",voltage)+"v");
				progressBar4.setProgress(Integer.parseInt(str));
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
				pot7.read();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setPot2(String.valueOf(pot2.read()));
				pot7.read();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setPot3(String.valueOf(pot3.read()));
				pot7.read();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setPot4(String.valueOf(pot4.read()));
				pot7.read();
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
	
	private class pot3 extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			while (true){
				if (isCancelled()){
					break;
				}
				setPot3(String.valueOf(pot3.read()));	
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
	
	private class pot4 extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			while (true){
				if (isCancelled()){
					break;
				}
				setPot4(String.valueOf(pot4.read()));	
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
	     //pot2Thread.cancel(true);	
	     //pot3Thread.cancel(true);	
	     //pot4Thread.cancel(true);	
	     super.onDestroy();
	}
	
	public void onPause(){		
		pot1Thread.cancel(true);	
	    //pot2Thread.cancel(true);
	    //pot3Thread.cancel(true);	
	    //pot4Thread.cancel(true);
	    super.onPause();		
	}
	
	public void onResume(){
		pot1Thread = new pot();
		//pot2Thread = new pot2();
		//pot3Thread = new pot3();
		//pot4Thread = new pot4();
		pot1Thread.execute();    	
		//pot2Thread.execute();
		//pot3Thread.execute();
		//pot4Thread.execute();
		super.onResume();
	}
}