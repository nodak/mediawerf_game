package net.patchingzone.ru4real.base;

import java.io.File;
import java.util.HashMap;

import net.patchingzone.ru4real.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

@SuppressLint("NewApi")
public class BaseWebview extends Fragment {

	private WebView webView;
	final Handler myHandler = new Handler();
	private View v;

	/** Called when the activity is first created. */
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		// activity in full screen
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// requestWindowFeature(Window.FEATURE_ACTION_BAR);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);

		v = inflater.inflate(R.layout.webview, container, false);

		return v;
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		webView = (WebView) v.findViewById(R.id.webView1);

		webView.setHorizontalScrollBarEnabled(false);
		webView.setVerticalScrollBarEnabled(false);
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
		webView.addJavascriptInterface(new MyJavaScriptInterface(getActivity()), "android");

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
			Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(1000);
		}

		@JavascriptInterface
		public void updateSound(String url, int volume) {
			//getResources().get
	

			if (sounds.containsKey(url) == false) {
				//check if file is stored 
				String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
				String fileURL = Environment.getExternalStorageDirectory() + "/mediawerf/mp3/" + fileName;
				File f = new File(fileURL);
								
				Log.d("mm", " " + fileURL + " " + f.exists());

				String cURL = "";
				if (f.exists()) {
					cURL = fileURL;
					Toast.makeText(getActivity(), "SD", Toast.LENGTH_SHORT).show();
					Log.d("mm", " SD ");
				} else {
					Toast.makeText(getActivity(), "URL", Toast.LENGTH_SHORT).show();
					cURL = url;
					Log.d("mm", " URL ");
				}
				
				
				sounds.put(url, SoundUtils.playSound(getActivity(), cURL, volume));
				// sounds.put(url, null);

				Log.d("qq2", "play " + url + " " + volume);

			} else {
				Log.d("qq2", "volume " + url + " " + volume);
				SoundUtils.setVolume((MediaPlayer) (sounds.get(url)), volume);

			}

		}
	}

	public void setPage(String Url) {
		webView.loadUrl(Url);
	}

	
	@Override
	public void onDestroy() {
		super.onDestroy();

		for (int i = 0; i < sounds.size(); i++) {
			sounds.get(i).stop();
		}
	}

}
