Mediawerf Game
==============

Contains the server side for the game with a small HTML5 mobile app for testing 

index.html 							monitor module 
mobile.html 						HTML5 testing app
module_scores.html			Independent screen showing the scores of the players 
module_projection.html	The game itself where you can see the players moving (uses processing.js) 
video.html							Just a test file for the video

css (we use LESS preprocessor)
---
module_auxiliar_screen.less			css for module_scores.html
module_projection.less					css for module_projection.html

game - contains the processing.js (based on HTML5 Canvas) 
---
CityScape.pde										City editor, you can create different buildings
Buildings.pde										
EnergyVampireProcessing.pde					
Message.pde											Messages that can be created by the players and placed on the map 
Player.pde											
Team.pde												Not used currently


