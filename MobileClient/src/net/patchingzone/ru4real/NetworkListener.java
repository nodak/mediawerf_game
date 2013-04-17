package net.patchingzone.ru4real;

import org.json.JSONArray;

public interface NetworkListener {

	public void onMessageReceived(String event, JSONArray arguments); 
	
}
