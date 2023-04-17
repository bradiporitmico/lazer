package it.netsnap.laser;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import it.netsnap.laser.event.Event;
import it.netsnap.laser.event.Joystick.ChangedSlider;



/**
 * @author doc
 */
public class ControllerPanel extends JPanel {
	private static ControllerPanel instance = null;

	public static ControllerPanel getInstance(){
		if (instance == null){
			instance = new ControllerPanel();
		}
		return instance;
	}

	public ControllerPanel() {
		initComponents();
		Event.addListner(new ChangedSlider(){
			@Override
			public void onEvent(String slider, float value) {
				Jogger jogger = Jogger.getInstance();
				System.out.printf("Slider '%s' changed to %.3f\n", slider, value);
				if (slider.equals("jogx")){
					jogger.move_x (value);
				}else
				if (slider.equals("jogy")){
					jogger.move_y (value);
				}else
				;
			}
		});


	}




	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner Evaluation license - Paolo
		ResourceBundle bundle = ResourceBundle.getBundle("i18n.strings");

		//======== this ========
		setName(bundle.getString("controllerpanel.name"));
		setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border
		. EmptyBorder( 0, 0, 0, 0) , "JFor\u006dDesi\u0067ner \u0045valu\u0061tion", javax. swing. border. TitledBorder. CENTER, javax
		. swing. border. TitledBorder. BOTTOM, new java .awt .Font ("Dia\u006cog" ,java .awt .Font .BOLD ,
		12 ), java. awt. Color. red) , getBorder( )) );  addPropertyChangeListener (new java. beans
		. PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("bord\u0065r" .equals (e .
		getPropertyName () )) throw new RuntimeException( ); }} );
		setLayout(new BorderLayout());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner Evaluation license - Paolo
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
