package it.netsnap.laser.event.Communicator;

import it.netsnap.laser.event.*;


public abstract class ResponseNewLine  extends Event {
	public abstract void onEvent(String line);
}

