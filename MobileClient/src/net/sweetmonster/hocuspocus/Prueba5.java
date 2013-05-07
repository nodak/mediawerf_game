package net.sweetmonster.hocuspocus;

import net.sweetmonster.hocuspocus.annotation.HocusPocus;



public class Prueba5 {

	@HocusPocus (min = 0, max = 255)
	float var1;
	
	@HocusPocus
	float var2;

	public Prueba5() {
		var1 = 1; 
		var2 = 2; 
		
	} 
	
	public void showValues() { 
		Utils.logD("values are " + var1 + " " + var2); 
		
	}
}
