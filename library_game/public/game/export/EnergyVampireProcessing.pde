/* @pjs transparent="true"; */


/* 
 * Energy Vampire Processing 
 * 
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

//Team teamRed;

ArrayList<Player> teamRed = new ArrayList<Player>();
ArrayList<Player> teamBlue = new ArrayList<Player>();
 


void setup() {  
  // BASIC SETUP STUFF
  size(600, 800);
  smooth();
  noStroke();

  players = new ArrayList();
  messages = new ArrayList();

  //teamRed = new Team();
}

void draw() {
  background(0, 0, 0, 0); //transparent 
  //p1.draw();

  //players 
  for (int i = 0; i < players.size(); i++) {
    Player p = players.get(i);
    p.setPosition(mouseX, mouseY);
    p.draw();
  }

  //messages 
  for (int i = 0; i < messages.size(); i++) {
    Message m = messages.get(i);
    m.draw();
  }

  l.draw();
}

Line l = new Line(); 

void mouseDragged() {
  println("hola"); 


} 

void mousePressed() {
  l.setInit(mouseX, mouseY);
} 

void mouseReleased() {
  println("Message created");
  messages.add(new Message("hola", mouseX, mouseY));
    l.setEnd(mouseX, mouseY);

} 


void keyPressed() {
  
  if (key == 'a') {
    println("Add");
    Player p = new Player(random(width), random(height), 20, 100, color(204, 153, 0, 125), color(255, 0, 0, 125));
    players.add(p);
  }
  if (key == 'r') {
    println("Remove");
    if (players.size() >= 1) {
      //players.remove(p);
    }
  }
  //println(players);
  if (players.size() == playerNr) {

    //teamRed.addPlayers(players.subList(0, players.size() / 2 + players.size() % 2));

    teamRed.addAll(players.subList(0, players.size() / 2 + players.size() % 2));
    for (int i = 0; i < teamRed.size(); i++)
    {
      teamRed.get(i).setTeam(1);
      teamRed.get(i).setColorPlayer(color(255, 255, 255));
    }

    teamBlue.addAll(players.subList(players.size() / 2 + players.size()%2, players.size()));
    for (int i = 0; i < teamBlue.size(); i++)
    {
      teamBlue.get(i).setTeam(1);
      teamBlue.get(i).setColorPlayer(color(0, 255, 0));
    }


    println(teamRed);
    //println(teamBlue);
  }
}

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
    println(init.x + " " + init.y + " " + end.x + " " + end.y);
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

class Message {

  String msg; 
  float posX; 
  float posY; 
  int lastTime; 
  int TIMER = 1000;
  boolean shake = true; 
  float incr = 0.0; 


  Message(String msg, float posX, float posY) {
    this.msg = msg; 
    this.posX = posX;
    this.posY = posY;
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
      println("hola " + posX + " " + incr);

      if (incr > 0.1) {
        shake = false;
        incr = incr - 0.1;
      }
    } 

    fill(255);
    text(msg, posX, posY);
  }
} 

class Player {
  String    t; 
  //float cw = textWidth(t);
  float     x;
  float     y;
  
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
  String    msg = "";
  int       NUM_CIRCLES = 3; 


  Player(float x, float y, float finalPlayerSize, float finalEnergySize, color colorPlayer, color colorEnergy) {
    this.x = x;
    this.y = y;
    this.finalPlayerSize = finalPlayerSize;
    this.finalEnergySize = finalEnergySize;
    this.colorPlayer = colorPlayer;
    this.colorEnergy = colorEnergy;
  }


  void draw() { 
    noStroke();
    fill(colorEnergy);
    
    //one one player is created 
    if (energySize < finalEnergySize) {
      energySize = energySize + 2;
    } 
    if (playerSize < finalPlayerSize) {
      playerSize = playerSize + 2;
    } 
    
    //one one player is destroyed
    //TODO
    //if (byebye) {
      //energySize 
    //} 

    //nimbus 
    for (int i = 0; i < NUM_CIRCLES; i++) {
      //make the circle breath (pulsate) a bit 
      breathOutSize = 4 * sin(incrOut + i * 1); 
      breathInSize = 12 * sin(incrIn + i * 1); 
     
      ellipse(x, y, energySize + breathInSize, energySize + breathInSize);
      fill(colorPlayer);
    }
    
    //person
    float pS = playerSize + breathOutSize;
    ellipse(x, y, pS, pS);
    //direction pointer 
    fill(255, 125); 
    arc(x, y, pS, pS, PI * 1.25, PI * 1.75);
    
    if (showSpeechBubble) {
      drawSpeechBubble(msg);
    }
    
    incrIn = incrIn + 0.05;
    incrOut = incrOut + 0.02;

  }

  void setPosition(float x, float y) { 
    this.x = x;
    this.y = y;
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
  
  void drawSpeechBubble(String t) {
    pushMatrix();
    translate(x-65, y-135);
    
    stroke(0);
    beginShape();
    vertex(30, 20);
    vertex(125, 20);
    vertex(125, 75);
    vertex(55,75);
    vertex(42,95);
    vertex(30, 75);
    endShape(CLOSE);
  
    fill(0);
    //println(cw);
  
    text(t,55,55);
    fill(255,0,0);
   
    popMatrix(); 
  }
}

class Team {
  
 // Variables 
  ArrayList<Player> members = new ArrayList<Player>();
  
  
  Team() { 
    
    
  } 
  
  public void addPlayers(ArrayList<Player> selectedPlayers) { 
    members.addAll(selectedPlayers);

    
  } 
  
  public void removePlayer() {
    
    
  } 
  
  // Constructor
  
  
  
  //Functions
  
  
  
  
  
}

