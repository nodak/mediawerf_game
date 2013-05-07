package net.patchingzone.ru4real;

import android.util.Log;

public class L {

	public static boolean enabled = true;
	private static OverlayLogger overlayLogger = null;
	private static String filter = null;

	//TODO clean this up, I drunk too much coffee and cannot think
	public static void d(String TAG, String text) {

		if (enabled) {
			if (filter == null) {
				Log.d(TAG, text);

				Log.d("qq", "" + overlayLogger);
				if (overlayLogger != null) {
					overlayLogger.addItem(TAG, text);
				}
			} else if (TAG.equals(filter)) {
				
				Log.d(TAG, text);

				Log.d("qq", "" + overlayLogger);
				if (overlayLogger != null) {
					overlayLogger.addItem(TAG, text);
				}
			}
			
			
		}
	}

	public static void filterByTag(String tag) {
		filter = tag;
	}

	public static void addLoggerWindow(OverlayLogger ol) {
		overlayLogger = ol;

	}

}
