package net.patchingzone.ru4real.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class AccelerometerManager {

	private final static String TAG = "Accel";

	/** indicates whether or not Accelerometer Sensor is supported */
	private static Boolean supported;
	/** indicates whether or not Accelerometer Sensor is running */
	private static boolean running = false;

	private AccelerometerListener listener = null; 

	private float currentValueX;
	private float previousValueX;
	private float currentValueY;
	private float previousValueY;
	private float currentValueZ;
	private float previousValueZ;

	boolean accelSupported;

	SensorManager sensormanager;
	SensorEventListener accelListener;

	Sensor accelerometer;

	public AccelerometerManager(Context c) { 

		// register
		sensormanager = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
		accelerometer = sensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		accelListener = new SensorEventListener() {

			public void onSensorChanged(SensorEvent event) {

				switch (event.sensor.getType()) {
				case Sensor.TYPE_ACCELEROMETER:

					listener.onAccelerometerChanged(event.values[0], event.values[1],
							event.values[2]);

					break;


				default:
					break;
				}

			}


			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				switch (accuracy) {
				case SensorManager.SENSOR_STATUS_UNRELIABLE:
					// accuracyLabel.setText(R.string.accuracy_unreliable);
					break;
				case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
					// accuracyLabel.setText(R.string.accuracy_low);
					break;
				case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
					// accuracyLabel.setText(R.string.accuracy_medium);
					break;
				case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
					// accuracyLabel.setText(R.string.accuracy_high);
					break;
				}
			}

		};

	}

	public float total() {

		return currentValueX + currentValueY + currentValueZ;
	}

	public float totalDiff() {

		return Math.abs((currentValueX + currentValueY + currentValueZ)
				- (currentValueX + currentValueY + currentValueZ));
	}

	public float diffX() {
		return Math.abs(currentValueX - previousValueX);
	}

	public float diffY() {
		return Math.abs(currentValueY - previousValueY);
	}

	public float diffZ() {
		return Math.abs(currentValueZ - previousValueZ);
	} 
	
    public void setListener(Object listener) { 
    	this.listener = (AccelerometerListener) listener; 
    	
    }


	public boolean isListening() {
		// TODO Auto-generated method stub
		return false;
	}

	public void startListening() {
		running = true;
		accelSupported = sensormanager.registerListener(accelListener, accelerometer,
				SensorManager.SENSOR_DELAY_GAME); 
	}

	public void stopListening() {
		running = false;
		sensormanager.unregisterListener(accelListener);
	}

}
