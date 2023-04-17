package it.netsnap.laser;

import it.netsnap.laser.event.Joystick.ChangedSlider;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;
import net.java.games.input.Component.Identifier;


public class Joystick extends Thread{
	private static Joystick instance = null;
	private Controller controller = null;
	private float x;
	private float y;
	private float z;

	public static Joystick getInstance(){
		if (instance == null){
			instance = new Joystick();
		}
		return instance;
	}


  public Joystick() {
    
    
		// Otteniamo l'ambiente dei controller
		ControllerEnvironment env = ControllerEnvironment.getDefaultEnvironment();

		// Otteniamo tutti i controller (giostick, tastiere, ecc.)
		Controller[] controllers = null;
		controllers = env.getControllers();

		// Scegliamo il joystick
		controller = null;
		for(Controller c : controllers) {
			// System.out.println("Contyroller type : " + c.getType());
			if(c.getType() == Controller.Type.STICK) {
				System.out.println("\n\nController: " + c.getName() + "\n---------------");
				for (Component comp:c.getComponents()){
					System.out.printf("%s [%s] - %s\n", comp.getName(), comp.isAnalog() ? "A" : "D", comp.getIdentifier().getName());
					
				}
		
				controller = c;
				// break;
			}
		}
		
		if(controller == null) {
			System.out.println("Joystick not found!");
			return;
		}

		start();
	}

	// private void setX (float value){
	// 	this.x = value;
	// 	for (it.netsnap.laser.event.Event listner : it.netsnap.laser.event.Event.getListners ( ChangeX.class )){
	// 		((ChangeX)listner).onEvent(value);
	// 	}
	// }
	
	// private void setY (float value){
	// 	this.y = value;
	// 	for (it.netsnap.laser.event.Event listner : it.netsnap.laser.event.Event.getListners ( ChangeY.class )){
	// 		((ChangeY)listner).onEvent(value);
	// 	}
	// }
	
	// private void setZ (float value){
	// 	this.z = value;
	// 	for (it.netsnap.laser.event.Event listner : it.netsnap.laser.event.Event.getListners ( ChangeZ.class )){
	// 		((ChangeZ)listner).onEvent(value);
	// 	}
	// }
	
	@Override
	public void run(){
		
		// Creiamo una coda eventi per l'input
		EventQueue eventQueue = controller.getEventQueue();
		Event event = new Event();

		Component comp_x = null;
		Component comp_y = null;
		Component comp_z = null;

		// Leggiamo gli input del joystick
		while(true) {
			controller.poll();
			
			while(eventQueue.getNextEvent(event)) {
				Component comp = event.getComponent();
				
				// System.out.println("[?7h[255D[40m");
				// System.out.println("[0;1;33m[22C[32m┌──┐┌─────┐ ┌─────┐ ┌──────┬──┐");
				// System.out.println("[22C│[37m██[32m├┘[37m█████[32m└┬┘[37m█████[32m└┬┘[37m██████[32m│[37m▐▌[32m│");
				// System.out.println("[17C┌──┐ │[37m██[32m│[37m██▄▄▄██[32m│[37m██[32m┌─┐[37m██[32m│[37m██▄▄▄▄ [32m│[37m▄▄[32m│");
				// System.out.println("[17C│[37m▒▒[32m└─┘[37m▒█[32m│[37m▒█[32m┌─┐[37m▒█[32m│[37m▒█[32m│ │[37m▒█[32m│ [37m▀▀▀▀▒█[32m│[37m▒█[32m│");
				// System.out.println("[17C└┐[37m▓▓▓▓▓[32m┌┤[37m▓▓[32m│ │[37m▓▓[32m│[37m▓▓[32m│ │[37m▓▓[32m│[37m▀▓▓▓▓▓▀[32m│[37m▓▓[32m│");
				// System.out.println("[18C└─────┘└──┘ └──┴──┘ └──┴───────┴──┘");
				// System.out.println("[0m");
				// System.out.println(comp);
				String compname = comp.getName();
				float value = event.getValue();
				// System.out.printf("'%s' = %f", compname, value);

				// if (value==0){
				// 	for (it.netsnap.laser.event.Event listner : it.netsnap.laser.event.Event.getListners ( ButtonPressed.class )){
				// 		((ButtonPressed)listner).onEvent(compname);
				// 	}

				// }


				// if (comp == comp_x){
				// 	System.out.printf("È il comp_x (%.3f)!!\n", value);
				// 	// this.setX (event.getValue());
				// }else
				// if (comp == comp_y){
				// 	System.out.printf("È il comp_y (%.3f)!!\n", value);
				// 	// this.setY (event.getValue());
				// }else
				// if (comp == comp_z){
				// 	System.out.printf("È il comp_z (%.3f)!!\n", value);
				// 	// this.setZ (event.getValue());
				// }else
				// if (compname.equals("slider")){
				// 	comp_x = comp;
				// 	// this.setX (event.getValue());
				// }else

				if (compname.equals("x")){
					for (it.netsnap.laser.event.Event listner : it.netsnap.laser.event.Event.getListners ( ChangedSlider.class )){
						((ChangedSlider)listner).onEvent("jogx", value);
					}

					// comp_x = comp;
					// this.setX (event.getValue());
				}else
				if (compname.equals("y")){
					for (it.netsnap.laser.event.Event listner : it.netsnap.laser.event.Event.getListners ( ChangedSlider.class )){
						((ChangedSlider)listner).onEvent("jogy", value);
					}
					// comp_y = comp;
					// this.setY (event.getValue());
				}else
				// if (compname.equals("z")){
				// 	comp_z = comp;
				// 	// this.setZ (event.getValue());
				// }else{
				// 	// for (it.netsnap.laser.event.Event listner : it.netsnap.laser.event.Event.getListners ( ButtonPressed.class )){
				// 	// 	((ButtonPressed)listner).onEvent(compname);
				// 	// }
			
				// }


				;
				// // System.out.println(comp.getIdentifier());
				// Identifier id = comp.getIdentifier();
				// // Otteniamo il valore dell'asse X e Y del joystick
				// if(comp.isRelative()) {
				// 	float value = event.getValue();
				// 	if(comp.getName().equals("X Axis")) {
				// 		System.out.println("X = " + value);
				// 	}
				// 	else if(comp.getName().equals("Y Axis")) {
				// 		System.out.println("Y = " + value);
				// 	}
				// }
			}
			
			// Aspettiamo un po' di tempo prima di leggere di nuovo l'input
			try {
				Thread.sleep(1);	
				// Thread.yield();
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}			



    

  }
}