package net.patchingzone.ru4real;

import net.patchingzone.ru4real.game.Player;

import org.json.JSONArray;

public interface NetworkListener {

	public void onMessageReceived(String event, JSONArray arguments); 
	public void onUpdatedLocation(String event, JSONArray arguments);
	public void onPlayerJoined(Player player);
	public void onUpdateLocation(Player player); 
	public void onTargetInRange(double latitude, double longitude, String value, float distance, int range);
	public void onTextMessage(String text);
	public void onPoke();
	public void onPlayerDisconnected(String event, JSONArray arguments);
	public void onListTrips(String event, JSONArray arguments);
	public void onListTargets(String event, JSONArray arguments);
	public void onPlayerInRange(String nickname, String sound, float distance);
	public void onRefresh();
	public void onPlayerScored(Player player, int targetIndex, int totalTargets);
	
}
