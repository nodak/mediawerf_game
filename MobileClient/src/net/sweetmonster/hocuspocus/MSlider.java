package net.sweetmonster.hocuspocus;

import net.patchingzone.ru4real.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MSlider extends LinearLayout {

	Context c;
	private SeekBar mSeekBar;
	private TextView mTextView;
	private TextView mTextView2;

	public MSlider(Context context) {
		super(context);
		this.c = context;

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.hocus_pocus_slider, this);

		addSlider();
	}

	public void addSlider() {

		mSeekBar = (SeekBar) findViewById(R.id.SeekBar); 
		mTextView = (TextView) findViewById(R.id.TextView1); 
		mTextView2 = (TextView) findViewById(R.id.TextView2); 
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
				// mTrackingText.setText("qq5");
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// mTrackingText.setText("qq2");
			}

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
			
				Log.d("qq", "qq"); 
				
				mTextView2.setText(Integer.toString(progress));
				// getString( R.string.seekbar_from_touch "q1")
			}

		}); 


	}

	public void setProperties(String string, float min, int max) { 
		mTextView.setText(string); 
		mSeekBar.setMax(max); 
	} 
	
	public void setSliderListerner(OnSeekBarChangeListener barChangeListener) { 
		mSeekBar.setOnSeekBarChangeListener(barChangeListener); 
	} 
}
