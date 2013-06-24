package com.example.demo;



import android.app.Activity;
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

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serial);
		
		entrada = (EditText)findViewById(R.id.editText1);
		
		Board board = (Board) getIntent().getSerializableExtra("board");
		serial = board.createSerial();
	}
	
	public void enviar(View view) {		
		serial.send(entrada.getText().toString());
		entrada.setText("");
	}

	public void onDestroy() {	   	
	     super.onDestroy();
	}
	
	public void onPause(){		
	    super.onPause();		
	}
	
	public void onResume(){		
		super.onResume();
	}


}

