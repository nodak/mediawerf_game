package net.patchingzone.ru4real.nuevo;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Scanner;

import net.patchingzone.ru4real.R;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends FragmentActivity {




	private String server = "";
	private String user = "";
	private String password = "";
	private String serverRoad = "";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	

 
		/*
		 * try { new MyServer(); Log.d("qq", "" + getLocalIpAddress()); } catch
		 * (IOException ioe) { System.err.println("Couldn't start server:\n" +
		 * ioe); // System.exit( -1 ); }
		 */

		//
		// String subnet = "192.168.40.1";
		// SubnetUtils utils = new SubnetUtils(subnet);
		// SubnetInfo info = utils.getInfo();
		//
		// System.out.printf("Subnet Information for %s:\n", subnet);
		// System.out.println("--------------------------------------");
		// System.out.printf("IP Address:\t\t\t%s\t[%s]\n", info.getAddress(),
		// Integer.toBinaryString(info.asInteger(info.getAddress())));
		// System.out.printf("Netmask:\t\t\t%s\t[%s]\n", info.getNetmask(),
		// Integer.toBinaryString(info.asInteger(info.getNetmask())));
		// System.out.printf("CIDR Representation:\t\t%s\n\n",
		// info.getCidrSignature());
		//
		// System.out.printf("Supplied IP Address:\t\t%s\n\n",
		// info.getAddress());
		//
		// System.out.printf("Network Address:\t\t%s\t[%s]\n",
		// info.getNetworkAddress(),
		// Integer.toBinaryString(info.asInteger(info.getNetworkAddress())));
		// System.out.printf("Broadcast Address:\t\t%s\t[%s]\n",
		// info.getBroadcastAddress(),
		// Integer.toBinaryString(info.asInteger(info.getBroadcastAddress())));
		// System.out.printf("Low Address:\t\t\t%s\t[%s]\n",
		// info.getLowAddress(),
		// Integer.toBinaryString(info.asInteger(info.getLowAddress())));
		// System.out.printf("High Address:\t\t\t%s\t[%s]\n",
		// info.getHighAddress(),
		// Integer.toBinaryString(info.asInteger(info.getHighAddress())));
		//
		// System.out.printf("Total usable addresses: \t%d\n",
		// Integer.valueOf(info.getAddressCount()));
		// System.out.printf("Address List: %s\n\n",
		// Arrays.toString(info.getAllAddresses()));

		Scanner scanner = new Scanner("192.168.40.1");
		while (scanner.hasNextLine()) {
			String address = scanner.nextLine();
			Log.d("qq2", "" + address);
			
		}

		/*
		 * try { FTPClient ftpClient = new FTPClient();
		 * ftpClient.connect(InetAddress.getByName(server));
		 * ftpClient.enterLocalPassiveMode(); ftpClient.login(user, password);
		 * ftpClient.changeWorkingDirectory(serverRoad);
		 * ftpClient.setFileType(FTP.BINARY_FILE_TYPE); BufferedInputStream
		 * buffIn=null; //buffIn = new BufferedInputStream(new
		 * FileInputStream(file)); ftpClient.enterLocalPassiveMode();
		 * ftpClient.storeFile("test.txt", buffIn); buffIn.close();
		 * ftpClient.logout(); ftpClient.disconnect(); } catch (SocketException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); } catch
		 * (UnknownHostException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */



		final EditText editText = null; //(EditText) findViewById(R.id.editText1);

		Button btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String qq = editText.getText().toString();
				final Handler mHandler = new Handler();

				// final String qq = "";

				final String[] q = qq.split(",");
				for (String string : q) {
					Log.d("qq", "la string es " + string);
				}

				final Runnable runMethod = new Runnable() {
					public void run() {

						int num = count % (q.length);
						Log.d("mm", "num " + q.length + " " + num);
						String msg = q[num];
						Log.d("mm", "msg " + msg);
						count = count + 1;

						activateWifi(false, "");
						activateWifi(true, msg);

						mHandler.postDelayed(this, 12000);
					}
				};

				mHandler.postDelayed(runMethod, 12000);

			}
		});



	}

	int count = 0;

	public String TAG = "qq";
	private Object mIsWifiEnabled = true;

	public void activateWifi(boolean enabled, String wifiName) {

		WifiManager wifi = (WifiManager) getSystemService(this.WIFI_SERVICE);
		Method[] wmMethods = wifi.getClass().getDeclaredMethods();
		Log.d(TAG, "enableMobileAP methods " + wmMethods.length);
		for (Method method : wmMethods) {
			Log.d(TAG, "enableMobileAP method.getName() " + method.getName());
			if (method.getName().equals("setWifiApEnabled")) {
				WifiConfiguration netConfig = new WifiConfiguration();
				netConfig.SSID = wifiName;
				netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
				netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
				netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
				netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

				//
				try {
					Log.d(TAG, "enableMobileAP try: ");
					method.invoke(wifi, netConfig, enabled);
					if (netConfig.wepKeys != null && netConfig.wepKeys.length >= 1)
						Log.d(TAG, "enableMobileAP key : " + netConfig.wepKeys[0]);
					Log.d(TAG, "enableMobileAP enabled: ");
					mIsWifiEnabled = enabled;
				} catch (Exception e) {
					Log.e(TAG, "enableMobileAP failed: ", e);
				}
			}
		}
	}

	void doit(String code) {
		// Create an execution environment.
		Context cx = Context.enter();

		// Turn compilation off.
		cx.setOptimizationLevel(-1);

		try {
			// Initialize a variable scope with bindnings for
			// standard objects (Object, Function, etc.)
			Scriptable scope = cx.initStandardObjects();

			// Set a global variable that holds the activity instance.
			ScriptableObject.putProperty(scope, "ActivityContext", Context.javaToJS(this, scope));

			// Evaluate the script.
			Object result = cx.evaluateString(scope, code, "doit:", 1, null);
			// cx.evaluateString(scope, "Toast.makeText('hola')", "qq", 1,
			// null);
			Log.d("qq", result.toString());
		} finally {
			// Context.exit();
		}

	}

	/*
	 * @Override public boolean dispatchTouchEvent(MotionEvent ev) {
	 * super.dispatchTouchEvent(ev);
	 * 
	 * 
	 * if (q.onTouchEvent(ev)) { return true; // btn2.onTouchEvent(ev); } else {
	 * return false; }
	 * 
	 * }
	 */



	public String getLocalIpAddress() {
		try {

			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("address", ex.toString());
		}
		return null;
	}
}
