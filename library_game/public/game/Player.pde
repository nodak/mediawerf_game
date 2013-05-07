class Player {
	String	 id; 
  String    t; 
  //float cw = textWidth(t);
  float     x;
  float     y;
 	float     toX = 0.0;
  float     toY = 0.0;
	float 		orientation = 0.0;
  
  float     finalPlayerSize;
  float     finalEnergySize; 
  float     playerSize = 0.0;
  float     energySize = 0.0;
  color     colorEnergy;
  color     colorPlayer;
  int       team;
  float     breathOutSize; 
  float     breathInSize; 
  float     incrIn = 0.0;
  float     incrOut = 0.0;
  boolean   showSpeechBubble = false;
  String    msg = "prueba";
  int       NUM_CIRCLES = 3; 
	PVector 	positions = []; 


  Player(String id, float x, float y, float finalPlayerSize, float finalEnergySize, color colorEnergy) {
		this(id, x, y, finalPlayerSize, finalEnergySize, color(0, 125), colorEnergy);
  }

  Player(String id, float x, float y, float finalPlayerSize, float finalEnergySize, color colorPlayer, color colorEnergy) {
   	this.id = id; 
 		this.x = x;
    this.y = y;
    this.finalPlayerSize = finalPlayerSize;
    this.finalEnergySize = finalEnergySize;
    this.colorPlayer = colorPlayer;
    this.colorEnergy = colorEnergy;
		//positions = new PVector();
  }



  void draw() { 
    noStroke();
    fill(colorEnergy);
    
    //when one player is created 
    if (energySize < finalEnergySize) {
      energySize = energySize + 2;
    } 
    if (playerSize < finalPlayerSize) {
      playerSize = playerSize + 2;
    } 
    
    //when one player is destroyed
    //TODO
    //if (byebye) {
      //energySize 
    //} 

		//TODO trail  
		/*
		for(int i = 0; i < positions.length; i++) {
			ellipse(positions[i].x, positions[i].y, 25, 25);
		}  
		*/ 
		
		pushMatrix();
		
		translate(toX, toY);
		toX = lerp(toX, x, 0.01); 
		toY = lerp(toY, y, 0.01); 
		
		rotate(orientation);
 		colorMode(HSB, 360);
    fill(colorEnergy);
    //nimbus 
    for (int i = 0; i < NUM_CIRCLES; i++) {
      //make the circle breath (pulsate) a bit 
      breathOutSize = 4 * sin(incrOut + i * 1); 
      breathInSize = 12 * sin(incrIn + i * 1); 
      ellipse(0, 0, energySize + breathInSize, energySize + breathInSize);
    }
		colorMode(RGB);

    //person
		fill(colorPlayer);
    float pS = playerSize + breathOutSize;
    ellipse(0, 0, pS, pS);
    //direction pointer 
    fill(255, 125); 
    arc(0, 0, pS, pS, PI * 1.25, PI * 1.75);
    
    if (showSpeechBubble) {
      drawSpeechBubble(msg);
    }
    
    incrIn = incrIn + 0.05;
    incrOut = incrOut + 0.02;
		popMatrix(); 
		
  }

  void setPosition(float x, float y) { 
    this.x = x;
    this.y = y;
		positions.push(new PVector(x, y));
  } 

  void setPosition(float orientation) { 
    this.orientation = orientation;
  }

  float getPositionX() {
    return this.x;
  }
  float getPositionY() {
    return this.y;
  } 
  float getPlayerSize() {
    return this.playerSize;
  }
  
  void setPlayerSize(float playerSize) {
    this.playerSize = playerSize;
  }
  float getEnergySize() {
    return this.energySize;
  }
  
  void setEnergySize(float energySize) {
    this.energySize = energySize;
  }
  color getColorPlayer() {
    return this.colorEnergy;
  }
  
  void setColorPlayer(color colorPlayer) {
    this.colorPlayer = colorPlayer;
  }
  
  color getColorEnergy() {
    return this.colorEnergy;
  }
  
  void setColorEnergy(color colorEnergy) {
    this.colorEnergy = colorEnergy;
  }
  void setTeam(int team){
    this.team = team; 
  }
  
  void setMessage(String msg) {
    this.msg = msg;  
  } 
  
  void setShowSpeechBubble(boolean b) { 
    this.showSpeechBubble = b;
  } 
 

  /* 
   BALL/BALL COLLISION FUNCTION
   Takes 6 arguments:
   + x,y position of the first ball - in this case "you"
   + diameter of first ball - elliptical collision is VERY difficult
   + x,y position of the second ball
   + diameter of second ball
   */

  boolean ballBall(int x1, int y1, int d1, int x2, int y2, int d2) {

    // find distance between the two objects
    float xDist = x1-x2;                                   // distance horiz
    float yDist = y1-y2;                                   // distance vert
    float distance = sqrt((xDist*xDist) + (yDist*yDist));  // diagonal distance

    // test for collision
    if (d1/2 + d2/2 > distance) {
      return true;    // if a hit, return true
    }
    else {            // if not, return false
      return false;
    }
  } 
  
  public void drawSpeechBubble(String t) {
    pushMatrix();
    translate(x-40, y-135);
    
    fill(255, 225);
    stroke(0);
    //rect(30, 20, 105, 55);
    beginShape();
    vertex(30, 20);
    vertex(125, 20);
    vertex(125, 75);
    
    vertex(50,75);
    vertex(40,90);
    vertex(30, 75);
    endShape(CLOSE);

		noStroke();
    fill(0);
    //debug(cw);
  
    text(t,55,55);
    fill(255,0,0);
   
    popMatrix(); 
  }
}

