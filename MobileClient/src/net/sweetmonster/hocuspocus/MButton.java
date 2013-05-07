package net.sweetmonster.hocuspocus;

import net.patchingzone.ru4real.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MButton extends LinearLayout {

	Context c;
	Button mButton; 
	TextView mTextView; 

	public MButton(Context context) {
		super(context);
		this.c = context;

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.hocus_pocus_button, this);
		
		mButton = (Button) findViewById(R.id.button1); 
		mTextView = (TextView) findViewById(R.id.TextView1); 
		mTextView.setVisibility(View.GONE); 
		//addEventListener();
	}

	public void addEventListener(OnClickListener onClickEventListener) { 


		mButton.setOnClickListener(onClickEventListener); 

	}

	public void setName(String string) {
		mButton.setText(string); 
		mTextView.setText(string); 
	}
}
