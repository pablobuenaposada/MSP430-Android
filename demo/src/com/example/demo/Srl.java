package com.example.demo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
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

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serial);
		
		entrada = (EditText)findViewById(R.id.editText1);
		
		Board board = (Board) getIntent().getSerializableExtra("board");
		serial = board.createSerial();
		texto = (EditText)findViewById(R.id.recibido);

	}
	
	public void enviar(View view) {		
		serial.send(entrada.getText().toString());
		entrada.setText("");
	}
	
	private class recibido extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			while (true){
				if (isCancelled()){
					break;
				}
				texto.setText(texto.getText()+"\n"+serial.read());
				try {
					Thread.sleep(10);
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

