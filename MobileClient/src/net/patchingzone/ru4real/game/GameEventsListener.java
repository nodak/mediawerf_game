package net.patchingzone.ru4real.game;

public interface GameEventsListener {

	public void onTargetInRange(int id); 
	public void onPlayerInRange(int id); 
	public void onNewArea(int id); 
	public void onPlayerJoined();
	public void onPlayerLeft();
	public void onListPlayers(String listPlayers);
	public void onPlaySound(String fileName);
	
}
