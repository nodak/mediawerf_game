package net.patchingzone.ru4real;

import java.io.IOException;
import java.net.URI;
import java.util.Vector;

import net.patchingzone.ru4real.base.AppSettings;
import net.patchingzone.ru4real.game.Player;
import net.patchingzone.ru4real.walkietalkie.PlayerRecorder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codebutler.android_websockets.SocketIOClient;
import com.google.android.gms.maps.model.LatLng;

public class Network {

	// String TAG = "socketClientConnection";
	String TAG = "NETWORK";
	boolean gameWebSocketConnected = false;
	private SocketIOClient gameWebSocket;
	Vector<NetworkListener> listeners = new Vector<NetworkListener>();

	private String walkieTalkieServer = "http://outside.mediawerf.net:8080";
	public static String walkieTalkieFilesAddress = "outside.mediawerf.net/wt";
	public static SocketIOClient walkieTalkieWebSocket;

	// socket io server
	public static Thread connection;
	public static int connectStatus = 2;
	public static Boolean canConnect = false;
	public static String channel = "0";

	public static String name = "";

	public Network() {

	}

	public void connectGame() {
		L.d(TAG, "not yet");
		gameWebSocket = new SocketIOClient(URI.create(AppSettings.SERVER_ADDRESS), new SocketIOClient.Handler() {

			@Override
			public void onConnect() {
				//L.d(TAG, "connected");
				Long tsLong = System.currentTimeMillis() / 1000;
				String ts = "AndroidMobileClient" + tsLong.toString();
				gameWebSocketConnected = true;

				JSONObject registerData = new JSONObject();
				JSONArray arguments = new JSONArray();

				try {
					registerData.put("nickname", ts);
					arguments.put(registerData);
					//L.d(TAG, "" + arguments.toString() + " " + gameWebSocket);
					gameWebSocket.emit("register", arguments);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void on(String event, JSONArray arguments) {
				// Log.d(TAG, "on message " + event + " " +
				// arguments.toString());
				// L.d("qq", "--> " + event + " " + arguments.toString());

				if (event.equals("playerJoined")) {

					// Gson gson = new Gson();
					// Type listType = new TypeToken<List<Post>>() {}.getType();
					// List<Player> players = (List<Player>)
					// gson.fromJson(arguments, listType);
					// Player player = gson.fromJson(arguments, Player.class);

					try {
						Player player = new Player();
						JSONObject ob = new JSONObject();

						ob = arguments.getJSONObject(0);
						player.id = ob.getString("id");
						player.nickname = ob.getString("nickname");

						ob = ob.getJSONObject("location");
						//L.d("mm", "latitud " + ob.getString("lat"));
						//L.d("mm", "arguments " + arguments.toString());
						player.latLng = new LatLng(Double.parseDouble(ob.getString("lat")), Double.parseDouble(ob
								.getString("lng")));

						for (NetworkListener listener : listeners) {
							listener.onPlayerJoined(player);
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else if (event.equals("playerDisconnected")) {
					for (NetworkListener listener : listeners) {
						listener.onPlayerDisconnected(event, arguments);
					}
				} else if (event.equals("targetInRange")) {
				

					//String arguments = "targetInRange = {target: {location:{lat: lat, lng:lng}, value:url}, distance:meters}";
					
					try {
						JSONObject ob = new JSONObject();
						ob = arguments.getJSONObject(0);
						float distance =  Float.parseFloat( ob.get("distance").toString());
						ob = (JSONObject) ob.get("target");
						JSONObject obLoc = (JSONObject) ob.get("location");
						double latitude = Double.parseDouble(obLoc.get("lat").toString());
						double longitude = Double.parseDouble(obLoc.get("lng").toString());
						String value = ob.get("value").toString();
						
						for (NetworkListener listener : listeners) {
							listener.onTargetInRange(latitude, longitude, value, distance);
						}
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else if (event.equals("updateLocation")) {

					try {
						Player player = new Player();
						JSONObject ob = new JSONObject();
						ob = arguments.getJSONObject(0);
						ob = (JSONObject) ob.get("player");
						player.nickname = ob.get("nickname").toString();
						player.id = ob.get("id").toString();

						ob = ob.getJSONObject("location");
						player.latLng = new LatLng(Double.parseDouble(ob.getString("lat")), Double.parseDouble(ob
								.getString("lng")));

						//L.d(TAG, "--------> " + player.id + " " + player.nickname + player.latLng.latitude + " " + player.latLng.longitude);
						
						
						for (NetworkListener listener : listeners) {
							listener.onUpdateLocation(player);
						}
						
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else if (event.equals("listTrips")) {
					for (NetworkListener listener : listeners) {
						listener.onListTrips(event, arguments);
					}
				} else if (event.equals("textMessage")) {
					JSONObject ob = new JSONObject();
					try {
						ob = arguments.getJSONObject(0);
						String message = ob.get("message").toString();
						
						for (NetworkListener listener : listeners) {
							listener.onTextMessage(message);
						} 
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else if (event.equals("poke")) {
					for (NetworkListener listener : listeners) {
						listener.onPoke();
					}
				} else if (event.equals("listTargets")) {
					for (NetworkListener listener : listeners) {
						listener.onListTargets(event, arguments);
					}
				} else if (event.equals("")) {
					for (NetworkListener listener : listeners) {
						// listener.onMessageReceived(event, arguments);
					}
				}

				for (NetworkListener listener : listeners) {
					listener.onMessageReceived(event, arguments);
				}

			}

			@Override
			public void onDisconnect(int code, String reason) {
				L.d(TAG, String.format("Disconnected! Code: %d Reason: %s", code, reason));
			}

			@Override
			public void onError(Exception error) {
				L.d(TAG, error + "");
				// connectStatus = 2;
			}

			@Override
			public void onJSON(JSONObject json) {
				L.d(TAG, "onJSON");

			}

			@Override
			public void onMessage(String message) {
				L.d(TAG, "onMessage");

			}
		});

		gameWebSocket.connect();
	}

	public void connectWalkieTalkie() {

		walkieTalkieWebSocket = new SocketIOClient(URI.create(walkieTalkieServer), new SocketIOClient.Handler() {
			String tag = "WalkieTalkie";

			@Override
			public void onConnect() {
				L.d(tag, "Connected!");

			}

			@Override
			public void on(String event, JSONArray arguments) {

				if (event.equals("userConnect")) // starts the game
					name = arguments.toString();
				if (event.equals("downloadFile")) {
					try {
						JSONObject ob = new JSONObject();
						ob = arguments.getJSONObject(0);
						L.d("inout", ob.getString("url"));
						// Log.d("inout", arguments.toString());
						if (ob.getString("channel").equals(channel)) {
							PlayerRecorder.play(ob.getString("url"));
							L.d("inout", ob.getString("url"));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onDisconnect(int code, String reason) {
				L.d(tag, String.format("Disconnected! Code: %d Reason: %s", code, reason));
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

	public void addGameListener(NetworkListener networkListener) {
		listeners.add(networkListener);

	}

	public void removeGameListner(NetworkListener networkListener) {
		listeners.add(networkListener);
	}

	public void disconnect() {
		try {
			gameWebSocket.disconnect();
			walkieTalkieWebSocket.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void sendLocation(double lat, double lon) {
		if (gameWebSocketConnected) {
			// send new location
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
				gameWebSocket.emit("updateLocation", arguments);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendMessage() {

	}

}
