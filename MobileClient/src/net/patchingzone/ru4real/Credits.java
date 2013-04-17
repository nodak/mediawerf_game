package net.patchingzone.ru4real;

import net.patchingzone.ru4real.base.BaseWebview;
import android.os.Bundle;

public class Credits extends BaseWebview {

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setPage("file:///android_asset/credit/index.html");
		// setPage("javascript:SetStyle();");
	}

}
