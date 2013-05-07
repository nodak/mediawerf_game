package net.patchingzone.ru4real.fragments;

import net.patchingzone.ru4real.base.BaseWebview;
import net.patchingzone.ru4real.base.SoundUtils;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class GameWebViewFragment extends BaseWebview {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setPage("http://mediawerf.dyndns.org/fun/silent2.html");
		// setPage("http://www.meneame.net");

		String url = Environment.getExternalStorageDirectory() + "/mediawerf/mp3/party.mp3";

		String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
		String fileNameWithoutExtn = fileName.substring(0, fileName.lastIndexOf('.'));

		Log.d("qq", "" + fileName + " " + fileNameWithoutExtn);

		//SoundUtils.playSound(url, 100);
	}

}
