class CityScape {

  ArrayList<Building> b; 
  boolean editMode = false;
  Building currentBuilding; 
  float count = 0.0;
  float count2 = -width;
	boolean bgShow = false;
  public CityObjects co;

  CityScape() { 
    b = new ArrayList<Building>();
    co = new CityObjects();
  } 

  public void drawBG() {
   
		if(configurationGUI.showBG == true) {
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

  public void drawCityObjects() {
    co.draw();
  }

  public void drawConnections(float x, float y) {
    //console.log(co.l.size());
    stroke(255);
    for (int i = 0; i < co.l.size(); i++) {

      q = co.l.get(i);
      x1 = q.pos.x;
      y1 = q.pos.y;
      distance = dist(x, y, x1, y1);

      text(distance, (x + x1) * 0.5, (y + y1) * 0.5);
      line(x, y, x1, y1);
    }


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

    var o = "0:835 695,634 838,561 732,767 595,\n1:696 865,674 842,709 817,727 834,816 784,841 820,734 896,712 864,\n2:857 810,812 748,857 713,905 774,\n3:519 833,526 847,531 845,540 858,529 868,556 898,592 874,566 839,559 843,554 838,557 834,543 815,\n4:832 583,815 558,849 529,855 535,877 519,891 535,896 535,917 564,898 576,878 554,\n5:1001 667,977 639,989 626,971 599,995 586,993 574,1008 571,1016 579,1045 562,1055 572,1078 569,1098 598,\n6:987 516,952 474,941 480,928 463,977 429,999 461,995 469,1013 499,\n7:1057 460,1040 436,1104 387,1103 377,1120 370,1122 375,1144 366,1169 399,1164 404,1172 414,1222 383,1240 405,1157 461,1137 440,1156 430,1130 413,\n8:203 585,335 489,283 409,173 489,164 482,141 495,\n9:134 483,107 445,146 417,136 413,123 401,127 378,143 363,162 370,170 391,225 364,226 351,149 313,155 259,264 305,287 294,385 444,388 460,362 476,285 384,\n10:305 653,286 626,330 593,311 564,442 475,446 493,546 433,564 471,474 535,546 651,524 667,515 660,420 722,351 621,\n11:718 550,605 376,633 358,734 500,791 459,697 325,679 332,671 323,721 294,842 463,\n13:881 438,856 410,878 391,885 396,1029 277,1025 269,1049 254,1067 279,\n14:1106 356,1085 336,1122 305,1130 308,1154 287,1172 288,1175 308,1132 348,1123 341,\n15:1186 245,1286 354,1267 370,1282 397,1256 424,1279 469,1313 514,1334 493,1354 501,1376 485,1385 495,1431 458,1388 413,1363 375,1271 279,1241 247,1217 216,\n16:1087 236,1102 253,1168 196,1163 187,1181 174,1129 114,1112 130,1145 185,\n17:836 388,773 310,863 250,892 290,906 284,915 293,922 292,932 308,\n18:888 231,912 255,931 239,912 214,\n19:952 260,971 278,1025 228,973 167,921 203,\n20:418 439,463 404,450 392,403 425,\n21:463 391,500 370,435 270,319 343,337 360,357 345,376 346,398 328,412 320,\n22:355 362,374 395,396 384,413 393,432 381,436 366,405 337,\n23:751 54,845 180,862 187,955 121,863 10,804 8,\n24:1149 21,1390 305,1427 282,1426 251,1398 261,1175 13,\n25:1003 14,1063 85,1081 80,1109 104,1132 75,1098 38,1080 53,1037 6,\n26:439 128,455 162,481 152,594 335,709 249,560 52,\n27:633 80,770 246,814 222,814 207,687 46,"
    var m = o.split("\n");

    $.each(m, function(k, v) { 

      var txtLine = v.split(":");
      console.log("---> " + txtLine);

     
      var m1 = txtLine[0]
      var m2 = txtLine[1]
      var mm = m2.split(",");

      qq = mm;

      Building cb = new Building();
      b.add(cb);
      $.each(qq, function(k, v) {
         if(v == "") {
        console.log("buuuuuu");
        return; 
      }
        var pos = v.split(" ");
        cb.addPoint(pos[0], pos[1]);
        console.log(m1 + " " + pos[0] + " " + pos[1])
      });
    //mm.split(" ");

    });
  } 

  public void save() {
    console.log("save");
    console.log(b.get(0)); 

    var txt = "";   
    for (int i = 0; i < b.size(); i++) {
      Building b_ = b.get(i); 
      var lineTxt = "" + i + ":";
      qq = b_;
      for(int j = 0; j < b_.p.size(); j++) {
        var x = b_.p.get(j).x;
        var y = b_.p.get(j).y;
        lineTxt = lineTxt + x + " " + y + ",";
      } 
      txt = txt + lineTxt + "\n";
      //console.log(lineTxt);
    } 
    console.log(txt);
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
  float r;

  Building () { 
    p = new ArrayList<PVector>();
    r = random();
  } 

  public void addPoint(int x, int y) {
    p.add(new PVector(x, y));
  } 

  public void removeLastPoint() {
    p.remove(p.size() - 1);
  } 


  public void draw() {
    if (fading) {
      bg = (int)(abs(255 * sin(count + r)));  
      count += 0.005;
			//println("hola");
    } 

    fill(bg); 
		//strokeWeight(10);	 
    //stroke(border, borderAlpha);	

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
