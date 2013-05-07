
function showOverlay(viz) {
	if (viz) {
		$("#overlay").show();
		$("#overlay").animate({opacity : 1.0}, 200);			
	} else {
		$("#overlay").animate({opacity : 0.0}, 200); 
	} 
}


/* 
* transformations on the content for the projection mapping 
*
*/ 
var Debug = function() {
	this.videoX = 0;
	this.videoY = 0;
	this.videoDeg = 0;
	
	this.contentX = 0;
	this.contentY = 0;
	this.contentDeg = 0;
	this.contentSkew = 0;
	
	this.showBG = false;
	
	this.toggleMap = function() {
	    console.log("toggleMap");
	    $("#map_canvas").fadeToggle(500);
	}
	
	this.toggleProcessing = function() {
	    console.log("toggleProcessing");
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
	
	
} 

var debugGUI;
function initDebug() {
	debugGUI = new Debug(); 
	//c.watch("x", function() { console.log("hola"); });
	
	var gui = new dat.GUI();
	gui.add(debugGUI, 'videoX', 0, 500).onChange(function(value) {
		debugGUI.updateVideoPosition();
	});
	
	gui.add(debugGUI, 'videoY', 0, 500).onChange(function(value) {
		debugGUI.updateVideoPosition();
	});
	
	gui.add(debugGUI, 'videoDeg', 0, 360).onChange(function(value) {
		debugGUI.updateVideoPosition();
	}); 
	
	gui.add(debugGUI, 'contentX', -200, 500).onChange(function(value) {
		debugGUI.updateContentPosition();
	});
	
	gui.add(debugGUI, 'contentY', -200, 500).onChange(function(value) {
		debugGUI.updateContentPosition();
	});
	
	gui.add(debugGUI, 'contentDeg', 0, 360).onChange(function(value) {
		debugGUI.updateContentPosition();
	}); 
	
	
	
	gui.add(debugGUI, 'toggleMap');
	gui.add(debugGUI, 'toggleProcessing');
	gui.add(debugGUI, 'toggleBGTransparency');
	
	
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
		console.log("esto es una prueba" + this.mapType);
		
	 };
	
	Gmap.prototype.placeMarker =	function (location) {
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
    	var divPixel       = overlay.getProjection().fromLatLngToDivPixel(location);

		//console.log(divPixel.x + " " + divPixel.y);
		return divPixel;
	};

	Gmap.prototype.getLatLngCoordinates = function(x, y) {


		var q = overlay.getProjection().fromContainerPixelToLatLng(
    		new google.maps.Point(x, y)
		);

		var latlon = [];
		latlon.x = q.jb;
		latlon.y = q.kb;
		//console.log(divPixel.x + " " + divPixel.y);


		return latlon;
	};


	
}





$(document).ready(function() {
 
	/*
	* socket io stuff to get information from the game controllers  
	*/
	var socket = io.connect('http://127.0.0.1:8080');

	//socket.emit('log');
	socket.on('registerController', function(data){
		console.log("registrado" + data);
	});
	
	setTimeout(function() {
		addOfflinePlayer(888, 2);
	}, 1000);

	socket.on('remoteController', function(data){
		//console.log(data[1] + " " + data[2]); 
		movePlayerXY("888", data[1], data[2]);
	});

	socket.on('force', function(data) {
		console.log("force");
	    trigger("#acc");
	});


	//maps 
	gmap = new Gmap();
	gmap.initializeGoogleMap(); 


	google.maps.event.addListener(gmap.map, 'mousemove', function(mEvent) {
          //latLngControl.updatePosition(mEvent.latLng);
    	//console.log(mEvent.latLng);
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
			console.log(p);
			p.removePlayer(player.id);
	});
	
	game.bind("updateLocation", function(player, location) {
		console.log("updateLocation");
		console.log(player.nickname + " " + location.lat + " " + location.lng);
		movePlayer(player);
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

	//move a player so we have to call google api to translate from geo positions to x, y 
	function movePlayer(player) {
		var p = Processing.getInstanceById('game');
		p.setPosition(player.id, player.location.lat, player.location.lng);	
	}
	
	function movePlayerXY(id, x, y) {
		var p = Processing.getInstanceById('game');
		p.setPositionXY(id, x, y);	
	}
	

    initDebug();

    /* panel */ 
    $("#panel").draggable();
  	console.log("ready!"); 
    $("#left").click(function() {
        console.log("left");
        game.sendMessage(game.players[0], "/say::Go Left");

    });
    
      $("#right").click(function() {
        console.log("right");
        game.sendMessage(game.players[0], "/say::Go Right");
    });
    
      $("#straight").click(function() {
        console.log("straight");
        game.sendMessage(game.players[0], "/say::Go Straight");
    });
    
      $("#turn").click(function() {
        console.log("turn");
        game.sendMessage(game.players[0], "/say::Turn around");
    });
    
      $("#poke").click(function() {
        console.log("poke");
        game.sendMessage(game.players[0], "/poke::");

    });
    
      $("#send").click(function() {
        var val = $("#msg").val();
        console.log("send " + val); 
        game.sendMessage(game.players[0], "/say::"+val);
    });
    
    function trigger(id) {
       $(id).css("background-color", "red"); 
        
        setTimeout(function() {
            $(id).css("background-color", ""); 
   
        }, 500);
    }
    


});