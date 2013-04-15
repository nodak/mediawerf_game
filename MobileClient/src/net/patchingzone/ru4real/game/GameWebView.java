package net.patchingzone.ru4real.game;

import net.patchingzone.ru4real.base.BaseWebview;
import android.os.Bundle;

public class GameWebView extends BaseWebview {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setPage("http://mediawerf.dyndns.org/fun/silent2.html");		
	    //setPage("http://www.meneame.net");	
    }
	

}
