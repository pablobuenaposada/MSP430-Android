package com.example.demo;

import lib.BluetoothDisabled;
import lib.Board;
import lib.NoBluetoothSupported;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity {
	public Board board;
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
		
		
		addTabSpec("led", "LED", R.drawable.ic_launcher,LedButton.class);
		addTabSpec("pwm", "PWM", R.drawable.ic_launcher,Pwm.class);
		addTabSpec("buttons", "BUT", R.drawable.ic_launcher,Buttons.class);
		addTabSpec("pot", "ADC", R.drawable.ic_launcher,Pot.class);
		addTabSpec("srl", "SRL", R.drawable.ic_launcher,Srl.class);
		addTabSpec("off", "OFF", R.drawable.ic_launcher,Offline.class);
		addTabSpec("i2c", "I2C", R.drawable.ic_launcher,I2c.class);
		addTabSpec("spi", "SPI", R.drawable.ic_launcher,Spi.class);
		addTabSpec("lcd", "LCD", R.drawable.ic_launcher,Lcd.class);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void addTabSpec(String tag, String labelId, int iconId, Class<? extends Activity> activityClass){
		Drawable icon = getResources().getDrawable(iconId);
		Intent intent = new Intent().setClass(this, activityClass);		
		intent.putExtra("board", board);
		TabHost tabHost = getTabHost();		
		TabSpec tabSpec = tabHost //
				.newTabSpec(tag) //
				.setIndicator(labelId, icon) //
				.setContent(intent);
		tabHost.addTab(tabSpec);
	}
	
	public void onDestroy() {	     
	     board.destroy();
	     super.onDestroy();
	}

}
