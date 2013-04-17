package net.patchingzone.ru4real.sensors;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

public class GPSManager {
	protected static final String TAG = "GPSManager";
	LocationManager gps;
	LocationManager locationManager;
	LocationListener listener;
	private Context c;

	public GPSManager(Context c) {
		this.c = c;
	}
	
	// gps
	public void startGPS() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		gps = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
		String provider;
		locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
		provider = locationManager.getBestProvider(criteria, false);

		listener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {

			}

			@Override
			public void onProviderEnabled(String provider) {

			}

			@Override
			public void onProviderDisabled(String provider) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				c.startActivity(intent);
			}

			@Override
			public void onLocationChanged(Location location) {
				Log.d(TAG, "onLocationChanged");
				Date df = new java.util.Date(location.getTime());

				JSONObject gpsData = new JSONObject();
				JSONArray arguments = new JSONArray();

				String vv = new SimpleDateFormat("dd-MM-yyyy , HH:mm:ss").format(df);

				try {
					gpsData.put("lat", location.getLatitude());
					gpsData.put("Long", location.getLongitude());
					gpsData.put("Alt", location.getAltitude());
					gpsData.put("Acc", location.getAccuracy());
					gpsData.put("Time", vv);
					gpsData.put("TimeStamp", location.getTime());
					gpsData.put("Head", location.getBearing());
					arguments.put(gpsData);

					Log.d(TAG, arguments.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		locationManager.requestLocationUpdates(provider, 100, 1, listener);
	} 


	public void stopGPS() {
		locationManager.removeUpdates(listener);
	} 
	
	
	public void startCompass() {
		
	} 
	
	public void stopCompass() {
		
	}
}
