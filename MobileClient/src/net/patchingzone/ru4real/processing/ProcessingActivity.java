package net.patchingzone.ru4real.processing;

import net.patchingzone.ru4real.Credits;
import net.patchingzone.ru4real.R;
import net.patchingzone.ru4real.audio.DebugSoundFragment;
import net.patchingzone.ru4real.base.BaseActivity;
import net.patchingzone.ru4real.base.MapCustomFragment;
import net.patchingzone.ru4real.fragments.CameraFragment;
import net.patchingzone.ru4real.fragments.GameWebViewFragment;
import net.patchingzone.ru4real.fragments.MainFragment;
import net.patchingzone.ru4real.fragments.VideoPlayerFragment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

@SuppressLint("NewApi")
public class ProcessingActivity extends BaseActivity {

	private static final String TAG = "FragmentHolder";
	private static final int MENU_WEBVIEW = 0;
	private static final int MENU_CAMERA = 1;
	private static final int MENU_WALKIE_TALKIE = 2;
	private static final int MENU_MAP = 3;
	private static final int MENU_PROCESSING = 4;
	private static final int MENU_MAIN = 5;
	private static final int MENU_PD = 6;
	private static final int MENU_VIDEO = 7;
	private static final int MENU_WEBVIEW_CREDITS = 8;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainforfragments);

		// addProcessingSketch(new ProcessingSketch(), R.id.f1);
		// addProcessingSketch(new MapCustomFragment(), R.id.f1);
		// addProcessingSketch(new VideoPlayerFragment(), R.id.f1);

		// addProcessingSketch(cameraFragment, R.id.f1);

		addProcessingSketch(new MainFragment(), R.id.f1);

	}

	public void changeFragment(int id, Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		fragmentTransaction.replace(id, fragment);
		fragmentTransaction.commit();
	}

	/*
	 * public void changeFragment(Fragment newFragment, int fragmentID, boolean
	 * addBackStack) {
	 * 
	 * Log.d(TAG, "cambiado");
	 * 
	 * FrameLayout _frameLayout = (FrameLayout) findViewById(fragmentID);
	 * _frameLayout.setVisibility(View.VISIBLE); FragmentTransaction ft =
	 * getSupportFragmentManager().beginTransaction(); if (addBackStack ==
	 * false) { ft.add(id, newFragment);
	 * ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); //
	 * ft.addToBackStack(null); } else { ft.replace(id, newFragment);
	 * ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	 * ft.addToBackStack(null); } ft.commit();
	 * 
	 * }
	 */

	public void addProcessingSketch(Fragment processingSketch, int fragmentPosition) {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(fragmentPosition, processingSketch);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, MENU_WEBVIEW, 0, "Webview");
		menu.add(0, MENU_CAMERA, 0, "Camera");
		menu.add(0, MENU_WALKIE_TALKIE, 0, "Walkie Talkie");
		menu.add(0, MENU_MAP, 0, "Map");
		menu.add(0, MENU_PROCESSING, 0, "Processing");
		menu.add(0, MENU_MAIN, 0, "Main");
		menu.add(0, MENU_PD, 0, "PD");
		menu.add(0, MENU_WEBVIEW_CREDITS, 0, "Webview Credits");

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case MENU_WEBVIEW:
			changeFragment(R.id.f1, new GameWebViewFragment());

			return true;
			
		case MENU_WEBVIEW_CREDITS:
			changeFragment(R.id.f1, new Credits());
			
			return true;


		case MENU_CAMERA:

			CameraFragment cameraFragment = new CameraFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("color", CameraFragment.MODE_COLOR_COLOR);
			bundle.putInt("camera", CameraFragment.MODE_CAMERA_BACK);
			cameraFragment.setArguments(bundle);

			changeFragment(R.id.f1, cameraFragment);

			return true;

		case MENU_WALKIE_TALKIE:

			return true;

		case MENU_MAP:

			changeFragment(R.id.f1, new MapCustomFragment());

			return true;

		case MENU_PROCESSING:
			changeFragment(R.id.f1, new ProcessingSketch());

			return true;

		case MENU_MAIN:
			changeFragment(R.id.f1, new MainFragment());

			return true;

		case MENU_VIDEO:
			changeFragment(R.id.f1, new VideoPlayerFragment());

			return true;

		case MENU_PD:
			changeFragment(R.id.f1, new DebugSoundFragment());

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
