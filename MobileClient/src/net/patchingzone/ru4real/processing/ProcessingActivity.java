package net.patchingzone.ru4real.processing;

import net.patchingzone.ru4real.R;
import net.patchingzone.ru4real.base.BaseActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

@SuppressLint("NewApi")
public class ProcessingActivity extends BaseActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainforfragments);

		//addProcessingSketch(new ProcessingSketch(), R.id.f1);

	}

	public void addProcessingSketch(Fragment processingSketch, int fragmentPosition) {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(fragmentPosition, processingSketch);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();

	}

}
