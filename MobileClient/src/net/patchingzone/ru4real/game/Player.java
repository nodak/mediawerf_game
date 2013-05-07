package net.patchingzone.ru4real.game;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class Player {
	//static
	public static int FINGER = 0;
	public static int PLAYER = 1;
	public static int OBSERVER = 2;
	
	//coming from deserialization
	public String id; 
	public String nickname;
	public String color;
	public LatLng latLng;
	public double lat;
	public double lng;
	public int score;
	boolean drone;
	
	public int type;
	public Marker marker;
	
	public void addMarker(Marker marker) {
		this.marker = marker;
	}
	
}
