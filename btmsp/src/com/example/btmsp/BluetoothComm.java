package com.example.btmsp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

public class BluetoothComm{
	
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");   
	private InputStream BTin = null;
    private OutputStream BTout = null;
    private Handler handler = null;
    private BluetoothSocket BTsocket = null;
	
	public BluetoothComm(Board board, Handler handler, String address){
		this.handler = handler;
		BluetoothAdapter BluetoothAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
		
        // If the adapter is null, then Bluetooth is not supported
        if (BluetoothAdapter == null) {            
            board.noBluetoothSupported();
            return;
        }        
        else if (!BluetoothAdapter.isEnabled()) {
        	board.askEnableBluetooth();                   
        }
        
        BluetoothDevice remoteDevice = BluetoothAdapter.getRemoteDevice(address);
        
        try {
			BTsocket = remoteDevice.createRfcommSocketToServiceRecord(MY_UUID);
			BTsocket.connect();
			BTin = BTsocket.getInputStream();
	        BTout = BTsocket.getOutputStream();        
		} catch (IOException e) {
			Log.d("ERROR", "Problem establishing connection");
		}
        
        //new Thread(receive).start();
        
	} 
	
	public synchronized void send(String data){		
    	try {
			BTout.write(data.getBytes());
		} catch (IOException e) {
			Log.d("ERROR", "Problem sending output");
		}		
	}
	
	public synchronized String read(){
		byte[] buffer = new byte[10];
		int bytesRead;
		String message="";
		while (!message.contains("/")){				
			try {
				bytesRead = BTin.read(buffer);
				message = message + new String(buffer).substring(0, bytesRead);
			} catch (IOException e) {
				Log.d("ERROR", "Problem reading input");
			}
		}	
		return message;
	}

	/*public final Runnable receive = new Thread(){
		public void run(){
			byte[] buffer = new byte[1024];
			int bytes;
			while (true){				
				try {
					bytes = BTin.read(buffer);					
					handler.obtainMessage(Board.MESSAGE_READ, bytes, -1, buffer).sendToTarget();				 
				} catch (IOException e) {
					Log.d("ERROR", "Problem reading input");
				}
			}
		}
	};*/
	
	public void destroy(){
		try {
			BTsocket.close();
		} catch (IOException e) {
			Log.d("ERROR", "Problem closing bluetooth socket");
		}
	}
	
}
