package it.netsnap.laser.event;

import java.util.ArrayList;

public abstract class Event {
	// void onEvent();
	private static ArrayList<Event> listners = new ArrayList<Event> ();

	public static void fire (Class<?> clazz, Object... args){

	}

	public static  ArrayList<Event> getListners(Class<?> clazz){
		ArrayList<Event> res = new ArrayList<Event>();
		for (Event listner:listners){
			
			if (clazz.isInstance(listner)){
				res.add(listner);
			}
		}
		return res;
	}


	public static void addListner (Event listner){
		listners.add(listner);
	}

	public static void removeListner (Event listner){
		listners.remove(listner);
	}


}
