package it.netsnap.laser.event.Communicator;

import it.netsnap.laser.event.Event;

public abstract class ResponseError  extends Event {
	public abstract void onEvent(int code);
}

