package net.patchingzone.ru4real;

import java.net.URI;
import java.util.Vector;

import net.patchingzone.ru4real.base.AppSettings;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.codebutler.android_websockets.SocketIOClient;

public class Network {

	// String TAG = "socketClientConnection";
	String TAG = "qq";
	boolean connected = false;
	private SocketIOClient ioWebSocket;
	Vector<NetworkListener> listeners = new Vector<NetworkListener>();

	public Network() {

	}

	public void connect() {
		Log.d(TAG, "not yet");
		ioWebSocket = new SocketIOClient(URI.create(AppSettings.SERVER_ADDRESS), new SocketIOClient.Handler() {

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
					Log.d(TAG, "" + arguments.toString() + " " + ioWebSocket);

					ioWebSocket.emit("register", arguments);
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

		ioWebSocket.connect();

	}

	public void setListener(NetworkListener networkListener) {
		listeners.add(networkListener);

	}

	public void disconnect() {

	}

	public void sendMessage() {

	}

}
