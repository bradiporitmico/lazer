package it.netsnap.laser;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;

import com.fazecast.jSerialComm.SerialPort;

import info.clearthought.layout.*;
import it.netsnap.laser.event.Communicator.DeviceChange;
import it.netsnap.laser.event.Communicator.ResponseError;
import it.netsnap.laser.event.Communicator.StatusChange;
import it.netsnap.laser.event.Event;



/**
 * @author doc
 */
public class MainForm extends JFrame {
	private ResourceBundle bundle;

	public MainForm() {
		initComponents();
		setVisible(rootPaneCheckingEnabled);
		bundle = ResourceBundle.getBundle("i18n.strings");

		

		enumDevices();
		// Communicator.getInstance().addListner(new StatusChange(){
		Event.addListner(new StatusChange(){

			@Override
			public void onEvent(boolean isOpen) {
				ledSerial.setColor(isOpen ? Color.GREEN : Color.GRAY);
				menuConnect.setEnabled(!isOpen);
				menuDisconnect.setEnabled(isOpen);
				// System.out.printf ("MainForm::StatusChangeEvent (%s, %s)\n", device_name, isConnected ? "true":"false");
			}


		});

		// Communicator.getInstance().addListner(new DeviceChange(){
		Event.addListner(new DeviceChange(){

			@Override
			public void onEvent(String deviceName) {
				lblSerial.setText(deviceName);
			}

		});

		// Communicator.getInstance().addListner(new CommunicatorResponseErrorInterface(){
		Event.addListner(new ResponseError(){
			@Override
			public void onEvent(int code) {
				System.out.printf ("MainForm::Error (%d)\n", code);
				lblStatus.setText("Error: " + code); 
			}
		});


		JPanel [] panels = {
			CommanderPanel.getInstance(),
			ControllerPanel.getInstance(),
		};
		for(JPanel panel:panels){ 
			tabs.addTab(panel.getName(), panel);
		}

	}

	public void enumDevices() {
		menuDevices.removeAll();
		JMenuItem menu = new JMenuItem();
		menu.setText(bundle.getString("menuRefreshDevices.text"));
		menu.addActionListener(e -> menuRefreshDevicesClick(e));
		menuDevices.add(menu);
		menuDevices.addSeparator();

		// crea le voci di menu che rappresentano le device seriali presenti sul sistema
		for (String device:Communicator.getInstance().getSerialDevices()){
			menu = new JMenuItem();
			menu.setText(device);
			// menuConnect.setEnabled(true);
			menu.addActionListener(e -> menuSelectSerialClick(e));
			menuDevices.add(menu);
		}
	}



	private void menuSelectSerialClick(ActionEvent e) {
		System.out.println(e.getActionCommand());
		Communicator.getInstance().setDevice (e.getActionCommand());
		menuConnectClick (null);
	}

	private void menuDisconnectClick(ActionEvent e) {
		Communicator.getInstance().close();
	}

	private void menuConnectClick(ActionEvent e) {
		Communicator.getInstance().open();
	}

	private void lblSerialMouseClicked(MouseEvent e) {
		menuSelectSerial.show(lblSerial, e.getX(), e.getY());
	}

	private void menuRefreshDevicesClick(ActionEvent e) {
		enumDevices();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner Evaluation license - Paolo
		ResourceBundle bundle = ResourceBundle.getBundle("i18n.strings");
		menuBar1 = new JMenuBar();
		menu1 = new JMenu();
		menuItem1 = new JMenuItem();
		infoBar = new JToolBar();
		ledSerial = new JCircle();
		lblSerial = new JLabel();
		lblStatus = new JLabel();
		tabs = new JTabbedPane();
		menuSelectSerial = new JPopupMenu();
		menuDisconnect = new JMenuItem();
		menuConnect = new JMenuItem();
		menuDevices = new JMenu();
		menuRefreshDevices = new JMenuItem();

		//======== this ========
		setMinimumSize(new Dimension(400, 400));
		setPreferredSize(new Dimension(1024, 768));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Provetta cispella");
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== menuBar1 ========
		{

			//======== menu1 ========
			{
				menu1.setText("File");

				//---- menuItem1 ----
				menuItem1.setText("Open");
				menu1.add(menuItem1);
			}
			menuBar1.add(menu1);
		}
		setJMenuBar(menuBar1);

		//======== infoBar ========
		{

			//---- ledSerial ----
			ledSerial.setMaximumSize(new Dimension(20, 20));
			ledSerial.setColor(Color.lightGray);
			ledSerial.setRadius(13);
			ledSerial.setMinimumSize(new Dimension(20, 20));
			infoBar.add(ledSerial);

			//---- lblSerial ----
			lblSerial.setText(bundle.getString("lblSerial.text"));
			lblSerial.setIcon(null);
			lblSerial.setComponentPopupMenu(null);
			lblSerial.setBorder(new EmptyBorder(2, 10, 2, 10));
			lblSerial.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					lblSerialMouseClicked(e);
				}
			});
			infoBar.add(lblSerial);

			//---- lblStatus ----
			lblStatus.setText("text");
			lblStatus.setBorder(new EmptyBorder(2, 10, 2, 10));
			infoBar.add(lblStatus);
		}
		contentPane.add(infoBar, BorderLayout.SOUTH);

		//======== tabs ========
		{
			tabs.setBorder(new BevelBorder(BevelBorder.LOWERED));
		}
		contentPane.add(tabs, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(null);

		//======== menuSelectSerial ========
		{

			//---- menuDisconnect ----
			menuDisconnect.setText(bundle.getString("menuDisconnect.text"));
			menuDisconnect.setEnabled(false);
			menuDisconnect.addActionListener(e -> menuDisconnectClick(e));
			menuSelectSerial.add(menuDisconnect);

			//---- menuConnect ----
			menuConnect.setText(bundle.getString("menuConnect.text"));
			menuConnect.setEnabled(false);
			menuConnect.addActionListener(e -> menuConnectClick(e));
			menuSelectSerial.add(menuConnect);
			menuSelectSerial.addSeparator();

			//======== menuDevices ========
			{
				menuDevices.setText(bundle.getString("menuDevices.text"));

				//---- menuRefreshDevices ----
				menuRefreshDevices.setText(bundle.getString("menuRefreshDevices.text"));
				menuRefreshDevices.addActionListener(e -> menuRefreshDevicesClick(e));
				menuDevices.add(menuRefreshDevices);
				menuDevices.addSeparator();
			}
			menuSelectSerial.add(menuDevices);
		}
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner Evaluation license - Paolo
	private JMenuBar menuBar1;
	private JMenu menu1;
	private JMenuItem menuItem1;
	private JToolBar infoBar;
	private JCircle ledSerial;
	private JLabel lblSerial;
	private JLabel lblStatus;
	private JTabbedPane tabs;
	private JPopupMenu menuSelectSerial;
	private JMenuItem menuDisconnect;
	private JMenuItem menuConnect;
	private JMenu menuDevices;
	private JMenuItem menuRefreshDevices;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
