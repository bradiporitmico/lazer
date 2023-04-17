package it.netsnap.laser;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.ArrayList;

import javax.swing.*;
/*
 * Created by JFormDesigner on Wed Apr 12 17:53:37 CEST 2023
 */

import it.netsnap.laser.event.Event;
import it.netsnap.laser.event.Communicator.ResponseError;
import it.netsnap.laser.event.Communicator.ResponseNewLine;
import it.netsnap.laser.event.Communicator.ResponseTerminated;



/**
 * @author doc
 */
public class CommanderPanel extends JPanel {
	private static CommanderPanel instance = null;

	private DefaultListModel<String> log;
	private ArrayList<String> commands_list;
	private int commands_index;
	private Thread reader;
	private boolean show_response;


	public static CommanderPanel getInstance(){
		if (instance == null){
			instance = new CommanderPanel();
		}
		return instance;
	}

	public CommanderPanel() {
		initComponents();

		
		listLog.setModel(new DefaultListModel<String>());
		log = (DefaultListModel)listLog.getModel();
		commands_list = new ArrayList<String>();

		// Communicator.getInstance().addListner(new ResponseError(){
		Event.addListner(new ResponseError(){
			@Override
			public void onEvent(int code) {
				if (show_response){
					log.addElement("Error: " + code);
				}
				show_response = false;
			}
		});

		// Communicator.getInstance().addListner(new ResponseNewLine(){
		Event.addListner(new ResponseNewLine(){
			@Override
			public void onEvent(String line) {
				if (show_response){
					log.addElement(line);
					listLog.ensureIndexIsVisible(log.size() - 1);
				}
			}
		});

		// Communicator.getInstance().addListner(new ResponseTerminated(){
		Event.addListner(new ResponseTerminated(){
			@Override
			public void onEvent() {

				if (show_response){
					log.addElement("OK");
					listLog.ensureIndexIsVisible(log.size() - 1);
				}
				show_response = false;

			}
		});

	}

	// @Override
	// public String getName (){
	// 	return "Commands";
	// 	return bundle.getString("btnSend.text");
	// }

	private void btnClearLog(ActionEvent e) {
		log.clear();
	}

	private void txtCmdKeyPressed(KeyEvent e) {
		switch (e.getKeyCode()){
			case 10:
				btnSend(new ActionEvent(e.getSource(), e.getID(), ""));
				break;
						
			case 38: // up arrrow 
				if (commands_list.size() > 0){
					txtCmd.setText(commands_list.get(commands_index));
					if (commands_index > 0){
						commands_index--;
					}
				}
				break;
			case 40: // down arrrow 
				if (commands_index <= commands_list.size() - 1 ){
					if (commands_index < commands_list.size() - 1 ){
						commands_index++;
						txtCmd.setText(commands_list.get(commands_index));
					}else{
						txtCmd.setText("");
					}
				}
				break;
			case 27: // esc
				txtCmd.setText("");
				commands_index = commands_list.size() - 1;
				break;
		}
	}

	private void btnSend(ActionEvent e) {
		// Communicator com = Communicator.getInstance();
		Communicator com = Communicator.getInstance();
		String cmd = txtCmd.getText();
		show_response = true;
		if (!com.sendCommand(cmd)){
			return;
		}
		log.addElement(">> " + cmd);
		commands_list.add(cmd);
		commands_index = commands_list.size() - 1;
		txtCmd.setText("");
	
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner Evaluation license - Paolo
		ResourceBundle bundle = ResourceBundle.getBundle("i18n.strings");
		toolBar = new JToolBar();
		btnClearLog = new JButton();
		panel4 = new JPanel();
		txtCmd = new JTextField();
		btnSend = new JButton();
		scrollPane2 = new JScrollPane();
		listLog = new JList();

		//======== this ========
		setNextFocusableComponent(txtCmd);
		setName(bundle.getString("commanderpanel.name"));
		setRequestFocusEnabled(false);
		setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder(
		0, 0, 0, 0) , "JF\u006frmDesi\u0067ner Ev\u0061luatio\u006e", javax. swing. border. TitledBorder. CENTER, javax. swing. border. TitledBorder
		. BOTTOM, new java .awt .Font ("Dialo\u0067" ,java .awt .Font .BOLD ,12 ), java. awt. Color.
		red) , getBorder( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .
		beans .PropertyChangeEvent e) {if ("borde\u0072" .equals (e .getPropertyName () )) throw new RuntimeException( ); }} );
		setLayout(new BorderLayout());

		//======== toolBar ========
		{

			//---- btnClearLog ----
			btnClearLog.setText(bundle.getString("btnClearLog.text"));
			btnClearLog.addActionListener(e -> btnClearLog(e));
			toolBar.add(btnClearLog);
		}
		add(toolBar, BorderLayout.NORTH);

		//======== panel4 ========
		{
			panel4.setLayout(new BoxLayout(panel4, BoxLayout.X_AXIS));

			//---- txtCmd ----
			txtCmd.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
			txtCmd.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					txtCmdKeyPressed(e);
				}
			});
			panel4.add(txtCmd);

			//---- btnSend ----
			btnSend.setText(bundle.getString("btnSend.text"));
			btnSend.addActionListener(e -> btnSend(e));
			panel4.add(btnSend);
		}
		add(panel4, BorderLayout.SOUTH);

		//======== scrollPane2 ========
		{
			scrollPane2.setViewportView(listLog);
		}
		add(scrollPane2, BorderLayout.CENTER);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner Evaluation license - Paolo
	private JToolBar toolBar;
	private JButton btnClearLog;
	private JPanel panel4;
	private JTextField txtCmd;
	private JButton btnSend;
	private JScrollPane scrollPane2;
	private JList listLog;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

