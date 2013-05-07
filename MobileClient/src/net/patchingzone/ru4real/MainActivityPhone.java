package net.patchingzone.ru4real;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

import net.patchingzone.ru4real.audio.DebugSoundFragment;
import net.patchingzone.ru4real.base.BaseActivity;
import net.patchingzone.ru4real.base.SoundUtils;
import net.patchingzone.ru4real.fragments.CameraFragment;
import net.patchingzone.ru4real.fragments.GameWebViewFragment;
import net.patchingzone.ru4real.fragments.MainFragment;
import net.patchingzone.ru4real.fragments.MapCustomFragment;
import net.patchingzone.ru4real.fragments.Utils;
import net.patchingzone.ru4real.fragments.VideoListener;
import net.patchingzone.ru4real.fragments.VideoPlayerFragment;
import net.patchingzone.ru4real.game.Player;
import net.patchingzone.ru4real.processing.ProcessingSketch;
import net.patchingzone.ru4real.sensors.AccelerometerListener;
import net.patchingzone.ru4real.sensors.AccelerometerManager;
import net.patchingzone.ru4real.sensors.GPSListener;
import net.patchingzone.ru4real.sensors.GPSManager;
import net.patchingzone.ru4real.sensors.OrientationListener;
import net.patchingzone.ru4real.sensors.OrientationManager;
import net.patchingzone.ru4real.walkietalkie.WalkieTalkieFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/*
 * STATES 
 * 
 * 1. Introduction (video)
 * 2. GPS LOCK -> GO (Change color + sound) 
 * 3. Game 
 * 
 */
@SuppressLint("NewApi")
public class MainActivityPhone extends BaseActivity {

	private static final String TAG = "FragmentHolder";

	Context c;
	private static final int MENU_WEBVIEW = 0;
	private static final int MENU_CAMERA = 1;
	private static final int MENU_WALKIE_TALKIE = 2;
	private static final int MENU_MAP = 3;
	private static final int MENU_PROCESSING = 4;
	private static final int MENU_MAIN = 5;
	private static final int MENU_PD = 6;
	private static final int MENU_VIDEO = 7;
	private static final int MENU_WEBVIEW_CREDITS = 8;
	private static final int MENU_TOGGLE_PROCESSING = 9;
	private static final int MENU_APP_FINISH = 10;
	private static final int MENU_TOGGLE_LOGGER = 11;

	private boolean toggle = false;
	private ProcessingSketch processingSketch;
	private VideoPlayerFragment videoPlayer;
	private Network network;

	private GPSManager gpsManager;
	private OrientationManager orientationManager;
	private AccelerometerManager accelerationManager;

	private boolean ready = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forfragments_phone_2);
		c = this;
		// addProcessingSketch(new ProcessingSketch(), R.id.f1);
		// addProcessingSketch(cameraFragment, R.id.f1);
		// addProcessingSketch(new MapCustomFragment(), R.id.f1);
		// addProcessingSketch(new WalkieTalkieFragment(), R.id.f2);
		// addProcessingSketch(new DebugSoundFragment(), R.id.f1);
		// addProcessingSketch(new MainFragment(), R.id.f1);
		// addProcessingSketch(new GameWebViewFragment(), R.id.f1);

		videoPlayer = new VideoPlayerFragment();
		addProcessingSketch(videoPlayer, R.id.f1);
		videoPlayer.addListener(new VideoListener() {

			@Override
			public void onReady(boolean ready) {
				// videoPlayer.initVideo("/raw/cityfireflies");
			}

			@Override
			public void onFinish(boolean finished) {
			}
		});

		processingSketch = new ProcessingSketch();
		addProcessingSketch(processingSketch, R.id.fragmentProcessing);

		OverlayLogger ol = new OverlayLogger();
		L.addLoggerWindow(ol);
		// L.filterByTag("NETWORK");
		addProcessingSketch(ol, R.id.fragmentLogOverlay);

		network = new Network();
		network.connectGame();
		network.addGameListener(new NetworkListener() {

			@Override
			public void onUpdatedLocation(String event, JSONArray arguments) {
			}

			@Override
			public void onTargetInRange(double latitude, double longitude, String value, float distance) {
				L.d(TAG, "" + latitude + " " + longitude + " " + value + " " + distance);

				int dist = Math.round(distance);
				int volume = 0;
				int active_range = 40;
				if (dist < active_range) {
					volume = (int) (100 - dist * (100 / active_range));
				}
				L.d("volume", "" + volume);

				// SoundUtils.playSound(value, volume);
				updateSound(value, volume);
			}

			@Override
			public void onPlayerJoined(final Player player) {

			}

			@Override
			public void onPlayerDisconnected(String event, JSONArray arguments) {
			}

			@Override
			public void onMessageReceived(final String event, final JSONArray arguments) {
				// L.d(TAG, "" + event + " " + arguments);
			}

			@Override
			public void onListTrips(String event, JSONArray arguments) {
			}

			@Override
			public void onListTargets(String event, JSONArray arguments) {
			}

			@Override
			public void onUpdateLocation(final Player player) {
				/*
				 * L.d("player", "--> update location"); ((Activity)
				 * c).runOnUiThread(new Runnable() {
				 * 
				 * @Override public void run() {
				 * 
				 * //Player p = (Player) playerList.get(player.id); //if (p !=
				 * null) { // L.d("player", "player update " + p.latLng.latitude
				 * + " " + p.latLng.longitude); //
				 * p.marker.setPosition(player.latLng); //} } });
				 */
			}

			@Override
			public void onTextMessage(String text) {
				String cmd[] = text.split("::");
				
				if (cmd[0].contains("/poke")) {
					Utils.vibrate(c, 500);
				} else if (cmd[0].contains("/say")) {
					L.d("TEXT", cmd[1]);
					SoundUtils.speak(c, cmd[1], Locale.ENGLISH);
				} else if (cmd[0].contains("/image")) {
					Log.d("image", cmd[1]);
					processingSketch.changeImage(cmd[1]);
				}
			}

			@Override
			public void onPoke() {
			}

		});

		gpsManager = new GPSManager(this);
		gpsManager.addListener(new GPSListener() {

			@Override
			public void onSpeedChanged(float speed) {
			}

			@Override
			public void onLocationChanged(final double lat, final double lon, double alt, float speed, float accuracy) {
				// L.d(TAG, " " + lat + " " + lon + " " + alt);
				// Date df = new java.util.Date(location.getTime());
				// String vv = new
				// SimpleDateFormat("dd-MM-yyyy , HH:mm:ss").format(df);

				JSONObject gpsData = new JSONObject();
				JSONArray arguments = new JSONArray();

				try {
					gpsData.put("lat", lat);
					gpsData.put("Long", lon);
					gpsData.put("Alt", alt);
					gpsData.put("Acc", speed);
					gpsData.put("Acc", accuracy);
					// gpsData.put("Time", vv);
					// gpsData.put("TimeStamp", location.getTime());
					// gpsData.put("Head", location.getBearing());
					arguments.put(gpsData);
					network.sendLocation(lat, lon);
					// currentPosition.setPosition(new LatLng(lat, lon));

					// L.d(TAG, arguments.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}

				gpsReady();
				// currentPosition.setPosition(new LatLng(lat, lon));
			}

			@Override
			public void onGPSSignalGood() {
			}

			@Override
			public void onGPSSignalBad() {
			}

			@Override
			public void onGPSStatus(boolean isGPSFix) {
			}
		});
		gpsManager.start();

		orientationManager = new OrientationManager(this);
		orientationManager.addListener(new OrientationListener() {

			@Override
			public void onOrientation(float pitch, float roll, float z) {
				L.d(TAG, "orientation " + pitch + " " + roll + " " + z);
			}
		});
		// orientationManager.start();

		accelerationManager = new AccelerometerManager(this);
		accelerationManager.addListener(new AccelerometerListener() {

			@Override
			public void onShake(float force) {
				L.d(TAG, "acc " + force);
			}

			@Override
			public void onAccelerometerChanged(float x, float y, float z) {
				L.d(TAG, "acc " + x + " " + y + " " + z);
			}
		});
		// accelerationManager.start();

	}

	protected void gpsReady() {
		if (!ready) {
			processingSketch.gpsLock(true);
			SoundUtils.speak(c, "GPS Ready", Locale.ENGLISH);
			ready = true;

			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					connectToTheWorld();
				}
			}, 5000);
		}
	}

	protected void connectToTheWorld() {
		SoundUtils.speak(c, "Connecting to the world", Locale.ENGLISH);

	}

	HashMap<String, MediaPlayer> sounds = new HashMap<String, MediaPlayer>();

	private int TOGGLE_LOGGER;

	public void updateSound(String url, int volume) {
		// getResources().get

		if (sounds.containsKey(url) == false) {
			// check if file is stored
			String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
			String fileURL = Environment.getExternalStorageDirectory() + "/mediawerf/mp3/" + fileName;
			File f = new File(fileURL);

			Log.d("mm", " " + fileURL + " " + f.exists());

			String cURL = "";
			if (f.exists()) {
				cURL = fileURL;
				// Toast.makeText(c, "SD", Toast.LENGTH_SHORT).show();
				Log.d("mm", " SD ");
			} else {
				// Toast.makeText(c, "URL", Toast.LENGTH_SHORT).show();
				cURL = url;
				Log.d("mm", " URL ");
			}

			sounds.put(url, SoundUtils.playSound(cURL, volume));
			// sounds.put(url, null);

			Log.d("qq2", "play " + url + " " + volume);

		} else {
			Log.d("qq2", "volume " + url + " " + volume);
			SoundUtils.setVolume((MediaPlayer) (sounds.get(url)), volume);

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, MENU_WEBVIEW, 0, "Webview");
		// menu.add(0, MENU_CAMERA, 0, "Camera");
		menu.add(0, MENU_WALKIE_TALKIE, 0, "Walkie Talkie");
		menu.add(0, MENU_MAP, 0, "Map");
		// menu.add(0, MENU_PROCESSING, 0, "Processing");
		// menu.add(0, TOGGLE_PROCESSING, 0, "Toggle Processing");
		menu.add(0, MENU_TOGGLE_LOGGER, 0, "Toggle Logger");
		// menu.add(0, MENU_MAIN, 0, "Main");
		menu.add(0, MENU_PD, 0, "PD");
		menu.add(0, MENU_WEBVIEW_CREDITS, 0, "Webview Credits");
		menu.add(0, MENU_APP_FINISH, 0, "Finish");

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case MENU_WEBVIEW:
			changeFragment(R.id.f1, new GameWebViewFragment());

			return true;

		case MENU_WEBVIEW_CREDITS:
			changeFragment(R.id.f1, new Credits());

			return true;

		case MENU_CAMERA:

			CameraFragment cameraFragment = new CameraFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("color", CameraFragment.MODE_COLOR_COLOR);
			bundle.putInt("camera", CameraFragment.MODE_CAMERA_BACK);
			cameraFragment.setArguments(bundle);

			changeFragment(R.id.f1, cameraFragment);

			return true;

		case MENU_WALKIE_TALKIE:
			changeFragment(R.id.f1, new WalkieTalkieFragment());

			return true;

		case MENU_MAP:

			changeFragment(R.id.f1, new MapCustomFragment());

			return true;

		case MENU_PROCESSING:
			changeFragment(R.id.f1, new ProcessingSketch());

			return true;

		case MENU_TOGGLE_PROCESSING:
			if (toggle) {
				View view = findViewById(R.id.fragmentProcessing);
				view.setVisibility(View.INVISIBLE);

				view = findViewById(R.id.f1);
				view.setVisibility(View.VISIBLE);
			} else {
				View view = findViewById(R.id.fragmentProcessing);
				view.setVisibility(View.VISIBLE);

				view = findViewById(R.id.f1);
				view.setVisibility(View.INVISIBLE);

			}
			toggle ^= true;
			return true;

		case MENU_TOGGLE_LOGGER:
			if (toggle) {
				View view = findViewById(R.id.fragmentLogOverlay);
				view.setVisibility(View.INVISIBLE);

			} else {
				View view = findViewById(R.id.fragmentLogOverlay);
				view.setVisibility(View.VISIBLE);

			}
			toggle ^= true;
			return true;

		case MENU_MAIN:
			changeFragment(R.id.f1, new MainFragment());

			return true;

		case MENU_VIDEO:
			changeFragment(R.id.f1, new VideoPlayerFragment());

			return true;

		case MENU_PD:
			changeFragment(R.id.f1, new DebugSoundFragment());

			return true;

		case MENU_APP_FINISH:
			finish();

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		gpsManager.stop();
		orientationManager.stop();
		accelerationManager.stop();
	}

}
