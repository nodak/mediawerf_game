package net.patchingzone.ru4real.base;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.patchingzone.ru4real.game.Game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.codebutler.android_websockets.SocketIOClient;

public class MainApp extends Application {

	public static SharedPreferences app_preferences;

	public static int team;
	public static Boolean gameActive = false;

	public static SocketIOClient ioWebSocket;
	public static String address;
	public static Boolean canConnect = false;
	public static int connectStatus = 2;
	public static Thread connection;

	LocationManager GPS;
	LocationManager locationManager;
	LocationListener listener;

	// gps
	public void createGPS() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		GPS = (LocationManager) getSystemService(LOCATION_SERVICE);
		String provider;
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
				startActivity(intent);
			}

			@Override
			public void onLocationChanged(Location location) {
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
					ioWebSocket.emit("gpsUpdate", arguments);
					// Log.d("gps", arguments.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		locationManager.requestLocationUpdates(provider, 100, 1, listener);
	}

	public void stopGps() {
		locationManager.removeUpdates(listener);
	}

	// connection
	public boolean startConnection() {
		if (true)
		// if(app_preferences.getString("IP", "") != "" &&
		// app_preferences.getString("Port", "") != "")
		{
			connectStatus = 0;
			address = "http://mediawerf.dyndns.org:7080";
			// address = "http://"+ app_preferences.getString("IP", "") +":"+
			// app_preferences.getString("Port", "");

			connection = new Thread() {
				@Override
				public void run() {
					try {
						socketCientConnection();
						ioWebSocket.connect();

					} catch (Exception e) {
						Log.e("exeption", "" + e);
						MainApp.connectStatus = 2;
					}
				}
			};
			connection.start();
			// check Connection
			while (connectStatus == 0) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return canConnect;
	}

	// close connection

	public boolean closeConnection() {
		try {
			connection = null;
			ioWebSocket.disconnect();
			return false;
		} catch (IOException e) {
			return true;
		}
	}

	public void socketCientConnection() {
		ioWebSocket = new SocketIOClient(URI.create(address), new SocketIOClient.Handler() {
			String tag = "socketCientConnection";

			@Override
			public void onConnect() {
				Log.d(tag, "Connected!");
				MainApp.canConnect = true;
				MainApp.connectStatus = 1;
			}

			@Override
			public void on(String event, JSONArray arguments) {
				if (event.equals("succesfull")) // run when device connection is
												// succesfull
					sucsessfull(arguments);
				if (event.equals("playerJoined"))// run when player a player has
													// joint the game
					onPlayerList(arguments);
				if (event.equals("startGame")) // starts the game
					startGame();
				if (event.equals("stopGame")) // stops the game
					gameActive = false;
			}

			@Override
			public void onDisconnect(int code, String reason) {
				Log.d(tag, String.format("Disconnected! Code: %d Reason: %s", code, reason));
			}

			@Override
			public void onError(Exception error) {
				MainApp.connectStatus = 2;
			}
		});
	}

	public void sucsessfull(JSONArray input) {
		// sets player's teamID
		try {
			JSONObject team = new JSONObject(input.getJSONObject(0).getString("team"));
			MainApp.team = Integer.parseInt(team.getString("id"));
			Log.d("TeamID", MainApp.team + "");
		} catch (JSONException e) {
			MainApp.team = 999;
			Log.d("TeamID", "fail");
		}
	}

	public void startGame() {
		Log.d("game", "0");
		if (!gameActive) {
			gameActive = true;
			Intent intent = new Intent(this, Game.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			Log.d("game", "1");
		}

	}

	public void onPlayerList(JSONArray players) {
		// code for reaciving players here //
		Log.d("playerList", players.toString());
	}

	public void testMsg() {
		JSONArray arguments = new JSONArray();
		arguments.put("Hello Server");
		try {
			ioWebSocket.emit("debug", arguments);
			// Log.d("message", "sended");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}