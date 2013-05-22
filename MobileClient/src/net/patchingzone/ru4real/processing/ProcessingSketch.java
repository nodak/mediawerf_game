package net.patchingzone.ru4real.processing;

import net.patchingzone.ru4real.MainActivityPhone;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

public class ProcessingSketch extends PApplet {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	PFont t;
	PImage image;

	private int cStatus = color(0, 255, 0);

	public void setup() {
		frameRate(28);
		smooth();
		t = createFont("AdventPro-SemiBold", 48);

	}

	public void draw() { // background(255);

		if (frameCount == 1) {
			Looper.prepare();
		}

		background(0);

		colorMode(RGB);
		//fill(100 + 155 * abs(sin((float) (frameCount * 0.02))), 255);
		//ellipse(width / 2, 260, 450, 450);

	
		//num places discovered 
		//TWO_PI / numTotal
		//show n 
		int slices = 10; 
		float sliceSize = TWO_PI / 10;
		float w_2 = width / 2;
		float h_ = 500;
		for (int i = 0; i < slices ; i++) {
			colorMode(HSB, 360); 
			fill(map(i, 0, slices, 0, 360), 225, 225); 
			arc(w_2 + 8, 255, 450, 450, sliceSize * i, sliceSize * (i + 1));
		
			colorMode(RGB, 255); 
			fill(0);
			float m = 70 + 60 * sin((float) (frameCount * 0.1 + i));
			arc(w_2 + 8, 255, 450 - m, 450 - m, sliceSize * i - 1, sliceSize * (i + 1) + 1);
			//ellipse(w_2, h_, 80, 80);
		}

		
		// fill(255 * sin((float)(frameCount * 0.01)), 255, 255);
		fill(cStatus);
		//ellipse(width / 2, 600, 270, 270);
		float r= sqrt(mouseX * mouseX + mouseY + mouseY);
		float theta = atan2(mouseY - 600 + 8, mouseX - w_2 ); 
		//Log.d("vol", "" + theta);
		//float vol = map();
		arc(w_2 + 2, 613, 270, 270, 0, theta);

		fill(0);
		text("" + theta, w_2, 600);
		ellipse(w_2 + 1, 613, 180, 180);
		
		if (image != null) {
			image(image, 0, 0);
			
		}
	}


	public void mouseReleased() {
		net.patchingzone.ru4real.fragments.Utils.vibrate(getActivity(), 200);
		MainActivityPhone activityPhone = (MainActivityPhone) getActivity();
		if (mouseY < height / 2) {
		//	activityPhone.network.sendAnswer(true);
		} else {
		//	activityPhone.network.sendAnswer(false);
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

}
