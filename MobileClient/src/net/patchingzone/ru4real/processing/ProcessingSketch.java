package net.patchingzone.ru4real.processing;

import java.util.HashMap;

import net.patchingzone.ru4real.MainActivityPhone;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

public class ProcessingSketch extends PApplet {

	HashMap<Integer, Boolean> scores;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	PFont t;
	PImage image;

	private int cStatus = color(0, 255, 0);
	private int score;
	private int totalTargets = 13;

	public void setup() {
		frameRate(28);
		smooth();
		t = createFont("AdventPro-SemiBold", 48);
		scores = new HashMap<Integer, Boolean>();

		//setScore(1, 1, 13);
		//setScore(2, 5, 13);
	}

	public void draw() { // background(255);

		if (frameCount == 1) {
			Looper.prepare();
		}

		background(0);

		colorMode(RGB);
		// fill(100 + 155 * abs(sin((float) (frameCount * 0.02))), 255);
		// ellipse(width / 2, 260, 450, 450);

		// num places discovered
		// TWO_PI / numTotal
		// show n
		int slices = totalTargets;
		float sliceSize = TWO_PI / totalTargets;
		float w_2 = width / 2;
		float h_ = 500;

		for (int i = 0; i < slices; i++) {
			// color
			colorMode(HSB, 360);
			fill(map(i, 0, slices, 0, 360), 225, 225);
			arc(w_2 + 8, 255, 450, 450, sliceSize * i, sliceSize * (i + 1));

			// black part that moves
			colorMode(RGB, 255);
			fill(0);

			float m = 0;

			if (scores.containsKey(i)) {
				m = 225;
			} else {
				m = 0 ;
			}
			m = m + 70 + 30 * sin((float) (frameCount * 0.1 + i));				
			arc(w_2 + 8, 255, 450 - m, 450 - m, (sliceSize * i) - 0.1f, sliceSize * (i + 1) + 0.1f);
			// ellipse(w_2, h_, 80, 80);
		}

		// fill(255 * sin((float)(frameCount * 0.01)), 255, 255);
		fill(cStatus);
		// ellipse(width / 2, 600, 270, 270);
		float r = sqrt(mouseX * mouseX + mouseY + mouseY);
		//float theta = atan2(mouseY - 600 + 8, mouseX - w_2);
		float theta = TWO_PI;
		// Log.d("vol", "" + theta);
		// float vol = map();
		arc(w_2 + 2, 613, 270, 270, 0, theta);

		fill(0);
		text("" + theta, w_2, 600);
		ellipse(w_2 + 1, 613, 180, 180);

		if (image != null) {
			image(image, 0, 0);

		}
	}

	public void mouseReleased() {
		//net.patchingzone.ru4real.fragments.Utils.vibrate(getActivity(), 200);
		MainActivityPhone activityPhone = (MainActivityPhone) getActivity();
		if (mouseY < height / 2) {
			// activityPhone.network.sendAnswer(true);
		} else {
			// activityPhone.network.sendAnswer(false);
		}
	}

	public void connected(boolean b) {

	}

	public void gpsLock(boolean b) {
		if (b)
			cStatus = color(255, 0, 0);
		else
			cStatus = color(0, 0, 0);
	}

	public void changeImage(String string) {
		image = loadImage(string);
	}

	public void setScore(int score, int targetIndex, int totalTargets) {
		this.totalTargets = totalTargets;
		this.score = score;
		scores.put(targetIndex, true);
	}

}
