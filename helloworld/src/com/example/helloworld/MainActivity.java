package com.example.helloworld;

import lib.BluetoothDisabled;
import lib.Board;
import lib.DigitalOutput;
import lib.DigitalOutput.Pin;
import lib.NoBluetoothSupported;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	
	private Board board;
	private DigitalOutput led;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		try {
			board = new Board("20:13:01:24:01:46");
		} catch (BluetoothDisabled e) {
			this.finish();
		} catch (NoBluetoothSupported e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		led = board.createDigitalOutput(Pin._20);
	}
	
	public void button(View view){
		led.write(true);
		
	}

	

}
