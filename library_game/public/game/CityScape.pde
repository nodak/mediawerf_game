class CityScape {

  ArrayList<Building> b; 
  boolean editMode = false;
  Building currentBuilding; 
  float count = 0.0;
  float count2 = -width;
	boolean bgShow = false;


  CityScape() { 
    b = new ArrayList<Building>();
  } 

  public void drawBG() {
   
		if(debugGUI.showBG == true) {
 			//fill(abs(125 * sin(count)));
    	fill(0);
			noStroke();
    	rect(0, 0, width, height); 


    	count2 += 15;

    	if (count2 > height && frameCount % 260 == 0) {
      	count2 = -width;
    	} else {
      	fill(255);
      	for (int i = 0; i < 5; i++) {
        	pushMatrix();
        	translate(0, count2);
        	rotate(PI/4);
        	rect(0, 0, width * 2, 2);
        	popMatrix();
      }
    }
		} 
	}
	
	public void drawBuildings() {
    for (int i = 0; i < b.size(); i++) {
      b.get(i).draw();
    }

    count += 0.01;
  } 

  public void setEditMode(boolean m) {
    this.editMode = m;
  }

  public void newBuilding() {
    currentBuilding = new Building();
    b.add(currentBuilding);
  }

  public void endCurrentBuilding() {
    //currentBuilding.end();
  } 

  public void addPoint(int x, int y) {
    if (editMode == true) {
      currentBuilding.addPoint(x, y);
    }
  } 

  public void removeLastPoint() {
    currentBuilding.removeLastPoint();
  } 

  public void load() {
  }
} 


class Building {
  ArrayList <PVector> p; 

  boolean fading = true;
  float count = 0.0;
  int bg = 255; 
  int bgAlpha = 255; 
  int border = 255; 
  int borderAlpha = 255;

  Building () { 
    p = new ArrayList<PVector>();
  } 

  public void addPoint(int x, int y) {
    p.add(new PVector(x, y));
  } 

  public void removeLastPoint() {
    p.remove(p.size() - 1);
  } 


  public void draw() {
    if (fading) {
      bg = (int)(abs(255 * sin(count)));  
      count += 0.01;
			//println("hola");
    } 

    fill(bg); 
		strokeWeight(10);	 
    stroke(border, borderAlpha);	

		//fill(255);
    beginShape();   
    for (int i = 0; i < p.size(); i++) {
      vertex(p.get(i).x, p.get(i).y);
      //println(i);
    }
    endShape(CLOSE);
  }

  public void setFading(boolean fading) {
    this.fading = fading;
  } 

  public void resetColors() {
    this.bg = 255;
    this.bgAlpha = 255;
    this.border = 255;
    this.borderAlpha = 255;
  }
} 


void mouseReleased() {
  city.addPoint(mouseX, mouseY);
} 

void keyReleased() {
  if (key == 'q') {
    debug("new Building");
    city.newBuilding();
  } 
  else if (key == 'p') {
    city.removeLastPoint();
  } 
  else if (key == 'z') {
    city.setEditMode(true);
  } 
  else if (key == 'x') {
    city.setEditMode(false);
  }
} 
