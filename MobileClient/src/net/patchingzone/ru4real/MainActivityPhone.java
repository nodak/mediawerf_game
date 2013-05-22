package net.patchingzone.ru4real;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import net.patchingzone.ru4real.audio.DebugSoundFragment;
import net.patchingzone.ru4real.base.AppSettings;
import net.patchingzone.ru4real.base.BaseActivity;
import net.patchingzone.ru4real.base.SoundUtils;
import net.patchingzone.ru4real.fragments.CameraFragment;
import net.patchingzone.ru4real.fragments.FragmentDistances;
import net.patchingzone.ru4real.fragments.GameWebViewFragment;
import net.patchingzone.ru4real.fragments.MainFragment;
import net.patchingzone.ru4real.fragments.MapCustomFragment;
import net.patchingzone.ru4real.fragments.SettingsFragment;
import net.patchingzone.ru4real.fragments.TextFragment;
import net.patchingzone.ru4real.fragments.Utils;
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

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
	private static final int MENU_SETTINGS = 3;
	private static final int MENU_PROCESSING = 4;
	private static final int MENU_MAIN = 5;
	private static final int MENU_PD = 6;
	private static final int MENU_VIDEO = 7;
	private static final int MENU_WEBVIEW_CREDITS = 8;
	private static final int MENU_TOGGLE_MAP = 9;
	private static final int MENU_APP_FINISH = 10;
	private static final int MENU_TOGGLE_LOGGER = 11;

	private float orientationPitch;

	private boolean toggle = false;
	private ProcessingSketch processingSketch;
	private VideoPlayerFragment videoPlayer;
	public Network network;

	private GPSManager gpsManager;
	public boolean gpsOn = true;
	private OrientationManager orientationManager;
	private AccelerometerManager accelerationManager;

	private boolean ready = false;

	private FragmentDistances fragmentDistances;
	//private TextFragment textFragment;
	SettingsFragment settingsFragment;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// check if tablet
		if (isTablet(this) == false) {
			Log.d("tablet", "no es un tablet");
			// This is not a tablet - start a new activity
			setContentView(R.layout.activity_forfragments_phone);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		} else {
			Log.d("tablet", "es un tablet");
			setContentView(R.layout.activity_forfragments_tablet);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		}
		c = this;

		AppSettings.get().load();

		L.d("BT", "" + isVoiceConnected());

		// addProcessingSketch(new ProcessingSketch(), R.id.f1);
		// addProcessingSketch(cameraFragment, R.id.f1);
		// addProcessingSketch(new WalkieTalkieFragment(), R.id.f2);
		// addProcessingSketch(new MainFragment(), R.id.f1);
		// addProcessingSketch(new GameWebViewFragment(), R.id.f1);

		// videoPlayer = new VideoPlayerFragment();
		/*
		 * addProcessingSketch(videoPlayer, R.id.f1);
		 * videoPlayer.addListener(new VideoListener() {
		 * 
		 * @Override public void onReady(boolean ready) { //
		 * videoPlayer.initVideo("/raw/cityfireflies"); }
		 * 
		 * @Override public void onFinish(boolean finished) { } });
		 */

		processingSketch = new ProcessingSketch();
		addFragment(processingSketch, R.id.fragmentProcessing);
		// addProcessingSketch(new YesNoFragment(), R.id.f1);
		// addProcessingSketch(new DebugSoundFragment(), R.id.fragmentSound);
		// addProcessingSketch(new DebugSoundFragment(), R.id.f2);

		if (AppSettings.debug == true) {
			OverlayLogger ol = new OverlayLogger();
			L.addLoggerWindow(ol);

			// L.filterByTag("QQ");
			// addFragment(ol, R.id.fragmentLogOverlay);

			fragmentDistances = new FragmentDistances();
			addFragment(fragmentDistances, R.id.f2);
		}
		// textFragment = new TextFragment();
		// addFragment(textFragment, R.id.f1);

		settingsFragment = new SettingsFragment();
		// removeFragment(settingsFragment);

		SoundUtils.speak(c, "Waiting to establish the connection", Locale.ENGLISH);

		if (isTablet(this)) {
			addFragment(new MapCustomFragment(), R.id.map);
		}

		network = new Network(this);
		network.connect();
		network.addGameListener(new NetworkListener() {

			@Override
			public void onUpdatedLocation(String event, JSONArray arguments) {
			}

			@Override
			public void onTargetInRange(double latitude, double longitude, String value, float distance, int range) {
				// L.d(TAG, "" + latitude + " " + longitude + " " + value + " "
				// + distance);

				int dist = Math.round(distance);
				int volume = 0;
				if (dist < range) {
					volume = (int) (100 - dist * (100 / range));
				}
				// L.d("volume", "" + volume);

				// SoundUtils.playSound(value, volume);
				updateSound(value, volume);

				if (AppSettings.debug == true) {
					fragmentDistances.addItem(value, "" + distance, "" + volume);
				}
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
				// fragmentDistances.addItem("qq", "5", "2");

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

			@Override
			public void onPlayerInRange(String nickname, String sound, float distance) {
				if (sound.isEmpty() == false) {
					L.d("PLAYER", "" + distance);

					int dist = Math.round(distance);
					int volume = 0;
					int range = 30;
					if (dist < range) {
						volume = (int) (100 - dist * (100 / range));
					}
					// L.d("volume", "" + volume);
					L.d("PLAYERINRANGE", sound + " " + distance);

					updateSound(sound, volume);

					if (AppSettings.debug == true) {
						fragmentDistances.addItem(nickname, "" + distance, "" + volume);
					}
				}
			}

			@Override
			public void onRefresh() {
				superMegaForceKill();
			}

			@Override
			public void onPlayerScored(Player player) {

			}

		});

		gpsManager = new GPSManager(this);
		gpsManager.addListener(new GPSListener() {

			@Override
			public void onSpeedChanged(float speed) {
			}

			@Override
			public void onLocationChanged(final double lat, final double lon, double alt, float speed, float accuracy) {
				if (gpsOn) {
					L.d("QQ", "" + orientationPitch);
					network.sendLocation(lat, lon, orientationPitch);
					// L.d("GPS", "gps");
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
				// L.d(TAG, "orientation " + pitch + " " + roll + " " + z);

				orientationPitch = pitch;
				// network.sendOrientation(pitch, roll, z);
			}
		});
		orientationManager.start();

		accelerationManager = new AccelerometerManager(this);
		accelerationManager.addListener(new AccelerometerListener() {

			@Override
			public void onShake(double force) {
				L.d(TAG, "acc " + force);
				// network.sendShake(force);
			}

			@Override
			public void onAccelerometerChanged(float x, float y, float z) {
				// L.d(TAG, "acc " + x + " " + y + " " + z);
			}
		});
		// accelerationManager.start();

		BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
			int scale = -1;
			int level = -1;
			int voltage = -1;
			int temp = -1;
			boolean isConnected = false;
			private int status;

			@Override
			public void onReceive(Context context, Intent intent) {
				level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
				scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
				temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
				voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
				// isCharging =
				// intent.getBooleanExtra(BatteryManager.EXTRA_PLUGGED, false);
				// status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
				status = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

				if (status == BatteryManager.BATTERY_PLUGGED_AC) {
					isConnected = true;
				} else if (status == BatteryManager.BATTERY_PLUGGED_USB) {
					isConnected = true;
				} else {
					isConnected = false;
				}

				if (isConnected == true && AppSettings.debug != true) {
					superMegaForceKill();
				}

				L.d("BATTERY", "level is " + level + " is connected " + isConnected);

				network.sendBattery(level);
			}
		};

		IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(batteryReceiver, filter);
	}

	protected void gpsReady() {
		if (!ready) {
			L.d("GPS", "ready");
			processingSketch.gpsLock(true);
			//textFragment.changeColor();
			SoundUtils.speak(c, "Connection established", Locale.ENGLISH);
			ready = true;
			network.sendGPSStatus(true);

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
		SoundUtils.speak(c, "You are ready to explore the world", Locale.ENGLISH);

	}

	HashMap<String, MediaPlayer> sounds = new HashMap<String, MediaPlayer>();

	private int TOGGLE_LOGGER;

	public void updateSound(String url, int volume) {
		// getResources().get
		// TODO fix this mess with SD assets, etc
		if (sounds.containsKey(url) == false) {
			// check if file is stored
			String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());

			// Reading from sdcard

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

			if (AppSettings.readFromAssets) {
				cURL = fileName;
			}

			sounds.put(url, SoundUtils.playSound(c, cURL, volume));
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

		// menu.add(0, MENU_WEBVIEW, 0, "Webview");
		// menu.add(0, MENU_CAMERA, 0, "Camera");
		// menu.add(0, MENU_WALKIE_TALKIE, 0, "Walkie Talkie");
		menu.add(0, MENU_SETTINGS, 0, "Settings");
		// menu.add(0, MENU_PROCESSING, 0, "Processing");
		// menu.add(0, MENU_TOGGLE_MAP, 0, "Toggle Map");
		// menu.add(0, MENU_TOGGLE_LOGGER, 0, "Toggle Logger");
		// menu.add(0, MENU_MAIN, 0, "Main");
		// menu.add(0, MENU_PD, 0, "PD");
		// menu.add(0, MENU_WEBVIEW_CREDITS, 0, "Webview Credits");
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

		case MENU_SETTINGS:
			addFragment(settingsFragment, R.id.fragmentSettings);
			// removeFragment(settingsFragment, R.id.fragmentSettings);

			return true;

		case MENU_PROCESSING:
			changeFragment(R.id.f1, new ProcessingSketch());

			return true;

		case MENU_TOGGLE_MAP:
			if (toggle) {
				View view = findViewById(R.id.f2);
				view.setVisibility(View.VISIBLE);
			} else {
				View view = findViewById(R.id.f2);
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

	public void superMegaForceKill() {

		int pid = android.os.Process.myPid();
		android.os.Process.killProcess(pid);

	}

	@Override
	protected void onPause() {
		super.onPause();

		gpsManager.stop();
		orientationManager.stop();
		accelerationManager.stop();
	}

	@Override
	protected void onResume() {
		super.onResume();

		gpsManager.start();
		// orientationManager.start();
		// accelerationManager.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		network.disconnect();
		Iterator it = sounds.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			// System.out.println(pairs.getKey() + " = " + pairs.getValue());
			MediaPlayer mp = (MediaPlayer) pairs.getValue();
			mp.stop();
			it.remove(); // avoids a ConcurrentModificationException
		}

		superMegaForceKill();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		L.d("KeyEvent", "" + keyCode);

		switch (keyCode) {
		case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
			// something for fast forward
			return true;
		case KeyEvent.KEYCODE_MEDIA_NEXT:
			// something for next
			return true;
		case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
			// something for play/pause
			return true;
		case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
			// something for previous
			return true;
		case KeyEvent.KEYCODE_MEDIA_REWIND:
			// something for rewind
			return true;
		case KeyEvent.KEYCODE_MEDIA_STOP:
			// something for stop
			return true;
		}

		return super.onKeyDown(keyCode, event);

	}

}
