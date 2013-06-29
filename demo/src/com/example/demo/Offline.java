package com.example.demo;

import java.util.ArrayList;

import lib.Board;
import lib.OfflineTask;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class Offline extends Activity{
	private OfflineTask ot;
	private Board board;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offline);
				
		board = (Board) getIntent().getSerializableExtra("board");		
	}
	
	public void button(View view) {
		RadioButton radio0 = (RadioButton) findViewById(R.id.radio0);
		RadioButton radio1 = (RadioButton) findViewById(R.id.radio1);
		RadioButton radio2 = (RadioButton) findViewById(R.id.radio2);
		
		TextView tiempoText = (EditText)findViewById(R.id.numero);
		int tiempo = Integer.parseInt(tiempoText.getText().toString());
		TextView muestrasText = (EditText)findViewById(R.id.muestras);
		int muestras = Integer.parseInt(muestrasText.getText().toString());		
		
		if (radio0.isChecked()) {
			ot = board.createOfflineTask(OfflineTask.Pin._67,OfflineTask.Mode.ANALOG,OfflineTask.Units.MINUTES,tiempo,muestras);
		}
		else if (radio1.isChecked()) {
			ot = board.createOfflineTask(OfflineTask.Pin._67,OfflineTask.Mode.ANALOG,OfflineTask.Units.SECONDS,tiempo,muestras);
		}
		else if (radio2.isChecked()){
			ot = board.createOfflineTask(OfflineTask.Pin._67,OfflineTask.Mode.ANALOG,OfflineTask.Units.MILLISECONDS,tiempo,muestras);
		}
		
		ot.start();
	}
	
	public void button2(View view) {
		ArrayList<Integer> data = ot.read();
		String dataString = "";
		for(int i=0; i < data.size(); i++){
			dataString = dataString + data.get(i).toString()+".";
		}
		dataString=dataString+"total"+data.size();
	}
	
	
	
}
