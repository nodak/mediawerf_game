package net.patchingzone.ru4real.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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

		if (AppSettings.screenAlwaysOn) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}

		setVolume(100);
		setBrightness(1f);
		// Utils.playSound("http://outside.mediawerf.net/8-Light_2.mp3");
		// playSound("http://outside.mediawerf.net/music.ogg");

	} 
	
	public void changeFragment(int id, Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		fragmentTransaction.replace(id, fragment);
		fragmentTransaction.commit();
	}

	public void addProcessingSketch(Fragment processingSketch, int fragmentPosition) {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(fragmentPosition, processingSketch);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();

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

}
