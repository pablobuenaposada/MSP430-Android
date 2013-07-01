package com.example.demo;

import lib.Board;
import lib.DigitalInput;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

public class Buttons extends Activity {

	private DigitalInput up;
	private DigitalInput down;
	private DigitalInput right;
	private DigitalInput left;
	private DigitalInput center;
	private DigitalInput p1;
	private DigitalInput p2;
	private ToggleButton upB;
	private ToggleButton downB;
	private ToggleButton rightB;
	private ToggleButton leftB;
	private ToggleButton centerB;
	private ToggleButton p1B;
	private ToggleButton p2B;
	private upT upThread;
	private downT downThread;
	private rightT rightThread;
	private leftT leftThread;
	private centerT centerThread;
	private p1T p1Thread;
	private p2T p2Thread;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buttons);
		
		Board board = (Board) getIntent().getSerializableExtra("board");
		up = board.createDigitalInput(DigitalInput.Pin._21);
		down = board.createDigitalInput(DigitalInput.Pin._22);
		right = board.createDigitalInput(DigitalInput.Pin._23);
		left = board.createDigitalInput(DigitalInput.Pin._24);
		center = board.createDigitalInput(DigitalInput.Pin._25);
		p1 = board.createDigitalInput(DigitalInput.Pin._16);
		p2 = board.createDigitalInput(DigitalInput.Pin._17);
		
    	upB = (ToggleButton)findViewById(R.id.up);
    	downB = (ToggleButton)findViewById(R.id.down);
    	rightB = (ToggleButton)findViewById(R.id.right);
    	leftB = (ToggleButton)findViewById(R.id.left);
    	centerB = (ToggleButton)findViewById(R.id.center);
    	p1B = (ToggleButton)findViewById(R.id.p1);
    	p2B = (ToggleButton)findViewById(R.id.p2);
    	
    	/*boton1Thread = new btn1();
    	boton1Thread.execute();
    	boton2Thread = new btn2();
    	boton2Thread.execute();*/
    	
    	
	}
	
	private void setP1(final boolean state){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				p1B.setChecked(state);
			}
		});
		
	}
	
	private void setP2(final boolean state){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				p2B.setChecked(state);
			}
		});
		
	}
	
	private void setUp(final boolean state){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				upB.setChecked(state);
			}
		});
		
	}
	
	private void setDown(final boolean state){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				downB.setChecked(state);
			}
		});
		
	}
	
	private void setRight(final boolean state){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				rightB.setChecked(state);
			}
		});
		
	}
	
	private void setLeft(final boolean state){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				leftB.setChecked(state);
			}
		});
		
	}
	
	private void setCenter(final boolean state){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				centerB.setChecked(state);
			}
		});
		
	}
	
	private class p1T extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			while (true){
				if (isCancelled()){
					break;
				}
				if (p1.read()){
					setP1(true);	
				}
				else{
					setP1(false);	
				}	
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
	
	private class p2T extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			while (true){
				if (isCancelled()){
					break;
				}
				if (p2.read()){
					setP2(true);	
				}
				else{
					setP2(false);	
				}	
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
	
	private class upT extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			while (true){
				if (isCancelled()){
					break;
				}
				if (up.read()){
					setUp(true);	
				}
				else{
					setUp(false);	
				}	
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
	
	private class downT extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			while (true){
				if (isCancelled()){
					break;
				}
				if (down.read()){
					setDown(true);	
				}
				else{
					setDown(false);	
				}	
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
	
	private class leftT extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			while (true){
				if (isCancelled()){
					break;
				}
				if (left.read()){
					setLeft(true);	
				}
				else{
					setLeft(false);	
				}	
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
	
	private class rightT extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			while (true){
				if (isCancelled()){
					break;
				}
				if (right.read()){
					setRight(true);	
				}
				else{
					setRight(false);	
				}	
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
	
	private class centerT extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			while (true){
				if (isCancelled()){
					break;
				}
				if (center.read()){
					setCenter(true);	
				}
				else{
					setCenter(false);	
				}	
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
	     upThread.cancel(true);	
	     downThread.cancel(true);	
	     leftThread.cancel(true);	
	     rightThread.cancel(true);	
	     centerThread.cancel(true);	
	     p1Thread.cancel(true);	
	     p2Thread.cancel(true);	
	     super.onDestroy();
	}
	
	public void onPause(){
		upThread.cancel(true);	
	    downThread.cancel(true);	
	    leftThread.cancel(true);	
	    rightThread.cancel(true);	
	    centerThread.cancel(true);
	    p1Thread.cancel(true);	
	    p2Thread.cancel(true);
	    super.onPause();		
	}
	
	public void onResume(){
		upThread = new upT();
    	upThread.execute(); 
    	downThread = new downT();
    	downThread.execute(); 
    	leftThread = new leftT();
    	leftThread.execute(); 
    	rightThread = new rightT();
    	rightThread.execute(); 
    	centerThread = new centerT();
    	centerThread.execute(); 
    	p1Thread = new p1T();
    	p1Thread.execute();
    	p2Thread = new p2T();
    	p2Thread.execute();
    	super.onResume();
	}
	
	
}
