package net.sweetmonster.hocuspocus;

import net.sweetmonster.hocuspocus.annotation.BadAnnotation;
import net.sweetmonster.hocuspocus.annotation.HocusPocus;
import net.sweetmonster.hocuspocus.annotation.HocusPocusMethod;



public class Prueba2 {

	float var8; 

	@HocusPocus (min = 0, max = 255)
	float var1;
	
	@HocusPocus
	float var2; 

	@BadAnnotation
	float var5; 

	public Prueba2() {
		var1 = 1; 
		var2 = 2; 
		var5 = 5; 
		var8 = 8; 
		
		//Prueba5 prueba5 = new Prueba5(); 
	} 
	
	@HocusPocusMethod
	public void showValues() { 
		Utils.logD("values are " + var1 + " " + var2); 
		
	}
}
