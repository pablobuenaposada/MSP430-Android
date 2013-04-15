package com.example.btmsp;

import com.example.btmsp.Board.AnalogInput;
import com.example.btmsp.Board.DigitalInput;
import com.example.btmsp.Board.DigitalOutput;
import com.example.btmsp.Board.PWM;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnSeekBarChangeListener {

	private Board board;	
	private AnalogInput pot;
	//private DigitalOutput led;
	private PWM pwm;	
	private SeekBar bar;
	private TextView t;
	private TextView pot_text;
	int i=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bar = (SeekBar)findViewById(R.id.seekBar1); // make seekbar object
        bar.setOnSeekBarChangeListener(this); // set seekbar listener.
		t = (TextView)findViewById(R.id.textView1);
		pot_text = (TextView)findViewById(R.id.textView2);
        board = new Board(this,"20:13:01:23:01:57");
		//led = board.createDigitalOutput(17);
		pot = board.createAnalogInput(67);
		//pwm = board.createPWM(42,120000,500);
		new Thread(receive).start();
        	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);		
		return true;
	}
	
	public void sendButton(View view) {
		//int a = pot.read();		
		//Toast.makeText(this,Integer.toString(a) , Toast.LENGTH_SHORT).show();		
		
		/*for(int i=0; i < 99; i++){
			pwm = board.createPWM(42,1000,i);
		}*/
		//pot_text.setText("xxxxxxxxxxxxxxxxxx");
		pwm = board.createPWM(42,1000,500);
	}
	
	public final Runnable receive = new Thread(){
		public void run(){
			
			while (true){
				/*try {
					//Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				setText(String.valueOf(pot.read()));
			}
		}
	};
	
	private void setText(final String str) {
		runOnUiThread(new Runnable() {
		@Override
		public void run() {
			pot_text.setText(str);
			}
		});
	}
    
    public void onDestroy() {
        super.onDestroy();
        board.destroy();
    }

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
		pwm = board.createPWM(42,1000,progress);
		t.setText(String.valueOf(progress));
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
    
}
