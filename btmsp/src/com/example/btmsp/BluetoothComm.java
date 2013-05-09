package com.example.btmsp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BluetoothComm{
	
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");   
	private InputStream BTin = null;
    private OutputStream BTout = null;   
    private BluetoothSocket BTsocket = null;
	
	public BluetoothComm(Board board,String address){		
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
			Log.e("ERROR", "Problem establishing connection");
		}        
	} 
	
	public synchronized void send(String data){		
    	try {    		 
			BTout.write(data.getBytes());
		} catch (IOException e) {
			Log.e("ERROR", "Problem sending output");
		}		
	}	
	
	public synchronized String read() throws TimeoutException{
		byte[] buffer = new byte[10];
		int bytesRead;
		String message="";
		long timeoutTime = System.currentTimeMillis()+1000;
		
		while (!message.contains("/")){			
			try {
				if (BTin.available() > 0){
					bytesRead = BTin.read(buffer);
					message = message + new String(buffer).substring(0, bytesRead);
				}
				else{
					if (System.currentTimeMillis() > timeoutTime){
						Log.e("ERROR", "Timeout reading input");
						throw new TimeoutException();
					}				
				}
			} catch (IOException e) {				
				Log.e("ERROR", "Problem reading input");
				throw new TimeoutException();
			}
		}		
		return message;
	}
	
	public void destroy(){
		try {
			BTsocket.close();
		} catch (IOException e) {
			Log.e("ERROR", "Problem closing bluetooth socket");
		}
	}
	
}

class TimeoutException extends Exception{
	public TimeoutException(){
		
	}
}

