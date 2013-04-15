package net.patchingzone.ru4real.processing;

import processing.core.PApplet;

public class Utils {

	PApplet p; 
	
	public static void corona(ProcessingSketch p, float innerRad, float outerRad, float angleinicial, float anglefinal, int pts) {
		float rot = (anglefinal - angleinicial) / pts;
		float q = p.map(p.mouseX, 0, p.width, 0, 10);

		p.beginShape(p.TRIANGLE_STRIP);
		for (int i = 0; i <= pts; i++) {
			float px = p.cos(p.radians(angleinicial)) * outerRad + p.random(q);
			float py = p.sin(p.radians(angleinicial)) * outerRad + p.random(q);
			// angle += rot;
			p.vertex(px, py);
			px = p.cos(p.radians(angleinicial)) * innerRad + p.random(q);
			py = p.sin(p.radians(angleinicial)) * innerRad + p.random(q);
			p.vertex(px, py);
			angleinicial += rot;
		}
		p.endShape();
	}

}
