package net.patchingzone.ru4real;

import net.patchingzone.ru4real.R;
import net.patchingzone.ru4real.audio.DebugSoundFragment;
import net.patchingzone.ru4real.base.BaseActivity;
import net.patchingzone.ru4real.fragments.CameraFragment;
import net.patchingzone.ru4real.fragments.GameWebViewFragment;
import net.patchingzone.ru4real.fragments.MainFragment;
import net.patchingzone.ru4real.fragments.MapCustomFragment;
import net.patchingzone.ru4real.fragments.VideoPlayerFragment;
import net.patchingzone.ru4real.processing.ProcessingSketch;
import net.patchingzone.ru4real.walkietalkie.WalkieTalkieFragment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

@SuppressLint("NewApi")
public class MainActivityTablet extends BaseActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forfragments_tablet);

		// addProcessingSketch(new ProcessingSketch(), R.id.f1);
		// addProcessingSketch(new VideoPlayerFragment(), R.id.f1);

		// addProcessingSketch(cameraFragment, R.id.f1);

		// addProcessingSketch(new MainFragment(), R.id.f1);
		addProcessingSketch(new MapCustomFragment(), R.id.map);
		addProcessingSketch(new DebugSoundFragment(), R.id.fragmentSound);
		addProcessingSketch(new WalkieTalkieFragment(), R.id.f2);
		// addProcessingSketch(new ProcessingSketch(), R.id.fragmentProcessing);

		OverlayLogger ol = new OverlayLogger();
		L.addLoggerWindow(ol);
		L.filterByTag("NETWORK");
		addProcessingSketch(ol, R.id.f1);

	}

}
