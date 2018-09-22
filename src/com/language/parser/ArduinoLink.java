package com.language.parser;
import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;

import jssc.SerialPortException;

public class ArduinoLink {

	
	PanamaHitek_Arduino arduino = new PanamaHitek_Arduino();
	
	
	
	ArduinoLink() throws InterruptedException{
		
		try {
			
			arduino.getSerialPorts();
			
			
			arduino.arduinoTX("/dev/ttyACM0", 9600);
			Thread.sleep(3000);
		} catch (ArduinoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Connection with arduino refused...");
		}
		
	}
	
	
	public void sendData(String message) throws InterruptedException
	{
		//System.out.println("sending with ArduinoLink");
		try {
			arduino.sendData(message);
			Thread.sleep(500);
		} catch (ArduinoException | SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
