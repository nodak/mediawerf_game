class CityObjects {
	ArrayList<CityObject> l; 

	CityObjects() {
		l = new ArrayList<CityObject>();
	}

	public void draw() {
 		for (int i = 0; i < l.size(); i++) {
  		    l.get(i).draw();
 	  	}
	}

	public void add(CityObject co) {
		l.add(co);
	}
}

class CityObject {
	int type; 
	String id;
	PVector pos;
	double lat;
	double lon;   
	float rangePixels;
	float rangeMeters;


	CityObject(type, id, x, y, lat, lon, range) { 
		this.type = type;
		this.id = id;
		this.pos = new PVector(x, y); 
		this.lat = lat;
		this.lon = lon; 
		this.rangePixels = range * gmap.distancePerMeter;
		this.rangeMeters = range;

		//console.log("position " + pos.x + " " + pos.y + " " + this.rangeMeters + " " + this.rangePixels);

	} 

	public void draw() {
		fill(155, 255);
		stroke(255);
		ellipse(pos.x, pos.y, rangePixels * sin(frameCount * 0.1 + pos.x), rangePixels * sin(frameCount * 0.1 + pos.x));

	}


}