package com.example.btmsp;

import com.example.btmsp.Board.AnalogInput;
import com.example.btmsp.Board.DigitalInput;
import com.example.btmsp.Board.DigitalOutput;
import com.example.btmsp.Board.PWM;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnSeekBarChangeListener {

	private Board board;	
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
	private ProgressBar progressBar;
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
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        board = new Board(this,"20:13:01:23:01:57");		
        button1 = board.createDigitalInput(26);
        button2 = board.createDigitalInput(27);
		led1 = board.createDigitalOutput(10);
		led2 = board.createDigitalOutput(11);
		pot = board.createAnalogInput(67);
		pwm = board.createPWM(42,1000,500);		
		new Thread(button1Thread).start();   
		new Thread(button2Thread).start();
		new Thread(potThread).start();		
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
	}
	
	public void button2(View view) {
	    // Is the toggle on?
	    boolean on = ((ToggleButton) view).isChecked();	   
	    if (on) {
	    	led2.write(true);
	    } else {
	    	led2.write(false);
	    }	    
	}
	
	public final Runnable button1Thread = new Thread(){
		public void run(){			
			while (true){
				
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
		}
	};
	
	public final Runnable button2Thread = new Thread(){
		public void run(){			
			while (true){				
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
		}
	};
	
	public final Runnable potThread = new Thread(){
		public void run(){			
			while (true){				
				setPotText(String.valueOf(pot.read()));		
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	
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
        super.onDestroy();
        board.destroy();        
    }

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {		
		dutyText.setText(String.valueOf(progress));
		pwm.setDuty(progress);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {		
		
	}
    
}
