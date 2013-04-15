package net.patchingzone.ru4real.base;

import java.util.HashMap;

import net.patchingzone.ru4real.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("NewApi")
public class BaseWebview extends Activity {

	private WebView webView;
	final Handler myHandler = new Handler();

	/** Called when the activity is first created. */
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// activity in full screen
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		// requestWindowFeature(Window.FEATURE_ACTION_BAR);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.webview);

		webView = (WebView) findViewById(R.id.webView1);

		webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setGeolocationEnabled(true);
		settings.setAppCacheEnabled(false);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {

			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

			}
		});

		settings.setLightTouchEnabled(true);
		webView.addJavascriptInterface(new MyJavaScriptInterface(this), "android");

		webView.setWebChromeClient(new WebChromeClient() {
			public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
				callback.invoke(origin, true, false);
			}
		});

		webView.getSettings().setGeolocationDatabasePath("/data/data/qq");

	}

	HashMap<String, MediaPlayer> sounds = new HashMap<String, MediaPlayer>();

	public class MyJavaScriptInterface {
		Context mContext;

		MyJavaScriptInterface(Context c) {
			mContext = c;
		}

		@JavascriptInterface
		public void Vibrate() {
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(1000);
		}

		@JavascriptInterface
		public void updateSound(String url, int volume) {
			if (sounds.containsKey(url) == false) {
				sounds.put(url, Utils.playSound(url, volume));
				//sounds.put(url, null);
				
				Log.d("qq2","play " +  url + " " + volume);

			} else {
				Log.d("qq2","volume " +  url + " " + volume);
				Utils.setVolume((MediaPlayer) (sounds.get(url)), volume);
				
			}

		}
	}

	public void setPage(String Url) {
		webView.loadUrl(Url);
	}

}
