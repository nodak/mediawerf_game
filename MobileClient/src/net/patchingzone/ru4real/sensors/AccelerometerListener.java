package net.patchingzone.ru4real.sensors;

public interface AccelerometerListener {

	public void onAccelerometerChanged(float x, float y, float z); 
	public void onShake(float force); 
	
}
