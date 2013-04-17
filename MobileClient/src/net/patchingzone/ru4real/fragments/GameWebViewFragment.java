package net.patchingzone.ru4real.fragments;

import net.patchingzone.ru4real.base.BaseWebview;
import android.os.Bundle;

public class GameWebViewFragment extends BaseWebview {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setPage("http://mediawerf.dyndns.org/fun/silent2.html");
		// setPage("http://www.meneame.net");
	}

}
