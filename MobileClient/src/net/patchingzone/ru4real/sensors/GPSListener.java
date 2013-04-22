package net.patchingzone.ru4real.sensors;

public interface GPSListener {

	public void onLocationChanged(double lat, double lon, double alt, float speed, float bearing);
	public void onSpeedChanged(float speed);
	public void onGPSSignalGood(); 
	public void onGPSSignalBad();
	public void onGPSStatus(boolean isGPSFix);
	
}
