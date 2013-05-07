$(document).ready(function(){
	$(function() {
		$('#slides').slidesjs({
			width: 940,
			height: 528,
			/*navigation: {
				active: true,
				// [boolean] Generates next and previous buttons.
				// You can set to false and use your own buttons.!!!!!!!
				// User defined buttons must have the following:
				// previous button: class="slidesjs-previous slidesjs-navigation"
				// next button: class="slidesjs-next slidesjs-navigation"
			}*/
			pagination: {
				active: false,
				// [boolean] Create pagination items.
				// You cannot use your own pagination. Sorry.
			}
		});
		$('.pull-me').click(function() {
			$('.language').slideToggle('slow');
		});
   });	
});

