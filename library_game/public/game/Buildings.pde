class CityScape {

  ArrayList<Line> l; 


  CityScape() { 
    l = new ArrayList<Line>();
  } 

  public void draw() {
    
  } 

  public void create() {

  }

  public void load() {
  }
} 


class Line {
  PVector init; 
  PVector end; 
  int w = 1; 
  color c = color(255);

  Line() {
    init = new PVector(); 
    end = new PVector();
  } 
  
  public void draw() {
    stroke(c); 
    strokeWeight(w);
    //debug(init.x + " " + init.y + " " + end.x + " " + end.y);
    line(init.x, init.y, end.x, end.y);
  } 
  
  public void setInit(int x, int y) {
    init.x = x; 
    init.y = y;
  } 
  
  public void setEnd(int x, int y) {
    end.x = x; 
    end.y = y;
  } 
  
  public void setWeight(int w) {
    this.w = w;
    
  } 
  
  public void setColor(color c) {
    this.c = c;
  } 
  
} 

