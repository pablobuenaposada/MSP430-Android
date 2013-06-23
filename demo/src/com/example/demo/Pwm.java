package com.example.demo;

import lib.Board;
import lib.PWM;
import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Pwm extends Activity {
	
	private PWM pwm;
	private PWM pwm2;
	private SeekBar bar;
	private SeekBar bar2;
	private TextView pwmText;
	private TextView pwmText2;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pwm);
		
		bar = (SeekBar)findViewById(R.id.seekBar1);
		bar2 = (SeekBar)findViewById(R.id.seekBar2);
		pwmText = (TextView)findViewById(R.id.textView1);
		pwmText2 = (TextView)findViewById(R.id.textView2);
		Board board = (Board) getIntent().getSerializableExtra("board");
		pwm = board.createPWM(PWM.Pin._42,1000,0);
		pwm2 = board.createPWM(PWM.Pin._43,1000,0);
		
		configBars();	
	}
	
	private void configBars(){
		bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar arg0,int progress, boolean arg2) {
				pwm.setDutyNonACK(progress);
				pwmText.setText("Duty:"+String.valueOf(progress));
			}
			
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub				
			}
			
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub				
			}
		});
		
		bar2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar arg0,int progress, boolean arg2) {
				pwm2.setDutyNonACK(progress);
				pwmText2.setText("Duty:"+String.valueOf(progress));
			}
			
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub				
			}
			
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub				
			}
		});
	}
	
	
	
}
