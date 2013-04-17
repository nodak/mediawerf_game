package net.patchingzone.ru4real.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class OrientationManager {

	SensorManager sm;
	SensorEventListener orientation;

	public OrientationManager() {

	}

	public void start() {

		orientation = new SensorEventListener() {

			@Override
			public void onSensorChanged(SensorEvent event) {

				Log.d("qq", event.values[0] + " " + event.values[1] + " " + event.values[2]);

				// listener.onOrientationChange(event.values[2],
				// event.values[1], event.values[0]);

			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {

			}
		};
	} 
	
	public void stop() {
		
	} 
	
	
	public void addListener() {
		
	
	} 
	
	public void removeListener() {
		
		
	} 
	
}
