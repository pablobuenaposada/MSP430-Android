package com.example.btmsp;

import com.example.btmsp.Board.AnalogInput;
import com.example.btmsp.Board.DigitalInput;
import com.example.btmsp.Board.DigitalOutput;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Board board;
	private boolean b = false;
	private AnalogInput pot;
	private DigitalOutput led;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		board = new Board(this,"20:13:01:23:01:57");
		//led = board.createDigitalOutput(17);
		pot = board.createAnalogInput(67);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void sendButton(View view) {
		int a = pot.read();
		
		Toast.makeText(this,Integer.toString(a) , Toast.LENGTH_SHORT).show();		
		
	}
			
    
    public void onDestroy() {
        super.onDestroy();
        board.destroy();
    }
    
}
