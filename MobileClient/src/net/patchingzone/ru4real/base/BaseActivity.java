package net.patchingzone.ru4real.base;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

@SuppressLint("NewApi")
public class BaseActivity extends FragmentActivity {

	private static final String TAG = "BaseActivity";
	//BluetoothHelper mBluetoothHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		//mBluetoothHelper = new BluetoothHelper(this);

		if (AppSettings.fullscreen) {
			// activity in full screen
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			// requestWindowFeature(Window.FEATURE_ACTION_BAR);
			getWindow()
					.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

		if (AppSettings.hideHomeBar) {
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		}

		View main_layout = this.findViewById(android.R.id.content).getRootView();
		main_layout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);

		if (AppSettings.screenAlwaysOn) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}

		setVolume(100);
		setBrightness(1f);
		// Utils.playSound("http://outside.mediawerf.net/8-Light_2.mp3");
		// playSound("http://outside.mediawerf.net/music.ogg");

		//MediaButtonIntentReceiver mMediaButtonReceiver = new MediaButtonIntentReceiver();
		//IntentFilter mediaFilter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
		//mediaFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY + 1);
		//registerReceiver(mMediaButtonReceiver, mediaFilter);
		//mediaFilter.se
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		//mBluetoothHelper.start();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		//mBluetoothHelper.stop();
	}

	public void changeFragment(int id, Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		fragmentTransaction.replace(id, fragment);
		fragmentTransaction.commit();
	}

	public void addFragment(Fragment processingSketch, int fragmentPosition) {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(fragmentPosition, processingSketch);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();

	}

	public void removeFragment(Fragment processingSketch) {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.remove(processingSketch);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();

	}

	public boolean isTablet(Context context) {
		boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
		boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
		return (xlarge || large);
	}

	private void setBrightness(float f) {
		WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
		layoutParams.screenBrightness = f;
		getWindow().setAttributes(layoutParams);
	}

	public void setVolume(int value) {

		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		value = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) * value / 100;

		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, value, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

	}

	public boolean isVoiceConnected() {
		boolean retval = true;
		try {
			retval = BluetoothAdapter.getDefaultAdapter().getProfileConnectionState(
					android.bluetooth.BluetoothProfile.HEADSET) != android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;

		} catch (Exception exc) {
			// nothing to do
		}
		return retval;
	}

	/*
	protected void speak(String text) {

		HashMap<String, String> myHashRender = new HashMap<String, String>();

		if (mBluetoothHelper.isOnHeadsetSco()) {
			myHashRender.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_VOICE_CALL));
		}
		//mTts.speak(text, TextToSpeech.QUEUE_FLUSH, myHashRender);
	} 
	*/

	public void setWakeLock(boolean b) {

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");

		if (b) {
			wl.acquire();
		} else {
			wl.release();
		}

	}

	// override home buttons
	@Override
	public void onAttachedToWindow() {
		if (AppSettings.overrideHomeButtons) {
			this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
			super.onAttachedToWindow();
		}
	}

	// override volume buttons
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		Log.d(TAG, "" + keyCode);

		if (AppSettings.overrideVolumeButtons
				&& (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {

			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_BACK && AppSettings.closeWithBack) {
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	// inner class
	// BluetoothHeadsetUtils is an abstract class that has
	// 4 abstracts methods that need to be implemented.
	private class BluetoothHelper extends BluetoothHeadsetUtils {
		public BluetoothHelper(Context context) {
			super(context);
		}

		@Override
		public void onScoAudioDisconnected() {
			// Cancel speech recognizer if desired
		}

		@Override
		public void onScoAudioConnected() {
			// Should start speech recognition here if not already started
		}

		@Override
		public void onHeadsetDisconnected() {

		}

		@Override
		public void onHeadsetConnected() {

		}
	}

}
