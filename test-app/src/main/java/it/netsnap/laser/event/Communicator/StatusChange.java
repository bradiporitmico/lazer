package it.netsnap.laser.event.Communicator;

import it.netsnap.laser.event.Event;

public abstract class StatusChange extends Event{
	public abstract void onEvent(boolean isOpened);
}

