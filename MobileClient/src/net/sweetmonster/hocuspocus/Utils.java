package net.sweetmonster.hocuspocus;

import android.util.Log;

public class Utils {

	private static String TAG = "m";

	public static final int DEBUG_ANDROID = 1;
	public static final int DEBUG_MODE = DEBUG_ANDROID;

	public static void logD(String string) {
		if (DEBUG_MODE == DEBUG_ANDROID) {
			Log.d(TAG, string);
		} else {
			System.out.println(TAG + " " + string);
		}
	}


}