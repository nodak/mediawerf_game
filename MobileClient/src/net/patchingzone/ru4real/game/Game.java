package net.patchingzone.ru4real.game;

import net.patchingzone.ru4real.R;
import net.patchingzone.ru4real.R.anim;
import net.patchingzone.ru4real.R.id;
import net.patchingzone.ru4real.R.layout;
import net.patchingzone.ru4real.base.MainApp;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Game extends Activity {

	MainApp ma;
	public RelativeLayout body;
	public Animation fade;
		
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.game);
	    
	    body = (RelativeLayout) findViewById(R.id.gameBackground);
	    fade = AnimationUtils.loadAnimation(this, R.anim.fade);
    
	    ma = new MainApp();
	    
	    exit();	    
	    Color(ma.team);
	}
	
	public void  Color(final int color)
	{
		fade.setAnimationListener(new AnimationListener() {
	        @Override
			public void onAnimationStart(Animation anim)
	        {
	        	switch(color)
	       	 	{
		   	 		case 0:
		   	 			body.setBackgroundColor(android.graphics.Color.RED);
		   	   		break;
		   	 		case 1:
		   	 			body.setBackgroundColor(android.graphics.Color.GREEN);
	    			break;
		   	 		case 2:
		   	 			body.setBackgroundColor(android.graphics.Color.BLUE);
	    			break;
		   	 		case 3:
		   	 			body.setBackgroundColor(android.graphics.Color.YELLOW);
	    			break;
		    		default:
		    			body.setBackgroundColor(android.graphics.Color.WHITE);
	    			break;
	       	 	}	
	        };
	        @Override
			public void onAnimationRepeat(Animation anim)
	        {
	        };
	        @Override
			public void onAnimationEnd(Animation anim)
	        {
	        	body.setBackgroundColor(android.graphics.Color.BLACK);
	        };     
        });
		body.startAnimation(fade);
	}
	
	@Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        switch (keyCode) {
        case KeyEvent.KEYCODE_VOLUME_UP:
        case KeyEvent.KEYCODE_VOLUME_DOWN:
        	ma.ioWebSocket.emit("Shoot", new JSONArray().put("I pushed the button"));
        	return true;
        default:
        	return super.dispatchKeyEvent(event);
        }
    }
	
	@Override
	public void onBackPressed() {
	    // prevents player from leaving the game via the back button
		Toast.makeText(getApplicationContext(), "You shall not pass!", Toast.LENGTH_SHORT).show();

	}
	
	public void exit()
	{
		//waits for the server to end the game
		Thread exit = new Thread() {
			@Override
			public void run() {
				while(ma.gameActive)
				{
					try {
						sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				finish();
			}
		};
		exit.start();
	}
}