class CityScape {

  ArrayList<Building> b; 
  boolean editMode = false;
  Building currentBuilding; 
  float count = 0.0;
  float count2 = -width;
	boolean bgShow = false;
  public CityObjects co;
  int action = 1;

  CityScape() { 
    b = new ArrayList<Building>();
    co = new CityObjects();
    //qq2 = co;
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
    noStroke();
    for (int i = 0; i < b.size(); i++) {
      b.get(i).draw();
    }

    count += 0.01;
  } 

  public void drawCityObjects() {
    co.draw(); 
  }

  //check if we are close to the hints and remove them
  public void checkHints (Player p) {
    CityObjects returnVal;

    for (int i = 0; i < co.l.size(); i++) {
      q = co.l.get(i);
      x1 = q.pos.x;
      y1 = q.pos.y;
      distance = dist(p.x, p.y, x1, y1);

      if (q.type == "hint" && distance < 15 ) {
        //console.log("lalalallalala");
        q.displayMsg(true);
        returnVal = q; 
      } else if (q.type == "hint" && distance > 15) {
        q.displayMsg(false);
      }
    } 

    return returnVal; 

  }

  public void drawConnections(float x, float y) {
    //console.log(co.l.size());
    stroke(255);
    for (int i = 0; i < co.l.size(); i++) {

      q = co.l.get(i);
      x1 = q.pos.x;
      y1 = q.pos.y;
      distance = dist(x, y, x1, y1);

      fill(225)
      stroke(225);
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

  public void load(boolean isLibrary) {

    var o;
    if (isLibrary) {
      o = "0:778 142,801 169,935 53,1072 194,1096 169,943 5,921 11,\n1:1101 211,1280 420,1292 413,1321 441,1348 413,1313 372,1289 386,1122 192,\n2:1324 467,1359 514,1299 568,1315 587,1385 531,1382 523,1394 510,1339 448,\n3:857 231,957 376,1064 296,960 154,\n4:963 388,1045 502,1079 525,1168 459,1054 320,\n5:1143 802,1155 816,1163 808,1199 853,1227 834,1211 803,1216 795,1195 766,\n6:1255 771,1273 794,1337 751,1341 737,1368 762,1352 774,1370 799,1451 739,1437 718,1387 749,1378 733,1382 729,1360 698,1336 707,1319 708,1319 720,\n7:1193 974,1214 1005,1309 937,1289 903,1270 909,1260 894,1232 911,1221 908,1185 933,1199 961,\n8:1026 891,1045 916,1090 885,1115 911,1129 900,1091 850,\n9:1298 670,1321 691,1335 676,1350 681,1388 645,1370 620,1338 641,1332 639,\n10:1400 579,1496 687,1478 710,1495 736,1466 765,1527 852,1555 825,1566 841,1594 817,1605 831,1646 792,1430 551,\n11:634 1061,731 998,734 1003,759 989,682 869,778 803,753 765,654 827,646 818,526 897,543 926,500 963,522 987,565 963,579 980,576 987,\n12:702 221,821 380,876 341,750 174,\n13:845 415,984 590,1028 564,1025 535,902 378,\n14:990 651,1049 722,1145 640,1121 622,1108 631,1077 584,\n15:1104 566,1125 589,1144 573,1126 548,\n16:1132 541,1172 586,1163 596,1179 610,1241 564,1186 500,\n17:1071 744,1092 770,1280 616,1260 589,1244 600,1232 585,1095 698,1104 717,\n18:783 1077,776 1065,981 928,1007 968,1019 968,1043 1003,1040 1014,1052 1027,984 1079,\n19:354 832,415 920,547 826,497 746,386 820,377 813,\n20:673 450,813 675,837 656,835 646,912 594,775 387,\n21:518 467,504 470,492 466,484 458,479 446,484 427,490 419,500 413,512 413,526 421,533 432,536 445,533 456,528 463,518 469,\n22:349 817,322 780,438 699,443 682,359 642,367 591,478 637,503 625,508 639,569 595,544 538,597 516,651 596,644 601,709 701,677 725,604 637,537 677,601 788,573 808,499 718,\n24:1420 119,1275 238,1208 169,1238 142,1282 170,1299 169,1360 110,1397 90,\n25:1141 67,1198 133,1240 95,1180 28,\n26:1073 4,1119 43,1150 13,1107 4,\n27:1408 263,1428 244,1387 192,1298 269,1612 635,1727 535,1712 517,1623 592,1346 279,1396 235,1398 253,\n28:1421 269,1435 250,1541 368,1526 388,\n29:1556 381,1569 359,1697 496,1677 515,\n30:1422 180,1457 219,1481 199,1464 197,1455 190,1488 161,1466 142,\n31:1488 247,1501 265,1621 157,1598 134,1596 152,\n32:1473 134,1492 152,1528 115,1510 100,\n34:1803 457,1822 473,1835 459,1817 442,";      
    } else {
      o = "0:835 695,634 838,561 732,767 595,\n";
      //o = "0:835 695,634 838,561 732,767 595,\n1:696 865,674 842,709 817,727 834,816 784,841 820,734 896,712 864,\n2:857 810,812 748,857 713,905 774,\n3:519 833,526 847,531 845,540 858,529 868,556 898,592 874,566 839,559 843,554 838,557 834,543 815,\n4:832 583,815 558,849 529,855 535,877 519,891 535,896 535,917 564,898 576,878 554,\n5:1001 667,977 639,989 626,971 599,995 586,993 574,1008 571,1016 579,1045 562,1055 572,1078 569,1098 598,\n6:987 516,952 474,941 480,928 463,977 429,999 461,995 469,1013 499,\n7:1057 460,1040 436,1104 387,1103 377,1120 370,1122 375,1144 366,1169 399,1164 404,1172 414,1222 383,1240 405,1157 461,1137 440,1156 430,1130 413,\n8:203 585,335 489,283 409,173 489,164 482,141 495,\n9:134 483,107 445,146 417,136 413,123 401,127 378,143 363,162 370,170 391,225 364,226 351,149 313,155 259,264 305,287 294,385 444,388 460,362 476,285 384,\n10:305 653,286 626,330 593,311 564,442 475,446 493,546 433,564 471,474 535,546 651,524 667,515 660,420 722,351 621,\n11:718 550,605 376,633 358,734 500,791 459,697 325,679 332,671 323,721 294,842 463,\n13:881 438,856 410,878 391,885 396,1029 277,1025 269,1049 254,1067 279,\n14:1106 356,1085 336,1122 305,1130 308,1154 287,1172 288,1175 308,1132 348,1123 341,\n15:1186 245,1286 354,1267 370,1282 397,1256 424,1279 469,1313 514,1334 493,1354 501,1376 485,1385 495,1431 458,1388 413,1363 375,1271 279,1241 247,1217 216,\n16:1087 236,1102 253,1168 196,1163 187,1181 174,1129 114,1112 130,1145 185,\n17:836 388,773 310,863 250,892 290,906 284,915 293,922 292,932 308,\n18:888 231,912 255,931 239,912 214,\n19:952 260,971 278,1025 228,973 167,921 203,\n20:418 439,463 404,450 392,403 425,\n21:463 391,500 370,435 270,319 343,337 360,357 345,376 346,398 328,412 320,\n22:355 362,374 395,396 384,413 393,432 381,436 366,405 337,\n23:751 54,845 180,862 187,955 121,863 10,804 8,\n24:1149 21,1390 305,1427 282,1426 251,1398 261,1175 13,\n25:1003 14,1063 85,1081 80,1109 104,1132 75,1098 38,1080 53,1037 6,\n26:439 128,455 162,481 152,594 335,709 249,560 52,\n27:633 80,770 246,814 222,814 207,687 46,";

    }
    var m = o.split("\n");

    $.each(m, function(k, v) { 

      var txtLine = v.split(":");
      //console.log("---> " + txtLine);

     
      var m1 = txtLine[0]
      var m2 = txtLine[1]
      var mm = m2.split(",");

      qq = mm;

      Building cb = new Building();
      b.add(cb);
      $.each(qq, function(k, v) {
         if(v == "") {
        //console.log("buuuuuu");
        return; 
      }
        var pos = v.split(" ");
        cb.addPoint(pos[0], pos[1]);
        //console.log(m1 + " " + pos[0] + " " + pos[1])
      });
    //mm.split(" ");

    });
  } 

  public void save() {
    //console.log("save");
    //console.log(b.get(0)); 

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

    colorMode(RGB, 255);
    fill(bg, 225); 
		//strokeWeight(10);	 
    //stroke(border, borderAlpha);	

		//fill(255);
    beginShape();   
    for (int i = 0; i < p.size(); i++) {
      vertex(p.get(i).x , p.get(i).y);
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
