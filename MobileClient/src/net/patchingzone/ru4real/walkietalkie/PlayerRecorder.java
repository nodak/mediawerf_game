package net.patchingzone.ru4real.walkietalkie;

import java.io.IOException;

import net.patchingzone.ru4real.base.AppSettings;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.util.Log;

public class PlayerRecorder {

	MediaRecorder recorder;
	public Boolean active;

	public PlayerRecorder() {
		// TODO Auto-generated constructor stub
		active = false;
	}

	public void Start(String Filename) {
		if (!active) {
			try {
				recorder = new MediaRecorder();

				recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				recorder.setOutputFile(Filename);

				recorder.prepare();
				recorder.start();
				active = true;
			} catch (IllegalStateException e) {
				Log.e("recorder", e.toString());
			} catch (IOException e) {
				Log.e("recorder", e.toString());
			}
		}
	}

	public void Stop() {
		if (active) {
			recorder.stop();
			recorder.reset();
			recorder.release();
			recorder = null;
			active = false;
		}
	}

	public static void play(String name) {
		Log.d("receverd", name);
		try {
			String url = "http://" + AppSettings.SETTINGS_WALKIE_TALKIE_ADDRESS + "/talk/" + name; // your
																							// URL
																							// here
			final MediaPlayer mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setDataSource(url);
			// mediaPlayer.setVolume(100, 100);

			mediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

				@Override
				public void onBufferingUpdate(MediaPlayer mp, int percent) {
					// TODO Auto-generated method stub
					// Log.d("loaded", percent + "");

				}
			});

			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					Log.d("prepared", "true");
					mediaPlayer.start();
				}
			});
			try {
				mediaPlayer.prepareAsync(); // might take long! (for buffering,
											// etc)
			} catch (Exception e) {
				Log.e("prepare", e + "");
			}

		} catch (Exception e) {
			Log.e("Media", e + "");
			// TODO: handle exception
		}

	}

}
