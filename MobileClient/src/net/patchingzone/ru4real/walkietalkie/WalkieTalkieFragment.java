package net.patchingzone.ru4real.walkietalkie;

import net.patchingzone.ru4real.Network;
import net.patchingzone.ru4real.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class WalkieTalkieFragment extends Fragment {

	Button btnRecord;
	// LinearLayout main;
	String file;
	PlayerRecorder r;
	FileManager f;

	private View v;
	private EditText channel;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		v = inflater.inflate(R.layout.fragment_walkie_talkie, container, false);
		/*
		 * StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		 * .detectAll() .penaltyLog() .penaltyDialog() .build());
		 * StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll()
		 * .penaltyLog() .build());
		 */

		btnRecord = (Button) v.findViewById(R.id.BTN_record);
		btnRecord.setVisibility(View.GONE);
		channel = (EditText) v.findViewById(R.id.ET_channel);
		btnRecord.setVisibility(View.VISIBLE);

		channel.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_UP) {
					Network.channel = channel.getText().toString();
				}
				return false;
			}
		});

		return v;

	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Network network = new Network();
		network.connectWalkieTalkie();

		String PhoneId = android.os.Build.MODEL;
		file = Environment.getExternalStorageDirectory().getPath() + "/" + PhoneId + ".3gp";

		r = new PlayerRecorder();
		f = new FileManager();

		btnRecord.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					btnRecord.setText("Pressed");
					btnRecord.setBackgroundResource(R.color.red);
					r.Start(file);

					break;
				case MotionEvent.ACTION_UP:
					btnRecord.setText("not pressed");
					btnRecord.setBackgroundResource(R.color.green);
					
					r.Stop();

					new Thread(new Runnable() {
						public void run() {
							String lastUpload = f.upload(file);
							if (lastUpload.length() > 10) {
								f.BroadcastFile(lastUpload);
								Log.d("uploadid", lastUpload);
								// connection.send(lastUpload);
							}

						}
					}).start();
					
					break;
				case MotionEvent.ACTION_CANCEL:
					btnRecord.setText("canceled");
					
					break;

				default:
					break;
				}

				return false;
			}
		});
		
		/*
		btnRecord.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!r.active) {
					r.Start(file);
					btnRecord.setText("Stop");
				} else {
					r.Stop();

					new Thread(new Runnable() {
						public void run() {
							String lastUpload = f.upload(file);
							if (lastUpload.length() > 10) {
								f.BroadcastFile(lastUpload);
								Log.d("uploadid", lastUpload);
								// connection.send(lastUpload);
							}

						}
					}).start();

					btnRecord.setText("Record");
				}
			}
		});
		*/


	}

	public void newPhone() {

		Thread t = new Thread("Thread1") {
			@Override
			public void run() {
				// some code #2

				while (true) {
					if (Network.name != "") {
						getActivity().runOnUiThread(new Runnable() {
							public void run() {
								// some code #3 (that needs to be ran in UI
								// thread)

								// TODO
							}
						});
						Network.name = "";
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		t.start();

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			Log.d("volume", "pressed");
			if (!r.active) {
				r.Start(file);
				btnRecord.setText("Stop");
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (r.active && keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			Log.d("volume", "stop");
			r.Stop();

			new Thread(new Runnable() {
				public void run() {
					String lastUpload = f.upload(file);
					if (lastUpload.length() > 10) {
						f.BroadcastFile(lastUpload);
						Log.d("uploadid", lastUpload);
						// connection.send(lastUpload);
					}

				}
			}).start();
			btnRecord.setText("Record");
			return true;
		} else {
			return false;
		}
	}
}
