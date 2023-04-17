package it.netsnap.laser.event.Communicator;

import it.netsnap.laser.event.Event;

public abstract class DeviceChange extends Event{
	public abstract void onEvent(String deviceName);
}

