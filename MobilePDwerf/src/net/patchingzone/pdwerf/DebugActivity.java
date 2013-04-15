package net.patchingzone.pdwerf;

import java.util.Arrays;

import org.puredata.android.service.PdService;
import org.puredata.core.PdBase;
import org.puredata.core.PdReceiver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class DebugActivity extends Activity {

	private String TAG = "qq";
	private Context c;
	LinearLayout ll;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.debug_layout);
		c = this; 
		
		//UI binding 
		bindButton(R.id.e, 0); // 40);
		bindButton(R.id.a, 1); // 45);
		bindButton(R.id.d, 2); // 50);
		bindButton(R.id.g, 3); // 55);
		bindButton(R.id.b, 4); // 59);
		bindButton(R.id.ee, 5); // 64);
		
		ll = (LinearLayout) findViewById(R.id.logWindow);
		final EditText pdName = (EditText) findViewById(R.id.pdName);
		final EditText pdValue = (EditText) findViewById(R.id.pdValue);
		
		Button btn = (Button) findViewById(R.id.pdSend);
		btn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String name = pdName.getText().toString();
				String value = pdValue.getText().toString();
				
				Log.d(TAG, name + " " + value);
				AudioService.sendMessage(name, value);
			}
		});

		PdBase.setReceiver(receiver);
		PdBase.subscribe("android");
		//start pure data sound engine 
		bindService(new Intent(this, PdService.class),
				AudioService.pdConnection, BIND_AUTO_CREATE);
		initSystemServices();
	}

	public void addText(final String text, final boolean left) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				//TextView txt = new TextView(c);
				//txt.setText(text);
				//txt.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				
				TextView txt; // = new TextView(c);
				//txt.setLayout(R.layout.view_textview);
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
	
	
	public void bindButton(int resource, final int value) {
		Button btn = (Button) findViewById(resource);
		btn.setSoundEffectsEnabled(false);

		btn.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
			
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Log.d("qq", "action_down"); 
					AudioService.sendBang(value);

					// btn.setPressed(true);
					// Set whatever color you want to set

				} else {
					// btn.setPressed(false);
				}
				return false;
			}
		});

	}
	
	
	private Toast toast = null;

	private void toast(final String msg) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (toast == null) {
					toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
				}
				toast.setText(TAG + ": " + msg);
				toast.show();
			}
		});
	}

	private PdReceiver receiver = new PdReceiver() {

		private void pdPost(String msg) {
			toast("Pure Data says, \"" + msg + "\"");
			Log.d("qq", "pd post" + msg);
		}

		@Override
		public void print(String s) {
			Log.d("qq", "pd >>" + s);
			addText(s, true);
		}

		@Override
		public void receiveBang(String source) {
			pdPost("bang");
		}

		@Override
		public void receiveFloat(String source, float x) {
			pdPost("float: " + x);
		}

		@Override
		public void receiveList(String source, Object... args) {
			pdPost("list: " + Arrays.toString(args));
		}

		@Override
		public void receiveMessage(String source, String symbol, Object... args) {
			pdPost("message: " + Arrays.toString(args));
		}

		@Override
		public void receiveSymbol(String source, String symbol) {
			pdPost("symbol: " + symbol);
		}
	};
	
	

	
	private void initSystemServices() {
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(new PhoneStateListener() {
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				if (AudioService.pdService == null)
					return;
				if (state == TelephonyManager.CALL_STATE_IDLE) {
					AudioService.start();
				} else {
					AudioService.pdService.stopAudio();
				}
			}
		}, PhoneStateListener.LISTEN_CALL_STATE);
	}

	//

	@Override
	protected void onResume() {
		super.onResume();
		// PdAudio.startAudio(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// PdAudio.stopAudio();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		unbindService(AudioService.pdConnection);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
