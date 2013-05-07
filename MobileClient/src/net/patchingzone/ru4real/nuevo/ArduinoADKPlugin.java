/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.patchingzone.ru4real.nuevo;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import net.patchingzone.ru4real.base.BaseActivity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.future.usb.UsbAccessory;
import com.android.future.usb.UsbManager;

public class ArduinoADKPlugin extends BaseActivity {
	private static final String TAG = "DemoKit";

	private static final String ACTION_USB_PERMISSION = "com.google.android.DemoKit.action.USB_PERMISSION";

	private UsbManager mUsbManager;
	private PendingIntent mPermissionIntent;
	private boolean mPermissionRequestPending;

	UsbAccessory mAccessory;
	ParcelFileDescriptor mFileDescriptor;
	FileInputStream mInputStream;
	FileOutputStream mOutputStream;

	private static final int MESSAGE_SWITCH = 1;
	public static final byte LED = 2;
	public static final byte LED_COMMAND_ON = 0;
	public static final byte LED_COMMAND_OFF = 1;

	Context c;

	// a button-controller with name buttonValue will change the
	// value of this variable when pressed.
	int buttonValue = 0;


	boolean status_start = false;

	int q = 0, min = 0, max = 0;


	protected class SwitchMsg {
		private byte sw;
		private byte state;

		public SwitchMsg(byte sw, byte state) {
			this.sw = sw;
			this.state = state;
		}

		public byte getSw() {
			return sw;
		}

		public byte getState() {
			return state;
		}
	}

	// eventos al enchufar y desenchufar
	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("qq", "DemoKitActivity - onReceive");
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbAccessory accessory = UsbManager.getAccessory(intent);
					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						openAccessory(accessory);
					} else {
						Log.d(TAG, "permission denied for accessory "
								+ accessory);
					}
					mPermissionRequestPending = false;
				}
			} else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
				UsbAccessory accessory = UsbManager.getAccessory(intent);
				if (accessory != null && accessory.equals(mAccessory)) {
					closeAccessory();
				}
			}
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		c = this;
		Log.d("qq", "DemoKitActivity - onCreate");

		mUsbManager = UsbManager.getInstance(this);
		mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(
				ACTION_USB_PERMISSION), 0);
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
		registerReceiver(mUsbReceiver, filter);

		if (getLastNonConfigurationInstance() != null) {
			mAccessory = (UsbAccessory) getLastNonConfigurationInstance();
			openAccessory(mAccessory);
		} 
		

		SharedPreferences settings = c.getSharedPreferences("preferen", c.MODE_PRIVATE); 
		// p.edit().clear();
		// p.edit().commit();

		min = settings.getInt("min", 252); 
		max = settings.getInt("max", 1252); 
	
		
		
	}
	
	public void popup(final int type) { 

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Title");
		alert.setMessage("Message");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Editable value = input.getText();
				
				switch (type) {
			
				case 1:
					max = Integer.parseInt(value.toString());
					
					break;

					
				case 2:
					min = Integer.parseInt(value.toString());
					
					break;
					
				default:
					break;
				} 
				

				SharedPreferences settings = c.getSharedPreferences("preferen", c.MODE_PRIVATE); 
				// p.edit().clear();
				// p.edit().commit();

				SharedPreferences.Editor editor = settings.edit();

				editor.putInt("min", min);
				editor.putInt("max", max);

				editor.commit(); 



			} 
		}); 

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});

		alert.show();

	}

	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, 1, 0, "max"); 
		menu.add(0, 2, 0, "min"); 

		return true;

	} 

	/* 
	@Override
	public Object onRetainNonConfigurationInstance() {
		Log.d("qq", "DemoKitActivity - onRetainNonConfiguration");

		if (mAccessory != null) {
			return mAccessory;
		} else {
			return super.onRetainNonConfigurationInstance();
		}
	} 
	*/ 

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case 1: 
			popup(1); 

			return true;

		case 2:
			popup(2); 

			return true;

		}

		return false;

	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d("qq", "DemoKitActivity - onResume");

		Intent intent = getIntent();
		if (mInputStream != null && mOutputStream != null) {
			return;
		}

		UsbAccessory[] accessories = mUsbManager.getAccessoryList();
		UsbAccessory accessory = (accessories == null ? null : accessories[0]);
		if (accessory != null) {
			if (mUsbManager.hasPermission(accessory)) {
				openAccessory(accessory);
			} else {
				synchronized (mUsbReceiver) {
					if (!mPermissionRequestPending) {
						mUsbManager.requestPermission(accessory,
								mPermissionIntent);
						mPermissionRequestPending = true;
					}
				}
			}
		} else {
			Log.d(TAG, "mAccessory is null");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		closeAccessory();
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mUsbReceiver);
		super.onDestroy();
	}

	private void openAccessory(UsbAccessory accessory) {
		Log.d("qq", "DemoKitActivity - openaccesory");
		Toast.makeText(this, "open", Toast.LENGTH_SHORT).show();

		mFileDescriptor = mUsbManager.openAccessory(accessory);
		if (mFileDescriptor != null) {
			mAccessory = accessory;
			FileDescriptor fd = mFileDescriptor.getFileDescriptor();
			mInputStream = new FileInputStream(fd);
			mOutputStream = new FileOutputStream(fd);
			Thread thread = new Thread(null, new Arduino(), "DemoKit");
			thread.start();
			Log.d(TAG, "accessory opened");
		} else {
			Log.d(TAG, "accessory open fail");
		}
	}

	private void closeAccessory() {
		Log.d("qq", "DemoKitActivity - closeAccesory");

		try {
			if (mFileDescriptor != null) {
				mFileDescriptor.close();
			}
		} catch (IOException e) {
		} finally {
			mFileDescriptor = null;
			mAccessory = null;
		}
	}

	Thread t = new Thread();

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_SWITCH:
				SwitchMsg o = (SwitchMsg) msg.obj;
				Log.d("qq", o.toString());
				handleSwitchMessage(o);
				break;

			}
		}
	};

	public void sendCommand(byte command, byte target, int value) {
		byte[] buffer = new byte[3];
		if (value > 255)
			value = 255;

		buffer[0] = command;
		buffer[1] = target;
		buffer[2] = (byte) value;
		Toast.makeText(this, buffer.toString(), Toast.LENGTH_SHORT).show();
		if (mOutputStream != null && buffer[1] != -1) {
			try {
				mOutputStream.write(buffer);
				Log.d(TAG, "write ok");
			} catch (IOException e) {
				Log.e(TAG, "write failed", e);
			}
		}
	} 
	
	public void sendPinValue(int pin, int value) {
		byte[] buffer = new byte[2];
		if (value > 255)
			value = 255;

		buffer[0] = (byte) pin;
		buffer[1] = (byte) value;

		if (mOutputStream != null && buffer[1] != -1) {
			try {
				mOutputStream.write(buffer);
				Log.d(TAG, "write ok");
			} catch (IOException e) {
				Log.e(TAG, "write failed", e);
			}
		}
	} 
	

	protected void handleSwitchMessage(SwitchMsg o) {
	}

	private int composeInt(byte hi, byte lo) {
		int val = (int) hi & 0xff;
		val *= 256;
		val += (int) lo & 0xff;
		return val;
	}

	class Arduino implements Runnable {

		public void run() {
			int ret = 0;
			byte[] buffer = new byte[16384];
			int i;

			while (ret >= 0 && mAccessory != null) {
				try {
					ret = mInputStream.read(buffer);
				} catch (IOException e) {
					break;
				}

				i = 0;
				while (i < ret) {
					int len = ret - i;

					switch (buffer[i]) {
					case 0x1:
						if (len >= 3) {
						

							q = composeInt(buffer[i + 1], buffer[i + 2]);
							// Toast.makeText(c, "hola", 252);
							Log.d("qq", "" + q);

						}
						i += 3;
						break;

					default:
						Log.d(TAG, "unknown msg: " + buffer[i]);
						i = len;
						break;
					}
				}

			}
		}

	}

}
