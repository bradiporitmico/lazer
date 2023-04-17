package it.netsnap.laser.event.Joystick;

import it.netsnap.laser.event.Event;

public abstract class ChangeZ extends Event{
	public abstract void onEvent(float value);
}
