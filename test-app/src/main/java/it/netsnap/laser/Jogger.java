package it.netsnap.laser;

import java.util.Locale;

public class Jogger extends Thread{
	private static Jogger instance = null;
	private float speed_x;
	private float speed_y;
	private float speed_z;

	public static Jogger getInstance(){
		if (instance == null){
			instance = new Jogger();
		}
		return instance;
	}

	public Jogger(){
		start();
	}
	
	/**
	 * s = v * dt - Computes distance traveled for next jog command.
	 * where:
	 * s - Incremental distance of jog command.
	 * dt - Estimated execution time of a single jog command in seconds.
	 * v - Current jog feed rate in mm/min. Less than or equal to max jog rate.
	 * N - Number of Grbl planner blocks (N=15)
	 * T = dt * N - Computes total estimated latency in seconds.
	 * 
	 * 
	 * 
	 * @param v Current jog feed rate in mm/min. Less than or equal to max jog rate.
	 * @return
	 */
	public float getTravelMm (float v){
		float dt = 1f;
		return (v / 60) * dt;
	}

	public void freeze (){
		speed_x = 0;
		speed_y = 0;
		speed_z = 0;
	}

	/**
	 * 
	 * @param speed La velocità di spostamento da 0 (fermo) a 1 (max speed), se negativo il movimento sarà verso lo zero (a sinistra per la x, in basso per la y)
	 */
	public void move (float speed_x, float speed_y, float speed_z){
		move_x (speed_x);
		move_y (speed_y);
		move_z (speed_z);
	}

	public void move_x (float speed){
		DeviceInfo di = DeviceInfo.getInstance();
		this.speed_x = speed * di.getParam (110);
		System.out.printf ("X Speed setted to : %.3f\n", this.speed_x);
	}

	public void move_y (float speed){
		DeviceInfo di = DeviceInfo.getInstance();
		this.speed_y= speed * di.getParam (111);
		System.out.printf ("Y Speed setted to : %.3f\n", this.speed_y);
	}

	public void move_z (float speed){
		DeviceInfo di = DeviceInfo.getInstance();
		this.speed_z = speed * di.getParam (112);
		System.out.printf ("Z Speed setted to : %.3f\n", this.speed_z);
	}

	@Override
	public void run(){
		try {
			boolean running = false;
			while (true){
				// long sleep = ;
				float travel_x = this.speed_x / 600f;
				float travel_y = this.speed_y / 600f;
				float travel_z = this.speed_z / 600f;
				// String cmd = String.format(Locale.US, "$J = G21 G91 X%.2f F%.3f", travel_x, Math.abs(this.speed));
				// String cmd = String.format(Locale.US, "$J = G21 G91 X%.2f F%.3f Y%.2f F%.3f Z%.2f F%.3f", travel_x, Math.abs(this.speed_x)+1, travel_y, Math.abs(this.speed_y)+10, travel_z, Math.abs(this.speed_z)+10);
				if (speed_x != 0){
					running = true;
					String cmd = String.format(Locale.US, "$J = G21 G91 X%.2f F%.3f ", travel_x, Math.abs(this.speed_x));
					System.out.printf ("Jog: '%s'\n", cmd);
					Communicator.getInstance().sendCommand(cmd);
				}
				if (speed_y != 0){
					String cmd = String.format(Locale.US, "$J = G21 G91 Y%.2f F%.3f ", travel_y, Math.abs(this.speed_y));
					System.out.printf ("Jog: '%s'\n", cmd);
					Communicator.getInstance().sendCommand(cmd);
				}
				if (speed_z != 0){
					running = true;
					String cmd = String.format(Locale.US, "$J = G21 G91 Z%.2f F%.3f ", travel_z, Math.abs(this.speed_z));
					System.out.printf ("Jog: '%s'\n", cmd);
					Communicator.getInstance().sendCommand(cmd);
				}
				if ((speed_x == 0) && (speed_y == 0) && (speed_z == 0) && running){
					running = true;
					String cmd = "$J = G21 G91 X0 Y0 Z0 F10";
					System.out.printf ("Jog: '%s'\n", cmd);
					Communicator.getInstance().sendCommand(cmd);
					running = false;
				}
				// if ((speed_x != 0) || (speed_y != 0) || (speed_z != 0)){
				// 	running = true;
				// 	System.out.printf ("Jog: '%s'\n", cmd);
				// 	Communicator.getInstance().sendCommand(cmd);
				// } else {
				// 	if (running){
				// 		cmd = "$J = G21 G91 X0 Y0 Z0 F10";
				// 		System.out.printf ("Jog: '%s'\n", cmd);
				// 		Communicator.getInstance().sendCommand(cmd);
				// 		running = false;
				// 	}
				// }
				Thread.sleep (100);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
		

}
