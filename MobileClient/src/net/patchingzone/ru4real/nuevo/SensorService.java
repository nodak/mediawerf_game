package net.patchingzone.ru4real.nuevo;


public class SensorService {
/*
	private AudioPCMManager pcm;
	private AudioListener audioListener = null;
	private BroadcastReceiver smsListener;
	private BroadcastReceiver callIncomingListener;
	private BroadcastReceiver callOutgoingListener;
	private BroadcastReceiver onBatteryChanged;
	private BroadcastReceiver btReceiver;

	SensorManager sm;

	SensorEventListener light;
	SensorEventListener proximity;
	SensorEventListener orientation;
	SensorEventListener magnetic;

	private Geocoder geocoder = null;



		final BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		mBtAdapter.startDiscovery();

		btReceiver = new BroadcastReceiver() {

			private List<Address> mArrayAdapter;
			private BluetoothAdapter device;

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();

				// When discovery finds a device
				if (BluetoothDevice.ACTION_FOUND.equals(action)) { // Get the
					// BluetoothDevice
					// object
					// from the
					// Intent
					// BluetoothDevice
					// device =

					intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE); // Add
					// the
					// name and address to an array adapter to show in a //
					// ListView //
					// mArrayAdapter.add(device.getName() + "\n" +
					// device.getAddress());

					// Log.d(MyApp.TAG + TAG, "" + device.getName() + " " +
					// device.getAddress());

				} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
					Log.v(TAG, "Entered the Finished "); //
					mBtAdapter.startDiscovery();
				}

			}
		};

		// Register the BroadcastReceiver
		// IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		// registerReceiver(mReceiver, filter); // Don't forget to unregister
		// during onDestroy

		//SMS

		smsListener = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle myBundle = intent.getExtras();
				SmsMessage[] messages = null;
				StringBuffer strMessage = new StringBuffer();

				if (myBundle != null) {
					Object[] pdus = (Object[]) myBundle.get("pdus");
					messages = new SmsMessage[pdus.length];

					for (int i = 0; i < messages.length; i++) {
						messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
						strMessage.append("SMS From: " + messages[i].getOriginatingAddress());
						strMessage.append(" : ");
						strMessage.append(messages[i].getMessageBody());
						strMessage.append("\n");

						//TODO 
						// messages[i].getOriginatingAddress();
						// messages[i].getMessageBody();
		
					}
				}
			}

		};

		//Incomming call

		callIncomingListener = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle bundle = intent.getExtras();

				Log.d("qq", "call received");

				if (null == bundle)
					return;

				String state = bundle.getString(TelephonyManager.EXTRA_STATE);

				if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
					String phonenumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

					String info = "Detect Calls sample application\nIncoming number: "
							+ phonenumber;

					Toast.makeText(context, info, Toast.LENGTH_LONG).show();

					//TODO 
					//phonenumber;

				}
			}

		};

		//Outgoing call 

		callOutgoingListener = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle bundle = intent.getExtras();

				if (null == bundle)
					return;

				String phonenumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

				String info = "Detect Calls sample application\nOutgoing number: " + phonenumber;

				Toast.makeText(context, info, Toast.LENGTH_LONG).show();

				//TODO
				//phonenumber;
			}

		};

		onBatteryChanged = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				int pct = 100 * intent.getIntExtra("level", 1) / intent.getIntExtra("scale", 1);

				Log.d("qq", "battery " + pct);
				// bar.setProgress(pct);
				// level.setText(String.valueOf(pct));

				switch (intent.getIntExtra("status", -1)) {
				case BatteryManager.BATTERY_STATUS_CHARGING:
					int plugged = intent.getIntExtra("plugged", -1);
					
					if (plugged == BatteryManager.BATTERY_PLUGGED_AC) { 
						Log.d("qq", "corriente"); 
					
					} else if (plugged == BatteryManager.BATTERY_PLUGGED_USB) {
						Log.d("qq", "usb"); 
						
					} else {
						Log.d("qq", "otro"); 
						
					}

					break;

				case BatteryManager.BATTERY_STATUS_DISCHARGING:

					break;
				case BatteryManager.BATTERY_STATUS_FULL:

					break;

				default:

					break;
				}
			}
		};

	


	// AudioPCM

		pcm = new AudioPCMManager(AudioPCMManager.LOW_QUALITY);
		pcm.setListener(audioListener = new AudioListener() {

			@Override
			public void onPowerChanged(float dbm) {

				valuesVolume[0] = dbm;
				osc.send(OSCMessageType.OSC_SOUND_DBM, valuesVolume);
				if (listener != null)
					listener.onSoundVolumeChange(dbm);
			}

			@Override
			public void onBlowChanged(boolean detected) {

			}

			@Override
			public void onBlowChanged(float dbm) {

				valuesBlow[0] = dbm;
				osc.send(OSCMessageType.OSC_SOUND_BLOW, valuesBlow);
				if (listener != null)
					listener.onBlowDetectChange(dbm);

			}
		});

		// Proximity
		sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		proximity = new SensorEventListener() {

			@Override
			public void onSensorChanged(SensorEvent event) {
				// Log.i(TAG, "sensor: " + event.sensor.getName() + ", values: "
				// + Arrays.toString(event.values));

				valuesProximity[0] = event.values[0];
				osc.send(OSCMessageType.OSC_PROXIMITY, valuesProximity);
				if (listener != null)
					listener.onProximityChange(event.values[0]);

			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// Log.i(TAG, "accuracy change: " + sensor + ", accuracy: " +
				// accuracy);

			}
		};

		//  Light

		light = new SensorEventListener() {

			@Override
			public void onSensorChanged(SensorEvent event) {

				valuesLight[0] = event.values[0];
				osc.send(OSCMessageType.OSC_LIGHT, valuesLight);
				if (listener != null)
					listener.onLightChange(event.values[0]);

			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {

			}
		};

	


			sm.registerListener(light, sm.getDefaultSensor(Sensor.TYPE_LIGHT),
					SensorManager.SENSOR_DELAY_FASTEST);
	

				sm.unregisterListener(light);


			sm.registerListener(proximity, sm.getDefaultSensor(Sensor.TYPE_PROXIMITY),
					SensorManager.SENSOR_DELAY_FASTEST);
	

				sm.unregisterListener(proximity);

	



			registerReceiver(smsListener, new IntentFilter(
					"android.provider.Telephony.SMS_RECEIVED"));
	

				unregisterReceiver(smsListener);

		





		// Register the BroadcastReceiver
		// Don't forget to unregister during onDestroy
		// registerReceiver(btReceiver, new
		// IntentFilter(BluetoothDevice.ACTION_FOUND));

		registerReceiver(callIncomingListener, new IntentFilter(
				"android.intent.action.PHONE_STATE"));
		registerReceiver(onBatteryChanged,
				new IntentFilter("android.intent.action.BATTERY_CHANGED"));

	}

	public void stop() {

	


		BroadcastReceiver headphoneplugged = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				//context.unregisterReceiver(this);

				String data = intent.getDataString();
				Bundle extraData = intent.getExtras();

				int st = intent.getIntExtra("state", 0);
				String nm = intent.getStringExtra("name"); 
				int mic = intent.getIntExtra("microphone", 0);
				//String all = String.format("st=%i, nm=%i, mic=%i", st, nm, mic);

				// intent didnt include extra info
				Log.d("qq", nm + " " + st +" " + nm +  " "); 
				//Log.d("qq", "" + data + " " + extraData + " " + all);
			}
		};

		IntentFilter headphoneFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG); 
		registerReceiver(headphoneplugged, headphoneFilter);

		BroadcastReceiver ScreenReceiver = new BroadcastReceiver() {
			public boolean wasScreenOn = true;

			@Override
			public void onReceive(Context context, Intent intent) {

				if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

					// DO WHATEVER YOU NEED TO DO HERE

					
					wasScreenOn = false; 
					Log.d("qq", "qq2");


				} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

					// AND DO WHATEVER YOU NEED TO DO HERE

					wasScreenOn = true;
					Log.d("qq", "qq5");


				}

			}

		}; 

		// INITIALIZE RECEIVER

		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);

		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(ScreenReceiver, filter);



	}


*/

}
