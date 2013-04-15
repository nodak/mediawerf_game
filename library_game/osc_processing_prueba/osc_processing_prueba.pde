import oscP5.*;
import netP5.*;

OscP5 oscP5;
NetAddress myRemoteLocation;


void setup() {
  size(400, 400);
  frameRate(25);
  /* start oscP5, listening for incoming messages at port 12000 */
  oscP5 = new OscP5(this, 12002); 

  myRemoteLocation = new NetAddress("127.0.0.1", 3333);
}

void draw() { 
  background(255);

  fill(0);
  ellipse(mouseX, mouseY, 12, 12);
  sendMouse(mouseX, mouseY);
} 

public void sendMouse(int x, int y) {
  /* create a new osc message object */
  OscMessage myMessage = new OscMessage("/mpl"); 

  myMessage.add(mouseX); //number 
  myMessage.add(mouseY); //msg 

  /* send the message */
  oscP5.send(myMessage, myRemoteLocation);
}

