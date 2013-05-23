class Message {

  String msg; 
  float posX; 
  float posY; 
	float rotation; 
  int lastTime; 
  int TIMER = 1000;
  boolean shake = true; 
  float incr = 0.0; 


  Message(String msg, float posX, float posY, float rotation) {
    this.msg = msg; 
    this.posX = posX;
    this.posY = posY;
		this.rotation = rotation;
    lastTime = millis();
    incr = 55; 
  } 

  void draw() {

    //start timer
    //if (millis() > lastTime + TIMER) {
    //  shake = true;
    //  lastTime = millis();
    //  incr = 55; 
    //} 

    if (shake) {
      posX = posX + 25 * sin(incr);
      //posY = ; 
      debug("hola " + posX + " " + incr);

      if (incr > 0.1) {
        shake = false;
        incr = incr - 0.1;
      }
    } 


		pushMatrix();
		translate(posX, posY);
		rotate(radians(rotation));
    fill(0, 125);
		rect(0, 0, 50, 20, 2);
		fill(255);
 		text(msg, 5, 15);
		popMatrix();
  }
} 

