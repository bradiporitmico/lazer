package it.netsnap.laser;
import javax.swing.UIManager;

import com.fazecast.jSerialComm.*;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import it.netsnap.laser.event.Event;
import it.netsnap.laser.event.Joystick.ChangedSlider;


/**
 * Hello world!
 *
 */
public class App 
{
	Communicator com;
	MainForm mainform;

	public App(){

		try {
			UIManager.setLookAndFeel( new FlatDarkLaf() );
		} catch( Exception ex ) {
			System.err.println( "Failed to initialize LaF" );
		}	

		com = Communicator.getInstance();
		Joystick.getInstance();
		DeviceInfo.getInstance();


		mainform = new MainForm();
	}




	public static void main( String[] args ) throws Exception
	{

		App app = new App();

		// String serial_device = "/dev/ttyUSB0";
		String serial_device = "";

		for (int i = 0; i < args.length; i++) {
			// System.out.printf("Argument %d: %s\n", i, args[i]);
			if (args[i].charAt(0) == '-'){
				switch (args[i].charAt(1)){
					case 'd':
						serial_device = args[++i];
						break;
				}
			}
		}

		System.out.println("Serial device = " + serial_device);

		if (app.com.setDevice(serial_device) == null){
			System.out.println( "Your should select a serial device from this ones:");
			for(SerialPort sp : SerialPort.getCommPorts()){
				// System.out.printf( "portname: %s, portdescription: %s, portlocation: %s, getSystemPortName:%s, getSystemPortPath:%s\n", sp.getDescriptivePortName(), sp.getPortDescription(), sp.getPortLocation(), sp.getSystemPortName(), sp.getSystemPortPath() );
				System.out.println(sp.getSystemPortPath() );
			}
			return;
		}

		if (!app.com.open()){
			throw new Exception ("Non posso aprire la device" + serial_device);
		}

		// app.com.write("$\n");
		// // app.com.write("$#\n");
		// // app.com.write("M03\n");
		// // app.com.write("M05\n"); // laser stop
		// int bytes;
		// String response;
		// while (!(response = app.com.getLine()).toLowerCase().equals("ok")){
		// 	System.out.printf("%s\n", response);
		// 	// if ((bytes = app.com.serial.bytesAvailable()) > 0){
		// 	// 	// System.out.println("C'è vita!");
		// 	// 	byte [] c = new byte [bytes];
		// 	// 	app.com.serial.readBytes(c, bytes);
		// 	// 	System.out.print(new String (c));

		// 	// }
	
		// }






		System.out.println( "Hello World!" );
	}

}
