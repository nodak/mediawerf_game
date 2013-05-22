/* @pjs transparent="true"; */ //this adds transparency to the sketch 

/* 
 * Energy Vampire Processing 
 * 
 * 
 * ideas: play sound when touching a building? 
 * TODO: we need a refactor! :)
 *
 */


int youX, youY;        // x,y position of "you"
int youSize = 100;      // assumes a circle - elliptical collision is VERY complicated
int speed = 5;
int playerNr = 10;

int ballX, ballY;      // x,y position of the ball - will be randomly placed in the setup
int ballSize = 100;    // assumes a circle - elliptical collision is VERY complicated


ArrayList<Player> players;
ArrayList<Message> messages;
PImage hintLeft;
PImage hintRight;
PImage hintUp;
PImage hintDown;
PImage hintQuestion;
PImage hintNo;

 
public CityScape city; 


void setup() {  
  // BASIC SETUP STUFF
  size($(document).width(), $(document).height());
  smooth();
  noStroke();

  hintLeft = loadImage("./game/data/hint_left.png");
  hintRight = loadImage("./game/data/hint_right.png");
  hintUp = loadImage("./game/data/hint_up.png");
  hintDown = loadImage("./game/data/hint_down.png");
  hintQuestion = loadImage("./game/data/hint_question.png");
  hintNo = loadImage("./game/data/hint_no.png");

  players = new ArrayList();
  messages = new ArrayList();

  city = new CityScape(); 
  city.load(); 


}

void draw() { 
 // stats.begin(); 

  background(0, 0, 0, 0); //transparent 
  city.drawBG(); 

  /*
  stroke(85);
  for (int i = 0; i < 1000; i++) {
    point(random(width), random(height));
  }
  */


 if (players.size() > 0) {
   // city.drawConnections(players.get(0).toX, players.get(0).toY);
  }

  //players 
  for (int i = 0; i < players.size(); i++) {
    Player p = players.get(i);
    //p.drawSpeechBubble("lalalal");
    //p.setPosition(mouseX, mouseY);
    p.draw();
  }

  city.drawBuildings(); 
 
  city.drawCityObjects();

  //messages 
  for (int i = 0; i < messages.size(); i++) {
    Message m = messages.get(i);
    m.draw();
  }

 // stats.end(); 

}

void mouseDragged() {
  debug("hola"); 
} 

void mousePressed() {

} 

void mouseReleased() {
  debug("Message created");
  messages.add(new Message("hola", mouseX, mouseY, random(360)));

} 


void keyPressed() {
  
  if (key == 'a') {
  	addPlayer();
  }
  if (key == 'r') {
    debug("Remove");
    if (players.size() >= 1) {
      //players.remove(p);
    }
  } 

  if (key == 'p') {
    console.log("p pressed");
    city.save(); 
  }
 
}

debugOut = true;
debugConsole = true;

public void addPlayer(String id, int c) {
  debug("Add");
  //Player p = new Player(random(width), random(height), 20, 100, color(204, 153, 0, 125), color(255, 0, 0, 125));

	colorMode(HSB, 360);
	Player p = new Player(id, random(width), random(height), 20, 50, color(c, 255, 255, 125));
	colorMode(RGB);
	players.add(p);

}

public void removePlayer(id) {
	debug("Remove player");
	
	for(int i = 0; i < players.size(); i++) {
		if(players.get(i).id == id) { 
			players.remove(i);
		}
	}

} 

public void targetRemoved(id) {
  debug("Remove target");
  city.co.targetRemoved(id);
}


public void addTarget(id, value, type, x, y, lat, lon, range) {
  city.co.add(new CityObject(id, value, type, x, y, lat, lon, range));
}


public void setPosition(String id, double lat, double lon, float orientation) {
	
	//google maps library will do the job to translate to XY coordinates 
  div =	gmap.getXYCoordinates(new google.maps.LatLng(lat, lon)); 
	
	int x = div.x; 
	int y = div.y; 
		
	for (int i = 0; i < players.size(); i++) {
    Player p = players.get(i);
		
		if(p.id == id) {
			debug(p.id); 
	  	p.setPosition(x, y);
      p.setOrientation(orientation);

		}
  }
} 


public void setPositionXY(String id, int x, int y, float orientation, latlon, int action) {

	for (int i = 0; i < players.size(); i++) {
    Player p = players.get(i);
		
		if(p.id == id) {
			debug(p.id); 
	  	p.setPosition(x, y);   
      p.setOrientation(orientation);
      p.manageAction(city, city.checkHints(p), latlon, action);
		}
		
  }
} 


public void setOrientation(String id, float orientation) {

    for (int i = 0; i < players.size(); i++) {
      Player p = players.get(i);
    
     if(p.id == id) {
        debug(p.id); 
        p.setOrientation(orientation);
      }
     
  }
} 

public void updateSounds(id, vol) { 
  city.co.updateSound(id, vol);
}

public void placeObject() {


}

//just a little helper function
//aparently processing.js shows a console that we might not want
//so its better sometimes to use console.log
public void debug(String str) {
	if(debugOut) {
		if(debugConsole) {
			console.log(str);
		} else {
			println(str);
		}
	
	}

}