package it.netsnap.laser;

import java.util.StringTokenizer;

import it.netsnap.laser.event.Event;
import it.netsnap.laser.event.Communicator.ResponseNewLine;
import it.netsnap.laser.event.Communicator.ResponseTerminated;
import it.netsnap.laser.event.Communicator.StatusChange;


enum Status {
	IDLE,
	INFO1,
	INFO2,
}


/**
 * 110 = X Max rate, mm/min
 * 111 = Y Max rate, mm/min
 * 112 = Z Max rate, mm/min
 */

public class DeviceInfo {
	private static DeviceInfo instance;
	private String model;
	private String grbl_version;
	private Status state;
	private float[] params;
	
	public static DeviceInfo getInstance(){
		if (instance == null){
			instance = new DeviceInfo();
		}
		return instance;
	}

	public DeviceInfo(){
		params = new float[255];
		Event.addListner(new StatusChange(){

			@Override
			public void onEvent(boolean isOpen) {
				if (isOpen){
					state = Status.IDLE;
					Communicator.getInstance().sendCommand("$I");
				}
			}
		});

		Event.addListner(new ResponseNewLine(){
			@Override
			public void onEvent(String line) {
				switch (state) {
					case IDLE:
						StringTokenizer tokenizer = new StringTokenizer(line.substring(1, line.length()-1), ":");
						while (tokenizer.hasMoreTokens()) {
							String key = tokenizer.nextToken();
							String value = tokenizer.nextToken();
							System.out.printf ("key: %s, value: %s\n", key, value);
							switch (key) {
								case "VER":
									grbl_version = value;
									break;
								case "MACHINE":
									model = value;
									break;
										
							}
						}
						break;
					case INFO1:
						tokenizer = new StringTokenizer(line, "=");
						while (tokenizer.hasMoreTokens()) {
							String key = tokenizer.nextToken();
							String value = tokenizer.nextToken();
							params [Integer.parseInt(key.substring(1))] = Float.parseFloat(value);
							System.out.printf ("key: %s, value: %s\n", key, value);
						}
						break;
					// case :
					// 	state = Status.INFO1;
					// 	Communicator.getInstance().sendCommand("$i");
					// 	break;
					// case INFO2:
					// 	state = Status.INFO1;
					// 	Communicator.getInstance().sendCommand("$i");
					// 	break;
				}
			}
		});

		Event.addListner(new ResponseTerminated(){
			@Override
			public void onEvent() {
				switch (state) {
					case IDLE:
						state = Status.INFO1;
						Communicator.getInstance().sendCommand("$$");
						break;
					case INFO1:
						state = Status.INFO2;
						break;
				}
				// StringTokenizer tokenizer = new StringTokenizer(str, ":");
			}
		});

	}

	public String getModel(){
		return this.model;
	}

	public String getGrbl(){
		return this.grbl_version;
	}

	public float getParam (int index){
		return params[index];
	}


	
}
