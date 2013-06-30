package com.example.demo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import lib.Board;
import lib.Serial;

public class Srl extends Activity {

	private Serial serial;
	private EditText entrada;
	private recibido recibidoThread;
	private TextView texto;
	private Board board;
	private Time time;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serial);
		
		time = new Time();		
		entrada = (EditText)findViewById(R.id.entrada);
		texto = (EditText)findViewById(R.id.recibido);		
		board = (Board) getIntent().getSerializableExtra("board");
		serial = board.createSerial();		
	}
	
	public void enviar(View view) {		
		serial.send(entrada.getText().toString());
		entrada.setText("");
	}
	
	private void setText(){
		String recibido = serial.read();
		if (!recibido.equals("")){
			time.setToNow();
			texto.append(time.format("%H:%M:%S")+" "+recibido+"\n");			
		}
	}
	
	private void setRecibido(){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				setText();
			}
		});		
	}
	
	private class recibido extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			while (true){
				if (isCancelled()){
					break;
				}
				setRecibido();				
				try {
					Thread.sleep(100);
				} 
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;	
		}	
	}

	public void onDestroy() {	
	     recibidoThread.cancel(true);
	     super.onDestroy();
	}
	
	public void onPause(){
		recibidoThread.cancel(true);
	    super.onPause();		
	}
	
	public void onResume(){						
		recibidoThread = new recibido();
		recibidoThread.execute();    	
		super.onResume();
	}
}

