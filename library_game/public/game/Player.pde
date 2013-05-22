class Player {
  int STATUS_NORMAL = 0;
  int STATUS_CARRYING_SOUND = 1;
  int status = STATUS_NORMAL;
  String carryingSound = ""; 
  PImage hintImg; 

	String	 id; 
  String    t; 
  //float cw = textWidth(t);
  float     x;
  float     y;
  float     orientation = 0.0;

 	float     toX = 0.0;
  float     toY = 0.0;
  float     toOrientation = 0.0;

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
  //PImage overlay; 



  Player(String id, float x, float y, float finalPlayerSize, float finalEnergySize, color colorEnergy) {
		this(id, x, y, finalPlayerSize, finalEnergySize, color(0, 85), colorEnergy);
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
    //overlay = loadImage("./game/data/qq.png");
  }



  void draw() { 
    noStroke();
    colorMode(HSB, 360);
    fill(colorEnergy);
    triangle(0, 0, 0, 300, 400, 0);

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
      float q = map(i, positions.length, 0, 25, 0);
			ellipse(positions[i].x, positions[i].y, q, q);
		}  
		*/ 
		
		pushMatrix();
		
		translate(toX, toY);
		toX = lerp(toX, x, 0.02); 
		toY = lerp(toY, y, 0.02); 
		
    toOrientation = lerp(toOrientation, orientation, 0.02);
		rotate(toOrientation);
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
    //text(carryingSound, 0, 0);
    ellipse(0, 0, pS, pS);
    //direction pointer 
    fill(255, 125); 

    for(int i = 0; i < 4; i++) {
      arc(0, 0, pS, pS, -PI/12 + PI/2*i, PI/12 + PI/2*i);
    }

    fill(0);
    stroke(colorEnergy);
    ellipse(0, 0, pS - 7, pS - 7);
    rotate(-toOrientation);
    imageMode(CENTER);

    if(hintImg != null) {
      image(hintImg, 0, 0, pS - 5, pS - 5);
    }

    //arc(0, 0, pS, pS, PI * 1.25, PI * 1.75);
    //arc(0, 0, pS, pS, PI * 1.25, PI * 1.75);
    //arc(0, 0, pS, pS, PI * 1.25, PI * 1.75);


    if (showSpeechBubble) {
      drawSpeechBubble(msg);
    }
    
    incrIn = incrIn + 0.05;
    incrOut = incrOut + 0.02;

    //imageMode(CENTER)
    //image(overlay, 0, 0);

		popMatrix(); 
		
  } 

  void grabSound(CityScape city, CityObject o) {
    console.log(o);
    hintImg = o.hintImg; 
    o.displayMsg(true); 
    o.deleteSent = true;
    city.co.removeTarget(o.id);
    playerSize += 10;
    carryingSound = o.value; 
  }

  void placeSound(lat, lon) { 
    if (carryingSound != "") {
      console.log("placing sound");
      game.addTarget(lat, lon, carryingSound, 20, "hint"); 
      carryingSound = ""; 
      hintImg = null;
      playerSize -= 10;
    }
  }

  void setPosition(float x, float y) { 
    this.x = x;
    this.y = y;
		positions.push(new PVector(x, y));
  } 

  void manageAction(CityScape city, CityObject o, latlon, int action) {

      if (action == 1 && carryingSound != "") {
        console.log("place sound");
        placeSound(latlon.lat, latlon.lon); 
      } else if (action == 1 && carryingSound == "") {
        console.log("carry sound");
        grabSound(city, o);
        //city.checkaction = 2;
      }
      action = 0;
  }

  void setOrientation(float orientation) { 
    this.orientation = radians(orientation);
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

