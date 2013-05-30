package com.example.btmsp;

import java.util.ArrayList;

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

public class MainActivity extends Activity {

	private Board board = null;	
	private AnalogInput pot;
	private AnalogInput pot2;
	private DigitalOutput led1;
	private DigitalOutput led2;
	private DigitalInput button1;
	private DigitalInput button2;
	private PWM pwm;	
	private PWM pwm2;
	private SeekBar bar;
	private SeekBar bar2;
	private TextView potText;
	private TextView dutyText;
	private TextView button1Text;
	private TextView button2Text;
	private TextView list;
	private ProgressBar progressBar;
	private ProgressBar progressBar2;
	//private OfflineTask ot;
	private boton1 boton1Thread;
	private boton2 boton2Thread;
	private pot potThread;
	private pot2 potThread2;
	private Serial serial;
	private I2C i2c;
	private SPI spi;
	int i=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bar = (SeekBar)findViewById(R.id.seekBar1); // make seekbar object
        //bar.setOnSeekBarChangeListener(this); // set seekbar listener.
        bar2 = (SeekBar)findViewById(R.id.SeekBar01); // make seekbar object
        //bar2.setOnSeekBarChangeListener(this); // set seekbar listener.
		potText = (TextView)findViewById(R.id.textView7);
		dutyText = (TextView)findViewById(R.id.textView1);
		button1Text = (TextView)findViewById(R.id.textView2);
		button2Text = (TextView)findViewById(R.id.textView5);
		list = (TextView)findViewById(R.id.textView8);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar2 = (ProgressBar) findViewById(R.id.ProgressBar01);
       
		try {
			//board = new Board(this,"20:13:01:23:01:57"); 
			board = new Board("20:13:01:24:01:46"); //a 19200bauds
		}
		catch (BluetoothDisabled e) {
			this.finish();
		} 
		catch (NoBluetoothSupported e) {			
		}
		
		button1 = board.createDigitalInput(DigitalInput.Pin._26);
        button2 = board.createDigitalInput(DigitalInput.Pin._27);
		led1 = board.createDigitalOutput(DigitalOutput.Pin._10);
		led2 = board.createDigitalOutput(DigitalOutput.Pin._11);
		pot = board.createAnalogInput(AnalogInput.Pin._74);
		pot2 = board.createAnalogInput(AnalogInput.Pin._67);
		pwm = board.createPWM(PWM.Pin._42,1000,500);
		pwm2 = board.createPWM(PWM.Pin._43,1000,500);
		spi = board.createSPI(SPI.Mode.SLAVE);
		initControls();
		
		//ot = board.createOfflineTask(26,'d',30000,30);
		//ot = board.createOfflineTask(OfflineTask.Pin._67,OfflineTask.Mode.ANALOG,OfflineTask.Units.MILLISECONDS,1000,3);
		//serial = board.createSerial();
		i2c = board.createI2C(0xC0, I2C.Mode.MASTER);
		boton1Thread = new boton1();
		boton1Thread.execute();
		boton2Thread = new boton2();
		boton2Thread.execute();
		potThread = new pot();
		potThread.execute();
		
		potThread2 = new pot2();
		potThread2.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);		
		return true;
	}
	
	public void button1(View view) {
		ArrayList<Integer> a = new ArrayList<Integer>();
		a.add(0x05);
		a.add(0xF5);
		i2c.send(a);
	    // Is the toggle on?
	    boolean on = ((ToggleButton) view).isChecked();	    
	    if (on) {
	    	led1.write(true);
	    } else {
	    	led1.write(false);
	    }
	    //list.setText(spi.read());
	    //spi.send("WEBA");
	    /*ArrayList<Integer> a = ot.read();
	    String b = "";
	    for(int i=0; i < a.size(); i++){
	    	b = b + a.get(i).toString();
	    }
	    b=b+"total"+a.size();
	    list.setText(b);
	    //serial.send("hola");*/
	}
	
	public void button2(View view) {
	    // Is the toggle on?
		//ot.start();
		ArrayList<Integer> a = new ArrayList<Integer>();
		a.add(0x05);
		a.add(0xF0);
		i2c.send(a);
	    boolean on = ((ToggleButton) view).isChecked();	   
	    if (on) {
	    	led2.write(true);
	    } else {
	    	led2.write(false);
	    }
	    //list.setText(serial.read());
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
	
	private void setPotText2(final String str) {
		runOnUiThread(new Runnable() {
		@Override
		public void run() {
			
			progressBar2.setProgress(Integer.parseInt(str));
			}
		});
	}
    
    public void onDestroy() {
    	boton1Thread.cancel(true);
    	boton2Thread.cancel(true);
    	potThread.cancel(true);    
    	potThread2.cancel(true);
    	board.destroy();
    	super.onDestroy();   	             
    }
    
    private void initControls() {
    	bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
    		public void onStopTrackingTouch(SeekBar arg0) {
    		}
 
		    public void onStartTrackingTouch(SeekBar arg0) {
		    }
		    
		    public void onProgressChanged(SeekBar arg0,int progress, boolean arg2) {
		    	pwm.setDutyNonACK(progress);		    	
		    }
    	});
    	
    	bar2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
    		public void onStopTrackingTouch(SeekBar arg0) {
    		}
 
		    public void onStartTrackingTouch(SeekBar arg0) {
		    }
		    
		    public void onProgressChanged(SeekBar arg0,int progress, boolean arg2) {
		    	pwm2.setDuty(progress);
		    }
    	});
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
					Thread.sleep(10);
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
					Thread.sleep(10);
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
				setPotText2(String.valueOf(pot2.read()));		
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
    
}



