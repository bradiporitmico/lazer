package it.netsnap.laser.event.Joystick;

import it.netsnap.laser.event.Event;

public abstract class ChangeY extends Event{
	public abstract void onEvent(float value);
}
