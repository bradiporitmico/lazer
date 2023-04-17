package it.netsnap.laser;

import java.util.ArrayList;
import java.util.Map;
import com.fazecast.jSerialComm.*;
import it.netsnap.laser.event.Event;
import it.netsnap.laser.event.Communicator.DeviceChange;
import it.netsnap.laser.event.Communicator.ResponseError;
import it.netsnap.laser.event.Communicator.ResponseNewLine;
import it.netsnap.laser.event.Communicator.ResponseTerminated;
import it.netsnap.laser.event.Communicator.StatusChange;


public class Communicator extends Thread{

	String device_path;
	SerialPort serial = null;
	String input_line = "";

	// private ArrayList<CommunicatorResponseListnerInterface> data_subscribers;
	// private ArrayList<CommunicatorStatusChangeInterface> changes_subscribers;
	// private ArrayList<Event> subscribers;


	private static Communicator instance = null;

	public static Communicator getInstance(){
		if (instance == null){
			instance = new Communicator();
		}
		return instance;
	}

	public Communicator(){
		// data_subscribers = new ArrayList<CommunicatorResponseListnerInterface>();
		// changes_subscribers = new ArrayList<CommunicatorStatusChangeInterface>();
		// subscribers = new ArrayList<Event>();
		start();
	}

	// public void addListner (Event callback){
	// 	subscribers.add(callback);
	// }

	public SerialPort setDevice (String dev){

		boolean actual_state = serial != null && serial.isOpen();
		close();
		// controlla se esiste la serial device
		for(SerialPort sp : SerialPort.getCommPorts()){
			if (sp.getSystemPortPath().equals(dev)){
				this.serial = sp;
				for (Event listner : Event.getListners ( DeviceChange.class )){
					((DeviceChange)listner).onEvent(dev);
				}

				break;
			}
		}
		this.device_path = dev;
		return this.serial;
	}
	
	public boolean open (){
		if (this.serial == null){
			return false;
		}
		this.serial.setBaudRate(115200);
		return this.serial.openPort();
	}
	
	public boolean close (){
		if (this.serial != null){
			return this.serial.closePort();
		}
		return false;
		
	}
	
	public boolean sendCommand (String cmd){
		return this.write (cmd + "\n");
		// if (!this.write (cmd + "\n")){
		// 	return false;
		// }
		
		// try {
		// 	String line;
		// 	boolean exit = false;
		// 	while (!exit){
		// 		line = this.getLine();
		// 		System.out.println ("-->" + line);
		// 		if (line.toLowerCase().equals("ok")){
		// 			return true;
		// 		}
		// 		if (line.toLowerCase().substring(0, 5).equals("error")){
		// 			return false;
		// 		}

		// 	}
		// 	return true;
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// 	return false;
		// 	// TODO: handle exception
		// }
	}

	public boolean write (String buffer){
		// System.out.println("> " + buffer);
		return serial.writeBytes(buffer.getBytes(), buffer.length()) == buffer.length();
	}

	public String getLine() throws SerialPortIOException, SerialPortTimeoutException{
		return this.getLine(10);
	}

	public String getLine(int timeout) throws SerialPortIOException, SerialPortTimeoutException{
		byte [] c = new byte[1];

		long time = System.currentTimeMillis();
		while (true){
			if (System.currentTimeMillis() - time > timeout * 1000){
				throw new SerialPortTimeoutException("Timeout reading line in ::getLine");
			}
			if (serial.bytesAvailable() > 0){
				if (serial.readBytes(c, 1) == 1){
					// System.out.print((char)c[0]+"["+c[0]+"]");
					switch (c[0]){
						case 10 : 
							String r = input_line;
							input_line = "";
							System.out.println("--------------->"+r);
							return r;
						case 13 : 
							break;
						default:
							input_line += (char)c[0];
					}
				} else {
					throw new SerialPortIOException("Unable to read from serial in ::getLine");
				}
			}
		}
	}

	@Override
	public void run(){
		try {
			while (true){
				// attende che la seriale si connetta
				System.out.println("Waiting for serial connection...");
				while ((serial == null) || (!serial.isOpen())){
					Thread.sleep(100);
				}
				System.out.println("Serial connected");
				for (Event listner : Event.getListners ( StatusChange.class )){
					((StatusChange)listner).onEvent(serial.isOpen());
				}

				byte [] c = new byte[1];
				String line = "";

				// loop di lettura dalla seriale
				while ( (serial != null) && serial.isOpen() ){
					if (serial.bytesAvailable() > 0){
						if (serial.readBytes(c, 1) == 1){
							// System.out.print((char)c[0]+"["+c[0]+"]");
							switch (c[0]){
								case 10 : 
									// System.out.println("received: " + line);

									if ((line.length() == 2) && line.toLowerCase().equals("ok")){
										for (Event listner : Event.getListners ( ResponseTerminated.class )){
											((ResponseTerminated)listner).onEvent();
										}
						
									} else
									if ((line.length() >= 5) && line.substring(0, 5).toLowerCase().equals("error")){
										for (Event listner : Event.getListners ( ResponseError.class )){
											((ResponseError)listner).onEvent(Integer.parseInt(line.substring(6)));
										}
									}  else {
										
										for (Event listner : Event.getListners ( ResponseNewLine.class )){
											((ResponseNewLine)listner).onEvent(line);
										}
	
									}
					
									line = "";
									break;
								case 13 : 
									break;
								default:
									line += (char)c[0];
							}
						} else {
							break; // esce dal loop di lettura e ritorna ad attendere la connessione della seriale
						}
					}
			
					// Thread.sleep(1);
					Thread.yield();
				}
				for (Event listner : Event.getListners ( StatusChange.class )){
					((StatusChange)listner).onEvent(serial.isOpen());
				}

			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public String[] getSerialDevices() {
		String [] res = new String [SerialPort.getCommPorts().length];
		int i = 0;
		for(SerialPort sp : SerialPort.getCommPorts()){
			res [i++] = sp.getSystemPortPath();
			// System.out.printf( "portname: %s, portdescription: %s, portlocation: %s, getSystemPortName:%s, getSystemPortPath:%s\n", sp.getDescriptivePortName(), sp.getPortDescription(), sp.getPortLocation(), sp.getSystemPortName(), sp.getSystemPortPath() );
			// System.out.println(sp.getSystemPortPath() );
		}

		return res;
	}

	
}
