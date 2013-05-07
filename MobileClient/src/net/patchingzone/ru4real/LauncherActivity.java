package net.patchingzone.ru4real;

import net.patchingzone.ru4real.base.BaseActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class LauncherActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_launcher);

		
		Intent intent = null;
	 	//intent = new Intent(this, WalkieTalkieFragment.class);
		//startActivity(intent);

		// check if tablet
		if (isTablet(this) == false) {
			Log.d("tablet", "no es un tablet");
			// This is not a tablet - start a new activity
			intent = new Intent(this, MainActivityPhone.class);
		} else {
			Log.d("tablet", "es un tablet");
			intent = new Intent(this, MainActivityTablet.class);

		}
		startActivity(intent);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.launcher, menu);
		return true;
	}


	public boolean isTablet(Context context) {
	    boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
	    boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
	    return (xlarge || large);
	}
	
}
