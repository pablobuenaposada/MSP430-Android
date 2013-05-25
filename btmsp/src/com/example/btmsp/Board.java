package com.example.btmsp;

public class Board {
	
	public enum Mode{
    	SEND,SEND_READ;
	}
	
	private BluetoothComm communication = null;	

	public Board(String BTaddress) throws BluetoothDisabled, NoBluetoothSupported{		
		communication = new BluetoothComm(BTaddress);
		communicate(Board.Mode.SEND_READ,"N/"); //notify that we are a new connection
	}	
    
    public synchronized String communicate(Board.Mode mode, String toSend){    	
    	
    	if(mode == Board.Mode.SEND){
    		send(toSend);
    		return null;
    	}
    	else if(mode == Board.Mode.SEND_READ){    		
			try {
				return read(toSend);
			} catch (TimeoutException e) {				
				return "/";
			}			  		
    	}
    	return null;
    }
    
    private synchronized void send(String data){
    	communication.send(data);
    }
    
    private synchronized String read(String toSend) throws TimeoutException{
    	send(toSend);
    	return communication.read();
    }

	public void destroy() {
		communication.destroy();		
	}		
		
	public DigitalOutput createDigitalOutput(DigitalOutput.Pin pin){
		return new DigitalOutput(this,pin);		
	}
	
	public DigitalInput createDigitalInput(DigitalInput.Pin pin){
		return new DigitalInput(this,pin);
	}
	
	public AnalogInput createAnalogInput(AnalogInput.Pin pin){
		return new AnalogInput(this,pin);
	}
	
	public PWM createPWM(PWM.Pin pin, int period, int duty){
		return new PWM(this,pin,period,duty);
	}
	
	public OfflineTask createOfflineTask(OfflineTask.Pin pin, OfflineTask.Mode mode, OfflineTask.Units units, int period, int numSamples){
		return new OfflineTask(this,mode,pin,units,period,numSamples);
	}
	
	public SPI createSPI(){
		return new SPI(this);
	}
	
	public Serial createSerial(){
		return new Serial(this);
	}
}

