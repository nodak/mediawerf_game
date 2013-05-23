package net.patchingzone.ru4real;

import java.io.IOException;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import net.patchingzone.ru4real.base.AppSettings;
import net.patchingzone.ru4real.game.Player;
import net.patchingzone.ru4real.walkietalkie.PlayerRecorder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.codebutler.android_websockets.SocketIOClient;
import com.google.android.gms.maps.model.LatLng;

public class Network {

	// String TAG = "socketClientConnection";
	String TAG = "NETWORK";
	boolean gameWebSocketConnected = false;
	// boolean libraryWebSocketConnected = false;
	boolean walkieTalkieWebSocketConnected = false;
	private SocketIOClient gameWebSocket;
	private SocketIOClient libraryWebSocket;
	public static SocketIOClient walkieTalkieWebSocket;
	Vector<NetworkListener> listeners = new Vector<NetworkListener>();

	private MainActivityPhone c;
	// private Timer timerLibrary;
	private Timer timerGame;

	// socket io server
	public static Thread connection;
	public static int connectStatus = 2;
	public static Boolean canConnect = false;
	public static String channel = "0";

	public static String name = "";

	public Network(Context c) {
		this.c = (MainActivityPhone) c;

	}

	public void connectGame() {
		// L.d(TAG, "not yet");
		gameWebSocket = new SocketIOClient(URI.create(AppSettings.get().serverAddress), new SocketIOClient.Handler() {

			@Override
			public void onConnect() {
				L.d(TAG, "game connected");
				// Random user name
				// Long tsLong = System.currentTimeMillis() / 1000;
				// String ts = "AndroidMobileClient" + tsLong.toString();
				String ts = AppSettings.get().playerID;

				gameWebSocketConnected = true;
				timerGame.cancel();

				JSONObject registerData = new JSONObject();
				JSONArray arguments = new JSONArray();

				try {
					registerData.put("nickname", ts);
					registerData.put("drone", false);
					arguments.put(registerData);
					// L.d(TAG, "" + arguments.toString() + " " +
					// gameWebSocket);
					gameWebSocket.emit("register", arguments);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void on(String event, JSONArray arguments) {
				L.d(TAG, "on message " + event + " " + arguments.toString());
				// L.d("qq", "--> " + event + " " + arguments.toString());
				// L.d("jj", "" + event);

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
						// L.d("mm", "latitud " + ob.getString("lat"));
						// L.d("mm", "arguments " + arguments.toString());
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

					// String arguments =
					// "targetInRange = {target: {location:{lat: lat, lng:lng}, value:url}, distance:meters}";

					try {
						JSONObject ob = new JSONObject();
						ob = arguments.getJSONObject(0);
						float distance = Float.parseFloat(ob.get("distance").toString());
						ob = (JSONObject) ob.get("target");
						JSONObject obLoc = (JSONObject) ob.get("location");
						double latitude = Double.parseDouble(obLoc.get("lat").toString());
						double longitude = Double.parseDouble(obLoc.get("lng").toString());
						String value = ob.get("value").toString();
						int range = Integer.parseInt(ob.get("range").toString());

						for (NetworkListener listener : listeners) {
							listener.onTargetInRange(latitude, longitude, value, distance, range);
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else if (event.equals("playerInRange")) {

					// String arguments =
					// "targetInRange = {player: {location:{lat: lat, lng:lng}, value:url}, distance:meters}";

					try {
						JSONObject ob = new JSONObject();
						ob = arguments.getJSONObject(0);
						float distance = Float.parseFloat(ob.get("distance").toString());
						JSONObject player = ob.getJSONObject("player");
						String sound = (String) player.get("sound").toString();
						String nickname = (String) player.get("nickname").toString();

						for (NetworkListener listener : listeners) {
							listener.onPlayerInRange(nickname, sound, distance);
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

						// L.d(TAG, "--------> " + player.id + " " +
						// player.nickname + player.latLng.latitude + " " +
						// player.latLng.longitude);

						for (NetworkListener listener : listeners) {
							listener.onUpdateLocation(player);
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else if (event.equals("playerScored")) {

					try {
						Player player = new Player();
						JSONObject ob = new JSONObject();
						ob = arguments.getJSONObject(0);
						ob = (JSONObject) ob.get("player");
						player.nickname = (String) ob.get("nickname").toString();

						//L.d("zz", player.nickname + " " + AppSettings.playerID);
						if (player.nickname.equals(AppSettings.playerID)) {
							player.score = Integer.parseInt(ob.get("score").toString());
							ob = arguments.getJSONObject(0);
							int targetIndex = Integer.parseInt(ob.get("targetIndex").toString());
							int totalTargets = Integer.parseInt(ob.get("totalObjectives").toString());
							// int totalTargets =
							// Integer.parseInt("nickname").toString();

							for (NetworkListener listener : listeners) {
								listener.onPlayerScored(player, targetIndex, totalTargets);
							}
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
				} else if (event.equals("refresh")) {
					for (NetworkListener listener : listeners) {
						listener.onRefresh();
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
				gameWebSocketConnected = false;

				connect();
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

	protected void connect() {
		if (gameWebSocketConnected == false) {
			L.d("CONNECTION", "NO CONNECTED");
			timerGame = new Timer();
			timerGame.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					connectGame();
					L.d("CONNECTION", "CONNECTED");
				}
			}, 0, 3000);
		}

		/*
		 * timerLibrary = new Timer(); if (libraryWebSocketConnected == false) {
		 * timerLibrary.scheduleAtFixedRate(new TimerTask() {
		 * 
		 * @Override public void run() { connectLibrary(); } }, 0, 1000); }
		 */
	}

	/*
	 * public void connectLibrary() {
	 * 
	 * libraryWebSocket = new
	 * SocketIOClient(URI.create(AppSettings.get().libraryAddress), new
	 * SocketIOClient.Handler() { String tag = "Library";
	 * 
	 * @Override public void onConnect() { L.d(tag, "Connected!");
	 * libraryWebSocketConnected = true; timerLibrary.cancel(); }
	 * 
	 * @Override public void on(String event, JSONArray arguments) {
	 * 
	 * if (event.equals("userConnect")) { // name = arguments.toString();
	 * 
	 * } }
	 * 
	 * @Override public void onDisconnect(int code, String reason) { L.d(tag,
	 * String.format("Disconnected! Code: %d Reason: %s", code, reason));
	 * libraryWebSocketConnected = false; }
	 * 
	 * @Override public void onError(Exception error) {
	 * 
	 * }
	 * 
	 * @Override public void onJSON(JSONObject json) { }
	 * 
	 * @Override public void onMessage(String message) { } });
	 * libraryWebSocket.connect();
	 * 
	 * }
	 */

	public void connectWalkieTalkie() {

		walkieTalkieWebSocket = new SocketIOClient(URI.create(AppSettings.get().libraryAddress),
				new SocketIOClient.Handler() {
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
						walkieTalkieWebSocketConnected = false;
					}

					@Override
					public void onError(Exception error) {
						L.d("peta", "esto peta");
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
			Log.d("qq", "" + gameWebSocket);
			gameWebSocket.disconnect();
			// walkieTalkieWebSocket.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void sendLocation(double lat, double lon, float orientation) {
		if (gameWebSocketConnected) {
			// send new location
			JSONObject location = new JSONObject();
			JSONArray arguments = new JSONArray();
			try {
				location.put("lat", lat);
				location.put("lng", lon);
				location.put("or", orientation);
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

	public void sendBattery(int batteryLevel) {
		if (gameWebSocketConnected) {
			// send new location
			JSONObject location = new JSONObject();
			JSONArray arguments = new JSONArray();
			try {
				location.put("battery", batteryLevel);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			arguments.put(location);

			try {
				gameWebSocket.emit("updateBattery", arguments);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendGPSStatus(boolean status) {
		if (gameWebSocketConnected) {
			// send new location
			JSONObject location = new JSONObject();
			JSONArray arguments = new JSONArray();
			try {
				location.put("status", status);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			arguments.put(location);

			try {
				gameWebSocket.emit("gpsStatus", arguments);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendMessage() {

	}

	/*
	 * public void sendShake(double force) { if (libraryWebSocketConnected) {
	 * 
	 * // send new location JSONObject acc = new JSONObject(); JSONArray
	 * arguments = new JSONArray(); try { acc.put("force", force); } catch
	 * (JSONException e) { e.printStackTrace(); } arguments.put(acc);
	 * 
	 * try { libraryWebSocket.emit("updateForce", arguments); } catch
	 * (JSONException e) { e.printStackTrace(); } }
	 * 
	 * }
	 * 
	 * 
	 * public void sendAnswer(boolean b) { L.d(TAG, "the answer is " + b); if
	 * (libraryWebSocketConnected) {
	 * 
	 * // send new location JSONObject acc = new JSONObject(); JSONArray
	 * arguments = new JSONArray(); try { acc.put("answer", b); } catch
	 * (JSONException e) { e.printStackTrace(); } arguments.put(acc);
	 * 
	 * try { libraryWebSocket.emit("updateAnswer", arguments); } catch
	 * (JSONException e) { e.printStackTrace(); } }
	 * 
	 * }
	 */

	/*
	 * public void sendOrientation(float pitch, float roll, float z) { if
	 * (libraryWebSocketConnected) {
	 * 
	 * // send new location JSONObject or = new JSONObject(); JSONArray
	 * arguments = new JSONArray(); try { or.put("pitch", pitch); or.put("roll",
	 * roll); or.put("z", z); } catch (JSONException e) { e.printStackTrace(); }
	 * arguments.put(or);
	 * 
	 * try { libraryWebSocket.emit("updateOrientation", arguments); } catch
	 * (JSONException e) { e.printStackTrace(); } }
	 * 
	 * }
	 */

}
