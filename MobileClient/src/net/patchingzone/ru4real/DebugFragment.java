package net.patchingzone.ru4real;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ShowToast")
public class DebugFragment extends Fragment {

	private String TAG = "qq";
	private Context c;
	LinearLayout ll;
	private View v;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.activity_debug_sound_2, container, false);
		
		return v;
	};

	public void onActivityCreated(Bundle savedInstanceState) {
		c = getActivity();

		// UI binding

		ll = (LinearLayout) v.findViewById(R.id.logWindow);
		final EditText pdName = (EditText) v.findViewById(R.id.pdName);
		final EditText pdValue = (EditText) v.findViewById(R.id.pdValue);

		Button btn = (Button) v.findViewById(R.id.pdSend);
		btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String name = pdName.getText().toString();
				String value = pdValue.getText().toString();

				Log.d(TAG, name + " " + value);
				// AudioService.sendMessage(name, value);
			}
		});

	}

	public void addText(final String text, final boolean left) {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TextView txt = new TextView(c);
				// txt.setText(text);
				// txt.setLayoutParams(new
				// LayoutParams(LayoutParams.FILL_PARENT,
				// LayoutParams.WRAP_CONTENT));

				TextView txt; // = new TextView(c);
				// txt.setLayout(R.layout.view_textview);
				if (left == true) {
					txt = (TextView) View.inflate(c, R.layout.view_textview_receive, null);
				} else {
					txt = (TextView) View.inflate(c, R.layout.view_textview_send, null);
				}
				txt.setText(text);
				ll.addView(txt);
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
		// PdAudio.startAudio(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		// PdAudio.stopAudio();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

}
