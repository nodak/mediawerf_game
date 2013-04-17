package net.patchingzone.ru4real.sensors;

public interface GPSListener {

	public void onLocationChanged(double lat, double lon, double alt);
	public void onSpeedChanged(float speed);
	public void onGPSSignalGood(); 
	public void onGPSSignalBad();
}
