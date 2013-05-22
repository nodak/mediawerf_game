class CityObjects {
	ArrayList<CityObject> l; 

	CityObjects() {
		l = new ArrayList<CityObject>();
	}

	public void draw() {
 		for (int i = 0; i < l.size(); i++) {
  		    l.get(i).draw();
  		    l.get(i).drawVolumes(8 * i, 50, 5, l.get(i).volume);
 	  	} 
	}

	public void add(CityObject co) {
		l.add(co);
	} 

	public void updateSound(id, vol) {
		for (int i = 0; i < l.size(); i++) {
  		    if (l.get(i).id == id) {
  		    	l.get(i).volume = vol;
  		    	break;
  		    }
 	  	} 
	} 

	public void removeTarget(id) {

		$.each(game.targets, function(k, v) { 
			if (v._id == id) {
				console.log("removing!");
				game.removeTarget(v); 
			}
		});


	} 

	public void targetRemoved(id) {
		//console.log("removed " + " "+ id);
		for (int i = 0; i < l.size(); i++) {
  		    if (l.get(i).id == id) {
  		    	console.log("OK!");
  		    	l.remove(i);
  		    	break;
  		    }
 	  	} 
	}
}

class CityObject {
	String TYPE_SOUNDSCAPE = "normal";
	String TYPE_HINT = "hint";
	String TYPE_OBJECTIVE = "objective";
	int type; 
	String id;
	PVector pos;
	double lat;
	double lon;   
	float rangePixels;
	float rangeMeters;
	float volume;
	int h; 
	int deleteSent = false;
	String value;
	float t = 0;
	float thint = 0; 
	PImage hintImg; 
	boolean showMsg = false;

	CityObject(id, value, type, x, y, lat, lon, range) { 
		this.type = type;
		this.id = id; 

		var myvalue = value.split('/'); //qq5[qq5.length -1 ]
		this.value = myvalue[myvalue.length -1 ];
		this.pos = new PVector(x, y); 
		this.lat = lat;
		this.lon = lon; 
		this.rangePixels = range * gmap.distancePerMeter;
		this.rangeMeters = range;
		this.h = int(random(360));
		t = random();
		thint = random();

		if (type == TYPE_HINT) {
			switch("up") {
				case "up":
					hintImg = hintUp;
					break;

				default :
					
				break;	
			}
		}

		//console.log(range);
		//console.log(gmap.distancePerMeter);

		//console.log("position " + pos.x + " " + pos.y + " " + this.rangeMeters + " " + this.rangePixels);
	} 

	public void draw() {
		fill(155, 255);
		stroke(255);

		//console.log("lalala");
		if (type == "normal") {
			noFill();

			stroke(255, map(t, 0, 1, 85, 0));
			float nSize = map(t, 0, 1, 0, 100);
			ellipse(pos.x, pos.y, nSize, nSize);


		} else if (type == TYPE_HINT) { 

			imageMode(CENTER); 
			pushMatrix();
			translate(pos.x, pos.y);
			rotate(0 + (PI / 8) * sin(thint));
			image(hintImg, 0, 0);
			popMatrix();
			thint = thint + 0.1;

			fill(0); 

			if (showMsg == true) {
				text("hola", pos.x, pos.y);
			}

			//rectMode(CENTER);
			//fill(h, 225, 225); 
			//rect(pos.x, pos.y, rangePixels * sin(frameCount * 0.1 + pos.x), rangePixels * sin(frameCount * 0.1 + pos.x));
		} else if (type == TYPE_OBJECTIVE) {
			rectMode(CENTER);
			fill(h, 225, 225); 
			rect(pos.x, pos.y, rangePixels * sin(frameCount * 0.1 + pos.x) + 20, rangePixels * sin(frameCount * 0.1 + pos.x));
		} 
		t = t + 0.005; 
		if (t > 1) t = 0;
	} 

	public void displayMsg(boolean b) {
		showMsg = b;
	}

	public void drawVolumes(x, y, w, h) {
		int posx = width - x - 50; 
		int posy = height - 100 - y;
		rectMode(CORNER);
		rect(posx, posy, w, -h);
		pushMatrix();
		translate(posx, posy);
		rotate(PI / 2);
		textSize(10);
		qq2 = value;
		text(value, 5, 0);
		popMatrix();

	}


}