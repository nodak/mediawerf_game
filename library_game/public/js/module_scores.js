	/*
	var Audio = function {
		Audio.NEW_PLAYER = "new_player.ogg";
		Audio.BYE_PLAYER = "bye_player.ogg";
		Audio.BG_SOUND = "bg_sound.ogg";
		Audio.play = function(soundFile) {
			
		};
		
	}
	*/
	
	game.bind("playerJoined", function(player) {
		console.log("playerJoined");
		playerJoin(player.nickname, player.id);
	});
	
		
	game.bind("playerDisconnected", function(player) {
		console.log("playerDisconnect");
		playerLeave(player.id);
	});
		
	
	var numPlayer = 0; 
	
	function playerJoin(name, num) {
		$("#soundJoin")[0].play();
		var p = $('.player#toClone').clone();
		p.find(".playerName").text(name);		
			
		p.attr("id", num);
		//p.css("display", "block"); 
		
		numPlayer++;
		$("#playerList").append(p);
		p.show('slow');
	} 
	
	function playerLeave(num) {
		var p = $("#playerList > #" + num);
		p.hide('slow', function() {
			p.remove();
		});
		$("#soundLeave")[0].play();
		
		
	}
	
	
	
/*
	//Set countdown and content div invisible initially
	$(function() {
			document.getElementById("countdown").style.display = "none";
			document.getElementById("content").style.display = "none";
				
	});
	
	//showDiv function, show div on button click
	function goToGame() {
		document.getElementById('countdown').style.display = "block";
		document.getElementById('loadGame').style.display = "none";
			   
			

		//Countdown function
		$(function(){
			//Don't show content div
			document.getElementById("content").style.display = "none";
			
            var timer = window.setInterval(function() {

                var timeCounter = $("span[id=show-time]").html();
                var updateTime = eval(timeCounter)- eval(1);
                $("span[id=show-time]").html(updateTime);
				
				//If less than 4 seconds left
				if(updateTime < 4){
					var span = document.getElementById("show-time");
					console.log(updateTime);
					
					//Change the fontsize of the countdown
					span.style.fontSize = "300px";			
					//Make the countdown blink
					function blink(selector){
						$(selector).fadeOut(800, function(){
							$(this).fadeIn(200, function(){
								blink(this);
							});
						});
					}
					$(function() {
						blink('#show-time');
					});
					
					//If countdown reaches 1, only fade out
					if(updateTime == 1){
						$(selector).fadeOut(800, function(){	
								blink(this);
						});
						document.getElementById("content").style.display = "inline";
					}
					
					//If countdown reaches 0, hide the countdown div, show the content div and stop the interval
					if(updateTime == 0){
						window.clearInterval(timer);
						document.getElementById("countdown").style.display = "none";
						document.getElementById("content").style.display = "inline";
					}
				}
				
			// 1000 miliseconds interval
            }, 1000);		
		});
	}

*/

var info; 
var Info = function() {
	
//update scores
Info.prototype.updateScores = function (numbersArray)
{
	var name = "";
	for (var i = 0; i < numbersArray.length; i++)
	{
		console.log(i);
		name = "#player" + (i+1);
		$(name).text(numbersArray[i]);
	}
}

	
  //sort that shit
	Info.prototype.sortUsingNestedText = function (parent, childSelector, keySelector) {
    	var items = parent.children(childSelector).sort(function(a, b) {
	        var vA = $(keySelector, a).text()/1000;
	        var vB = $(keySelector, b).text()/1000;
	        return (vA > vB) ? -1 : (vA < vB) ? 1 : 0;
    	});
		parent.append(items);
	}

} 



$(document).ready(function(){
	var info = new Info();

  //socket io stuff
	var socket = io.connect('http://http://davidjonas.dyndns.org:9091');
  	$('#getNums').click(function(){
  		socket.emit('log');
  	});

  	socket.on('updateScores', function(data){
	  	info.updateScores(data.array);
	  	info.sortUsingNestedText($('#top3leftscores'), "div", "span.points");
  	});

  	socket.on('updateLocation',function(data) {
  		//console.log("x: "data.positions[0].x);
  		//console.log("y: "data.positions[0].y);

			console.log("Got LOCATION of player " + newPlayer.getNickname() + " at Lat:" + data['lat'] + " Lng:" + data['lng']);
			socket.broadcast.emit('updateLocation', {lat: data['lat'], lng: data['lng'], player: newPlayer.getNickname()});
  	});




//goToGame();

	
});
