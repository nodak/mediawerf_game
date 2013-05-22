package net.patchingzone.ru4real.fragments;

import net.patchingzone.ru4real.MainActivityPhone;
import net.patchingzone.ru4real.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class YesNoFragment extends Fragment {

	private View v;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		v = inflater.inflate(R.layout.fragment_yes_no, container, false);
		final MainActivityPhone activityPhone = (MainActivityPhone) getActivity();

		Button btnYes = (Button) v.findViewById(R.id.buttonYes);
		btnYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				net.patchingzone.ru4real.fragments.Utils.vibrate(getActivity(), 200);
				//activityPhone.network.sendAnswer(true);
			}
		});

		Button btnNo = (Button) v.findViewById(R.id.buttonNo);
		btnNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				net.patchingzone.ru4real.fragments.Utils.vibrate(getActivity(), 200);
				//activityPhone.network.sendAnswer(false);
			}
		});

		return v;

	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {

		}
		return true;
	}

}
