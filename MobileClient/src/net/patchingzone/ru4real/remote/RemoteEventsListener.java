package net.patchingzone.ru4real.remote;

public interface RemoteEventsListener {

	public void onAudioVolume(int volume); 
	public void onReconnect(); 
	public void onInfo(); 

	
}
