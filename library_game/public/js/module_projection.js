
function showOverlay(viz) {
	if (viz) {
		$("#overlay").show();
		$("#overlay").animate({opacity : 1.0}, 200);			
	} else {
		$("#overlay").animate({opacity : 0.0}, 200); 
	} 
}


function speak(text) {

	var myText = [];
	$.each(text.split(" "), function(k, v) {
		myText.push(capitalise(v));

	});;

	var encodedText = encodeURIComponent(myText.join(" ")+ " ");
	var audio = new Audio();
	audio.src ='http://translate.google.com/translate_tts?ie=utf-8&tl=en&q=' + encodedText;
	console.log(audio.src);
	audio.play();
}

function capitalise(str)
{
   return str.charAt(0).toUpperCase() + str.slice(1);
}


/* 
* transformations on the content for the projection mapping 
*
*/ 
var Configuration = function() {
	this.computerName = 'untitled';
	this.serverAddress = "http://outside.mediawerf.net:8080";
	this.localAddress = "";
	this.layout = "";
	this.showMap = false;
	this.debugMessages = true;
	this.showDistances = true;
	this.showOverlays = true;

	this.videoX = 0;
	this.videoY = 0;
	this.videoDeg = 0;
	
	this.contentX = 0;
	this.contentY = 0;
	this.contentDeg = 0;
	this.contentSkew = 0;
	
	this.showBG = false;
	
	this.toggleMap = function() {
	    $("#map_canvas").fadeToggle(500);
	}
	
	this.toggleProcessing = function() {
	    $("#canvascontainer").fadeToggle(500);
	    
	}
	
	this.toggleBGTransparency = function() {
	    this.showBG ^= true;
	}
	
	this.updateVideoPosition = function() {
		this.updateVideo(this.videoX, this.videoY, this.videoDeg);
	}; 
	
	this.updateVideo = function(x, y, deg) { 
		$("#videoframe").css("-webkit-transform", "rotate3d(0, 0, 1, "+deg+"deg) translate(" + x +"px, " + y + "px)")
						.css("-webkit-transform-origin", x + "px " + y +"px " + 0 + "px");
		//$("videoframe").css({top:y+"px", left:x+"px"});

	}; 	
	
	this.updateContentPosition = function() {
		this.updateContent(this.contentX, this.contentY, this.contentDeg);
	}; 
	
	this.updateContent = function(x, y, deg) { 
		$("#content").css("-webkit-transform", "rotate3d(0, 0, 1, "+deg+"deg) translate(" + x +"px, " + y + "px)")
						.css("-webkit-transform-origin", x + "px " + y +"px " + 0 + "px");
		//$("videoframe").css({top:y+"px", left:x+"px"});

	};  

	this.updateComputerName = function(name) {
		localStorage.setItem("computerName", name);
	};

	this.updateServerAddress = function(name) {
		localStorage.setItem("serverAddress", name);
	};

	this.updateLocalAddresss = function(name) {
		localStorage.setItem("localAddress", name);
	};


	this.getComputerName = function() {
		var name = localStorage.getItem("computerName");

		if (name != null) {
			this.computerName = name;
		}
		console.log(this.computerName);

		return this.computerName;
	};
	
	
} 

var configurationGUI;
var nickname;

function initDebug() {
	configurationGUI = new Configuration(); 
	//c.watch("x", function() { console.log("hola"); });
	
	var gui = new dat.GUI();
	configurationGUI.getComputerName();

	gui.add(configurationGUI, 'computerName').onChange(function(value) { 
		configurationGUI.updateComputerName(value);
	});

	gui.add(configurationGUI, 'serverAddress').onChange(function(value) { 
		configurationGUI.updateServerAddress(value);
	});

	gui.add(configurationGUI, 'localAddress').onChange(function(value) { 
		configurationGUI.updateLocalAddress(value);
	});

	

	gui.add(configurationGUI, 'videoX', 0, 500).onChange(function(value) {
		configurationGUI.updateVideoPosition();
	});
	
	gui.add(configurationGUI, 'videoY', 0, 500).onChange(function(value) {
		configurationGUI.updateVideoPosition();
	});
	
	gui.add(configurationGUI, 'videoDeg', 0, 360).onChange(function(value) {
		configurationGUI.updateVideoPosition();
	}); 
	
	gui.add(configurationGUI, 'contentX', -200, 500).onChange(function(value) {
		configurationGUI.updateContentPosition();
	});
	
	gui.add(configurationGUI, 'contentY', -200, 500).onChange(function(value) {
		configurationGUI.updateContentPosition();
	});
	
	gui.add(configurationGUI, 'contentDeg', 0, 360).onChange(function(value) {
		configurationGUI.updateContentPosition();
	}); 
	
	
	
	gui.add(configurationGUI, 'toggleMap');
	gui.add(configurationGUI, 'toggleProcessing');
	gui.add(configurationGUI, 'toggleBGTransparency');
	
}




/* 
* Google Maps stuff 
* TODO: it would be could to recenter the area just draging the map 
*
*/

//center of our map
var llat = 51.885583;
var llon = 4.495753;
var gmap;

var Gmap = function() {
	var map; 
	var mapType;
	var distancePerMeter;


	//google maps
	Gmap.prototype.initializeGoogleMap = function () {
  	var mapOptions = {
	 		center: new google.maps.LatLng(llat, llon),
	    zoom: 18,
	  	mapTypeId: google.maps.MapTypeId.ROADMAP
	  };
	
	
		this.map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
		
		overlay = new google.maps.OverlayView();
	 	overlay.draw = function() {};
	  	overlay.remove = function() {};
	  	overlay.setMap(this.map);		
	 };
	
	Gmap.prototype.placeMarker = function (location) {
		var marker = new google.maps.Marker({
			position: location, 
			map: this.map
		});
	
		this.map.setCenter(location);
	};
	
	Gmap.prototype.getXYCoordinates = function(location) {
		this.mapType = this.map.mapTypes[this.map.getMapTypeId()];
    	//var mapPixel       = this.mapType.projection.fromLatLngToPoint(location);
    	//var containerPixel = overlay.getProjection().fromLatLngToContainerPixel(location);
    	var divPixel = overlay.getProjection().fromLatLngToDivPixel(location);

		return divPixel;
	};

	Gmap.prototype.getLatLngCoordinates = function(x, y) {


		var q = overlay.getProjection().fromContainerPixelToLatLng(
    		new google.maps.Point(x, y)
		);

		var latlon = [];
		//console.log(q);
		latlon.lat = q.jb;
		latlon.lon = q.kb;

		//console.log(q);
		//console.log(divPixel.x + " " + divPixel.y);


		return latlon;
	}; 

	Gmap.prototype.getMetersPerPixel = function() {
		var latLngA = new google.maps.LatLng(this.getLatLngCoordinates(0, 0).lat, this.getLatLngCoordinates(0, 0).lon); 
		var latLngB = new google.maps.LatLng(this.getLatLngCoordinates(1, 0).lat, this.getLatLngCoordinates(1, 0).lon); 

		//console.log(latLngA);
		//console.log(latLngB);

		var distance = google.maps.geometry.spherical.computeDistanceBetween (latLngA, latLngB);
		console.log("distance is " + distance);
		this.distancePerMeter = distance;

		return distance;
	}
	

} 

function getSounds(f) {
	jQuery.get('./static/sounds.txt', function(data){
    
    	var line = data.split('\n');

    	$.each(line, function(n, value){
       		//console.log(value); // log each title
       		var name = value.split('.')[0]; 
       		var uri = "http://mediawerf.dyndns.org/GameLoops/" + name + ".ogg";
       		//console.log(name);
       		//console.log(uri);

       		sound[name] = new buzz.sound(uri);
 			sound[name].play();
 			sound[name].loop();
       		sound[name].setVolume(0);
       		
    	});

	});
}

var t;
var sound = {}; 
var qq;

$(document).ready(function() {
    initDebug();

	getSounds();


	/*
	* socket io stuff to get information from the game controllers  
	*/
	/*
	var socket = io.connect(configurationGUI.serverAddress);

	//socket.emit('log');
	socket.on('registerController', function(data){
		console.log("registrado" + data);
	});
	

	//controllers 
	socket.on('remoteController', function(data){
		movePlayerXY(nickname, data[1], data[2]);
	}); 

	socket.on('force', function(data) {
		console.log("force");
	    trigger("#acc");
	});

	socket.on('answer', function(data) {
		console.log("answer");  

		if (data.answer == true) { 
	    	trigger("#yes");
	    } else {
	  		trigger("#no");
  	
	    }
	});


	socket.on('orientation', function(data) {
		console.log("orientation " + data.pitch);  

		var p = Processing.getInstanceById('game');
		p.setOrientation(1, data.pitch);
	});
	*/ 


	//keys 
	var keyControllerX = document.width / 2;
	var keyControllerY = document.height / 2;
	var keyControllerIncrX = 15;
	var keyControllerIncrY = 15;


	$(document).bind('keydown', function (evt) { 
		var moving = false;
		switch(evt.which) {
			case 37: //left 
				keyControllerX -= keyControllerIncrX;
				moving = true;
				break;
			case 39: //right
				keyControllerX += keyControllerIncrX;
				moving = true;
				break;
			case 38: //up 
				keyControllerY -= keyControllerIncrY;
				moving = true;
				break;

			case 40: //down 
				keyControllerY += keyControllerIncrY;
				moving = true;
				break;
		} 

		if (moving) {
			movePlayerXY(nickname, keyControllerX, keyControllerY);
		}
	});




	//maps 
	gmap = new Gmap();
		
	gmap.initializeGoogleMap();
	google.maps.event.addListenerOnce(gmap.map, 'idle', function(){
    	// do something only the first time the map is loaded
		gmap.getMetersPerPixel();
		nickname = configurationGUI.getComputerName();

		addOfflinePlayer(nickname, 2);
		game.registerPlayer(nickname);
		game.listTargets();


	});

	google.maps.event.addListener(gmap.map, 'mousemove', function(mEvent) {
          //latLngControl.updatePosition(mEvent.latLng);
    	//console.log(mEvent.latLng);
    });
	

  	google.maps.event.addListener(gmap.map, 'center_changed', function() {
    	

  	});

	loc = new google.maps.LatLng(llat, llon);
	gmap.placeMarker(loc);

	//when player join we added to our list of connected players
	game.bind("playerJoined", function(player) {
		console.log("playerJoined");
		//playerJoin(player.nickname, numPlayer);
		addPlayer(player);
	});
	
	//remove the player from the list
	game.bind("playerDisconnected", function(player) {
			console.log("playerDisconnected");
			//playerJoin(player.nickname, numPlayer);
		
			var p = Processing.getInstanceById('game');
			p.removePlayer(player.id);
	});
	
	game.bind("updateLocation", function(player, location) {
		//console.log("updateLocation");
		//console.log(player.nickname + " " + location.lat + " " + location.lng);
		movePlayer(player);
	});

	game.bind("textMessage", function(data) {
		console.log(data);
		var q = data.message.split("::");

		if (q[0] == "/say") {
			speak(q[1]);
		}

	});
	
		

	game.bind("targetInRange", function(data) {
		//console.log("targetInRange");
		//console.log(data.target);
		//console.log(data.distance);

		var dist = Math.round(data.distance);
		var volume = 0;
		if (dist < data.target.range) {
			volume = (100 - dist * (100 / data.target.range));
		} 

		var qq = data.target.value;
		soundName = qq.substring(qq.lastIndexOf("/") + 1, qq.lastIndexOf("."));
		//console.log(" " + volume);

		if (sound[soundName] != undefined) {
			sound[soundName].fadeTo(Math.round(volume));
			sound[soundName].loop();
			console.log("-->" + soundName + " " + volume);
		}


	});
	
	game.bind("playerInRange", function(player, distance) {
		console.log("targetInRange");
		console.log(player);
		console.log(distance);

		//console.log(player.nickname + " " + location.lat + " " + location.lng);
		movePlayer(player);
	});
	

	game.bind("listTargets", function(data) {
		console.log("listTargets");
		console.log(data);

		//t = data.targets;
	
		addTargets(data.targets);

		//console.log(player.nickname + " " + location.lat + " " + location.lng);
	});
	

	
	function addPlayer(player) {
		var p = Processing.getInstanceById('game');
		p.addPlayer(player.id, player.color);
	}

	//adding a player to move with the game pad
	function addOfflinePlayer(id, color) {
		var p = Processing.getInstanceById('game');
		p.addPlayer(id, color);
	} 

	function addTargets(targets) {
		var p = Processing.getInstanceById('game');

		$.each(targets, function(key, value) { 

			var lat = value.location.lat;
			var lon = value.location.lng;
			var div = gmap.getXYCoordinates(new google.maps.LatLng(lat, lon)); 

			var x = div.x; 
			var y = div.y;
			
			//console.log("qq -----> " + value._id + " " + x + " " + y + " " + lat + " " + lon + " " + value.range);
			p.addTarget(value._id, Math.round(x), Math.round(y), lat, lon, value.range);

		}); 

	}

	//move a player so we have to call google api to translate from geo positions to x, y 
	function movePlayer(player) {
		var p = Processing.getInstanceById('game');
		p.setPosition(player.id, player.location.lat, player.location.lng);	
	}
	
	function movePlayerXY(id, x, y) {
		var p = Processing.getInstanceById('game');
		p.setPositionXY(id, x, y);	
		var latlon = gmap.getLatLngCoordinates(x, y);  
		//console.log("movement " + latlon.lat + " " + latlon.lon);
		game.sendLocation({lat:latlon.lat, lng:latlon.lon});

	}
	

    /* panel */ 
    $("#panel").draggable();
    $("#left").click(function() {
        game.sendMessage(game.players[0], "/say::Go Left");

    });
    
      $("#right").click(function() {
        game.sendMessage(game.players[0], "/say::Go Right");
    });
    
      $("#straight").click(function() {
        game.sendMessage(game.players[0], "/say::Go Straight");
    });
    
      $("#turn").click(function() {
        game.sendMessage(game.players[0], "/say::Turn around");
    });
    
      $("#poke").click(function() {
        game.sendMessage(game.players[0], "/poke::");

    });
    
      $("#send").click(function() {
        var val = $("#msg").val();
        game.sendMessage(game.players[0], "/say::"+val);
    });
    
    function trigger(id) {
       $(id).css("background-color", "red"); 
        
        setTimeout(function() {
            $(id).css("background-color", ""); 
   
        }, 500);
    }
    


});