package net.patchingzone.ru4real.base;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;

public class Utils {

	private static final String TAG = "AudioPlayer";

	public static MediaPlayer playSound(String url, int volume) {
		final MediaPlayer mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setLooping(true);
		mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				Log.d(TAG, "prepared");
				mMediaPlayer.start();

			}
		});

		mMediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {
				Log.d(TAG, "" + percent);
			}
		});

		mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				Log.d(TAG, "completed");
			}
		});
		


		// mMediaPlayer.reset();
		try {
			mMediaPlayer.setDataSource(url);
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

			mMediaPlayer.prepareAsync();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mMediaPlayer.setVolume(volume, volume);

		return mMediaPlayer;
	}

	public static void setVolume(MediaPlayer mMediaPlayer, int value) {
		float volume = (float) (value) / 100;
		Log.d(TAG, "" + volume);
		mMediaPlayer.setVolume(volume, volume);

	}

}
