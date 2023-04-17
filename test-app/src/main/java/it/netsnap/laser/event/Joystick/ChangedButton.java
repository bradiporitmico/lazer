package it.netsnap.laser.event.Joystick;

import it.netsnap.laser.event.Event;

public abstract class ChangedButton extends Event{
	public abstract void onEvent(int index, boolean pressed);
}
