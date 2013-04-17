package net.patchingzone.ru4real.game;

import net.patchingzone.ru4real.R;
import net.patchingzone.ru4real.base.MainApp;
import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Game extends Activity {

	MainApp ma;
	public RelativeLayout body;
	public Animation fade;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);

	}

	@Override
	public void onBackPressed() {
		// prevents player from leaving the game via the back button
		Toast.makeText(getApplicationContext(), "You shall not pass!", Toast.LENGTH_SHORT).show();

	}

}