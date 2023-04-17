package it.netsnap.laser.event.Joystick;

import it.netsnap.laser.event.Event;

public abstract class ChangedSlider extends Event{
	public abstract void onEvent(String slider, float value);
}
