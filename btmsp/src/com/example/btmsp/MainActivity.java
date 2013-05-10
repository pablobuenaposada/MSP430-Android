package com.example.btmsp;

import java.util.ArrayList;

import com.example.btmsp.Board.AnalogInput;
import com.example.btmsp.Board.DigitalInput;
import com.example.btmsp.Board.DigitalOutput;
import com.example.btmsp.Board.OfflineTask;
import com.example.btmsp.Board.PWM;
import com.example.btmsp.Board.Serial;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnSeekBarChangeListener {

	private Board board = null;	
	private AnalogInput pot;
	private DigitalOutput led1;
	private DigitalOutput led2;
	private DigitalInput button1;
	private DigitalInput button2;
	private PWM pwm;	
	private SeekBar bar;
	private TextView potText;
	private TextView dutyText;
	private TextView button1Text;
	private TextView button2Text;
	private TextView list;
	private ProgressBar progressBar;
	private OfflineTask ot;
	private boton1 boton1Thread;
	private boton2 boton2Thread;
	private pot potThread;
	private Serial serial;
	int i=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bar = (SeekBar)findViewById(R.id.seekBar1); // make seekbar object
        bar.setOnSeekBarChangeListener(this); // set seekbar listener.
		potText = (TextView)findViewById(R.id.textView7);
		dutyText = (TextView)findViewById(R.id.textView1);
		button1Text = (TextView)findViewById(R.id.textView2);
		button2Text = (TextView)findViewById(R.id.textView5);
		list = (TextView)findViewById(R.id.textView8);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        		
		try {
			//board = new Board(this,"20:13:01:23:01:57"); 
			board = new Board("20:13:01:24:01:46"); //a 19200bauds
		}
		catch (BluetoothDisabled e) {
			this.finish();
		} 
		catch (NoBluetoothSupported e) {			
		}
		
		button1 = board.createDigitalInput(26);
        button2 = board.createDigitalInput(27);
		led1 = board.createDigitalOutput(10);
		led2 = board.createDigitalOutput(11);
		pot = board.createAnalogInput(67);
		pwm = board.createPWM(42,1000,500);
		//ot = board.createOfflineTask(26,'d',30000,30);
		ot = board.createOfflineTask(67,'a',1,3);
		serial = board.createSerial();
		boton1Thread = new boton1();
		boton1Thread.execute();
		boton2Thread = new boton2();
		boton2Thread.execute();
		potThread = new pot();
		potThread.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);		
		return true;
	}
	
	public void button1(View view) {
	    // Is the toggle on?
	    boolean on = ((ToggleButton) view).isChecked();	    
	    if (on) {
	    	led1.write(true);
	    } else {
	    	led1.write(false);
	    }	    
	    
	    /*ArrayList<Integer> a = ot.read();
	    String b = "";
	    for(int i=0; i < a.size(); i++){
	    	b = b + a.get(i).toString();
	    }
	    b=b+"total"+a.size();
	    list.setText(b);*/
	    serial.send("hola");
	}
	
	public void button2(View view) {
	    // Is the toggle on?
		//ot.start();
	    boolean on = ((ToggleButton) view).isChecked();	   
	    if (on) {
	    	led2.write(true);
	    } else {
	    	led2.write(false);
	    }
	    list.setText(serial.read());
	}	
	
	private void setTextButton1(final String str) {
		runOnUiThread(new Runnable() {
		@Override
		public void run() {
			button1Text.setText(str);
			}
		});
	}
	
	private void setTextButton2(final String str) {
		runOnUiThread(new Runnable() {
		@Override
		public void run() {
			button2Text.setText(str);
			}
		});
	}
	
	private void setPotText(final String str) {
		runOnUiThread(new Runnable() {
		@Override
		public void run() {
			potText.setText(str);
			progressBar.setProgress(Integer.parseInt(str));
			}
		});
	}
    
    public void onDestroy() {
    	boton1Thread.cancel(true);
    	boton2Thread.cancel(true);
    	potThread.cancel(true);    	
    	board.destroy();
    	super.onDestroy();   	             
    }

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {		
		dutyText.setText(String.valueOf(progress));
		pwm.setDuty(progress);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {		
	}
	
	private class boton1 extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			while (true){
				if (isCancelled()){
					break;
				}
				if (button1.read()){
					setTextButton1("on");					
				}
				else{
					setTextButton1("off");					
				}				
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;			
		}				  
	}
	
	private class boton2 extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			while (true){
				if (isCancelled()){
					break;
				}
				if (button2.read()){
					setTextButton2("on");					
				}
				else{
					setTextButton2("off");					
				}				
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;			
		}				  
	}
	
	private class pot extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			while (true){
				if (isCancelled()){
					break;
				}
				setPotText(String.valueOf(pot.read()));		
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;			
		}				  
	}
    
}



