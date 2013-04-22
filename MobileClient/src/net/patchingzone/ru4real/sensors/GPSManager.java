package net.patchingzone.ru4real.sensors;

import java.util.Vector;

import net.patchingzone.ru4real.L;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;

public class GPSManager extends CustomSensorManager {
	protected static final String TAG = "GPSManager";
	LocationManager gps;
	LocationManager locationManager;
	LocationListener listener;
	private Context c;
	Vector<GPSListener> listeners;
	boolean connected = false;
	private boolean isGPSFix;
	private Location mLastLocation;
	private long mLastLocationMillis;

	public GPSManager(Context c) {
		this.c = c;
		listeners = new Vector<GPSListener>();
	}

	// gps
	public void start() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setBearingAccuracy(Criteria.ACCURACY_FINE);
		// criteria.setSpeedRequired(true);
		gps = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
		String provider;
		locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
		provider = locationManager.getBestProvider(criteria, false);

		listener = new LocationListener() {

		

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				L.d(TAG, "the gps status is: " + status);

				//TODO add a listener to see when the GPS is on or not
				switch (status) {
				case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
					if (mLastLocation != null)
						isGPSFix = (SystemClock.elapsedRealtime() - mLastLocationMillis) < 3000;

					if (isGPSFix) { // A fix has been acquired.
						// Do something.
					} else { // The fix has been lost.
						// Do something.
					}
					
					for (GPSListener l : listeners) {
						l.onGPSStatus(isGPSFix);
					}


					break;
				case GpsStatus.GPS_EVENT_FIRST_FIX:
					// Do something.
					isGPSFix = true;

					break;
				}
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
				// Log.d(TAG, "onLocationChanged");

				// Toast.makeText(c, "hola" + location.getLatitude() + " " +
				// location.getLongitude(), Toast.LENGTH_LONG).show();
				// Log.d(TAG, "" + location.getLatitude() + " " +
				// location.getLongitude());

				for (GPSListener l : listeners) {
					l.onLocationChanged(location.getLatitude(), location.getLongitude(), location.getAltitude(),
							location.getSpeed(), location.getAccuracy());
				}

				if (location == null)
					return;

				mLastLocationMillis = SystemClock.elapsedRealtime();

				// Do something.

				mLastLocation = location;

			}
		};

		locationManager.requestLocationUpdates(provider, 100, 0.1f, listener);
	}

	public void stop() {
		locationManager.removeUpdates(listener);
	}

	public void addListener(GPSListener gpsListener) {
		listeners.add(gpsListener);
	}

	public void removeListener(GPSListener gpsListener) {
		listeners.remove(gpsListener);
	}
}
