package net.patchingzone.ru4real.fragments;

import net.patchingzone.ru4real.MainActivityPhone;
import net.patchingzone.ru4real.R;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TextFragment extends Fragment {

	private View v;
	ImageView img;
	private LinearLayout ll;

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		v = inflater.inflate(R.layout.fragment_text, container, false);
		final MainActivityPhone activityPhone = (MainActivityPhone) getActivity();

		img = (ImageView) v.findViewById(R.id.img);
		ll = (LinearLayout) v.findViewById(R.id.ll);
		
		//ObjectAnimator alphaanimator = ObjectAnimator.ofFloat(img, View.ALPHA, 0);
		//alphaanimator.setRepeatCount(5);
		//alphaanimator.setRepeatMode(ValueAnimator.REVERSE);
		//alphaanimator.start();
		
		ObjectAnimator rotateanimator = ObjectAnimator.ofFloat(img, View.ROTATION, 360);
		rotateanimator.setRepeatCount(ValueAnimator.INFINITE);
		rotateanimator.setDuration(5000);
		rotateanimator.setInterpolator(new LinearInterpolator());
		rotateanimator.setRepeatMode(ValueAnimator.INFINITE);
		//rotateanimator.start();
		
		AnimatorSet setAnimation = new AnimatorSet();
		//setAnimation.play(alphaanimator);
		setAnimation.play(rotateanimator); //.after(alphaanimator);
		
		setAnimation.start();
		
		
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
	
	public void changeColor() {
		ll.setBackgroundResource(R.color.white);
	}

}
