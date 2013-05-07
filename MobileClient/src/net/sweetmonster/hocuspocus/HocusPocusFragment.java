package net.sweetmonster.hocuspocus;

import java.util.Vector;

import net.patchingzone.ru4real.R;
import net.sweetmonster.hocuspocus.Hocuspocus.QQ;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class HocusPocusFragment extends Fragment {

	LinearLayout ll;

	View v;
	LinearLayout l;
	TextView mProgressText;
	TextView mTrackingText;
	Context c;

	Hocuspocus hocusPocus;
	Prueba2 p2;

	protected boolean isShown = true;
	

	public HocusPocusFragment() {

		hocusPocus = new Hocuspocus();
		hocusPocus.addObject(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Log.d("qq", "onactivity");

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		c = getActivity();
		v = inflater.inflate(R.layout.hocus_pocus_main, null);

		Log.d("qq", "createview");

		init();

		Log.d("qq", "despues de init");

		return v;

	}

	public void init() {

		Log.d("qq", "init");

		p2 = new Prueba2();
		hocusPocus.addObject(p2);

		Vector<QQ> q = hocusPocus.getData();

		final ScrollView scrollView = (ScrollView) v.findViewById(R.id.hocuspocus_main);
		Button button = (Button) v.findViewById(R.id.hocuspocus_button);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isShown  == true) {
					Log.d("qq", "gone"); 
					scrollView.setVisibility(View.GONE);
					isShown = false;
				} else {
					Log.d("qq", "visible"); 
					scrollView.setVisibility(View.VISIBLE);
					isShown = true;
				}
			}
		});

		l = (LinearLayout) v.findViewById(R.id.ll);

		for (QQ qq : q) {

			Log.d("qqm", "method " + qq.method);
			Log.d("qqm", "name " + qq.name);

			if (qq.method == null) {
				Log.d("qqm", "addSlider");
				addSlider(qq);
			} else {
				Log.d("qqm", "addMethod");
				addButton(qq);
			}
		}

		ll = new LinearLayout(getActivity());
		ll.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

	}

	public void addSlider(final QQ qq) {

		MSlider mSlider = new MSlider(getActivity());
		mSlider.setProperties(qq.name, qq.min, (int) qq.max);
		l.addView(mSlider);
		// mSlider.animate().x(50f).y(10f);

		mSlider.setSliderListerner(new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// Log.d("qq", "" + progress);
				hocusPocus.setValue(qq.obj, qq.attr, progress);
				p2.showValues();
			}

		});
	}

	public void addButton(final QQ qq) {

		MButton mButton = new MButton(c);
		mButton.setName(qq.name);

		mButton.addEventListener(new OnClickListener() {

			String methodName = "";

			@Override
			public void onClick(View v) {
				Log.d("qqm", "boton " + qq.obj + " " + qq.method);
				hocusPocus.callMethod(qq.obj, qq.method);
			}
		});

		l.addView(mButton);

	}

	private void clearUI() {
		l.removeAllViews();

	}

	public Hocuspocus getHocusPocus() {

		return hocusPocus;

	}

}
