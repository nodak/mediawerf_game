package net.patchingzone.ru4real.nuevo;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

public class Info {

	static public void info() {

		StringBuffer buf = new StringBuffer();
		buf.append("VERSION.RELEASE {" + Build.VERSION.RELEASE + "}");
		buf.append("\nVERSION.INCREMENTAL {" + Build.VERSION.INCREMENTAL);
		buf.append("\nVERSION.SDK {" + Build.VERSION.SDK + "}");
		buf.append("\nBOARD {" + Build.BOARD + "}");
		buf.append("\nBRAND {" + Build.BRAND + "}");
		buf.append("\nDEVICE {" + Build.DEVICE + "}");
		buf.append("\nFINGERPRINT {" + Build.FINGERPRINT + "}");
		buf.append("\nHOST {" + Build.HOST + "}");
		buf.append("\nID {" + Build.ID + "}");

		Log.d("build", buf.toString());

	}

	static public void DPI(Activity act) {

		DisplayMetrics metrics = new DisplayMetrics();
		act.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		switch (metrics.densityDpi) {
		case DisplayMetrics.DENSITY_HIGH: // HDPI
			break;
		case DisplayMetrics.DENSITY_LOW: // LDPI
			break;
		case DisplayMetrics.DENSITY_MEDIUM: // MDPI
			break;
		}

	}

	public static String deviceID(Context c) { 

		return Secure.getString(c.getContentResolver(), Secure.ANDROID_ID);

	}

	public static String getImei(Activity a) {
		return ((TelephonyManager) a.getSystemService(Activity.TELEPHONY_SERVICE)).getDeviceId();
	}

}
