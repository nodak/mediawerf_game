package net.patchingzone.ru4real.base;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;

public class Utils {

	public static MediaPlayer playSound(String url, int volume) {
		final MediaPlayer mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				Log.d("qq", "prepared");
				mMediaPlayer.start();

			}
		});

		mMediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {
				Log.d("qq", "" + percent);
			}
		});

		mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				Log.d("qq", "completed");
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
		Log.d("qq2", "" + volume);
		mMediaPlayer.setVolume(volume, volume);

	}

}
