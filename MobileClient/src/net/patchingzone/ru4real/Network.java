package net.patchingzone.ru4real;

import java.io.IOException;
import java.net.URI;
import java.util.Vector;

import net.patchingzone.ru4real.base.AppSettings;
import net.patchingzone.ru4real.walkietalkie.PlayerRecorder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.codebutler.android_websockets.SocketIOClient;

public class Network {

	// String TAG = "socketClientConnection";
	String TAG = "NETWORK";
	boolean connected = false;
	private SocketIOClient GameWebSocket;
	Vector<NetworkListener> listeners = new Vector<NetworkListener>();
	
	
	private String walkieTalkieServer = "http://outside.mediawerf.net:8080";
	public static SocketIOClient walkieTalkieWebSocket;
	public static String walkieTalkieFilesAddress = "outside.mediawerf.net/wt";
	//socket io server 
	public static Thread connection;
	public static int connectStatus = 2;
	public static Boolean canConnect = false;
	public static String channel = "0";
	
	public static String name = "";
	
	
	public Network() {

	}

	public void connectGame() {
		Log.d(TAG, "not yet");
		GameWebSocket = new SocketIOClient(URI.create(AppSettings.SERVER_ADDRESS), new SocketIOClient.Handler() {

			@Override
			public void onConnect() {
				Log.d(TAG, "connected");
				Long tsLong = System.currentTimeMillis() / 1000;
				String ts = "MobileClient" + tsLong.toString();
				connected = true;
				// connectStatus = 1;

				JSONObject registerData = new JSONObject();
				JSONArray arguments = new JSONArray();

				try {
					registerData.put("nickname", ts);
					arguments.put(registerData);
					Log.d(TAG, "" + arguments.toString() + " " + GameWebSocket);
					GameWebSocket.emit("register", arguments);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void on(String event, JSONArray arguments) {
				Log.d(TAG, "on");

				for (NetworkListener listener : listeners) {
					listener.onMessageReceived(event, arguments);
				}
			}

			@Override
			public void onDisconnect(int code, String reason) {
				Log.d(TAG, String.format("Disconnected! Code: %d Reason: %s", code, reason));
			}

			@Override
			public void onError(Exception error) {
				Log.d(TAG, error + "");
				// connectStatus = 2;
			}

			@Override
			public void onJSON(JSONObject json) {
				Log.d(TAG, "onJSON");

			}

			@Override
			public void onMessage(String message) {
				Log.d(TAG, "onMessage");

			}
		});

		GameWebSocket.connect();
	}
		
	public void connectWalkieTalkie() {	
		
		walkieTalkieWebSocket = new SocketIOClient(URI.create(walkieTalkieServer), new SocketIOClient.Handler() {
			String tag = "WalkieTalkie";
					
			@Override
			public void onConnect() {
		        Log.d(tag, "Connected!");
		     
		    }

		    @Override
		    public void on(String event, JSONArray arguments) {
		     
		        if(event.equals("userConnect")) 	//starts the game
		        	name = arguments.toString();
		        if(event.equals("downloadFile"))
		        {
					try {
						JSONObject ob = new JSONObject();
						
						ob = arguments.getJSONObject(0);
						Log.d("inout", ob.getString("url"));
						//Log.d("inout", arguments.toString());
						if(ob.getString("channel").equals(channel))
						{
							PlayerRecorder.play(ob.getString("url"));
							Log.d("inout", ob.getString("url"));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		    }

		    @Override
		    public void onDisconnect(int code, String reason) {
		        Log.d(tag, String.format("Disconnected! Code: %d Reason: %s", code, reason));  
		    }

		    @Override
		    public void onError(Exception error) {

		    }

			@Override
			public void onJSON(JSONObject json) {
			}

			@Override
			public void onMessage(String message) {
			}
		});
		walkieTalkieWebSocket.connect();
		

	}

	public void setListener(NetworkListener networkListener) {
		listeners.add(networkListener);

	}

	public void disconnect() {
		try {
			GameWebSocket.disconnect();
			walkieTalkieWebSocket.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void sendLocation(double lat, double lon) {
		//send new location
		JSONObject location = new JSONObject();
		JSONArray arguments = new JSONArray();
		try {
			location.put("lat", lat);
			location.put("lng", lon);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		arguments.put(location);
		
		try {
			GameWebSocket.emit("updateLocation", arguments);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage() {

	}

}
