package net.patchingzone.ru4real.nuevo;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class SoundManager 
{	
	private  AudioManager  mAudioManager;
	private  Context mContext;
		
	
	private MediaPlayer	 mMediaPlayer;
	
	private static SoundManager instance;	// Singleton instance
	
	// Private singleton constructor
	private SoundManager() 
	{
		// ...
	}				
	
	public static SoundManager getInstance()
	{
		if(instance == null)
			instance = new SoundManager();
		return instance;
	}
	
	
	public void init(Context theContext)
	{
		mContext = theContext;	  
	    mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE); 
	    mMediaPlayer = new MediaPlayer();
	}
	
	public void addSound(int Index,int SoundID)
	{
		if(true)return;				
	}		
	
	public void playSound(int soundId)
	{				
		// Create a new one & play
		mMediaPlayer = MediaPlayer.create(mContext, soundId);
		int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
		mMediaPlayer.setVolume(streamVolume, streamVolume);
		mMediaPlayer.start();
	}
	
	public boolean isPlaying()
	{
		if(mMediaPlayer == null)
			return false;
		
		return mMediaPlayer.isPlaying();		
	}
	
	public void releaseSounds()
	{
		if(mMediaPlayer == null)
			return;
		
		mMediaPlayer.stop();
		mMediaPlayer.release();
		mMediaPlayer = null;
	}	
}
