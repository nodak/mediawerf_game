package net.patchingzone.ru4real.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import net.patchingzone.ru4real.L;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;
import android.util.Log;

public class AppSettings {

	static AppSettings appSettings = new AppSettings();

	// == APP SETTINGS ==========
	public static boolean debug = true;
	public static boolean fullscreen = true;
	public static boolean portrait = true;
	public static boolean stayAwake = true;
	public static boolean overrideHomeButtons = false;
	public static boolean overrideVolumeButtons = false;
	public static boolean hideHomeBar = true;
	public static boolean screenAlwaysOn = true;
	public static boolean closeWithBack = true;

	public static boolean readFromAssets = true;

	protected static String fileSettings =  "settings_0.1.txt";

	// == GAME SETTINGS ==========

	public static String playerID = "unknown";
	public String libraryAddress = "http://outside.mediawerf.net:8080";
	public String walkieTalkieAddress = "http://outside.mediawerf.net:8082";
	public String serverAddress = "http://outside.mediawerf.net:7080";

	public static final String SETTINGS_PLAYERID = "playerID";
	public static final String SETTINGS_LIBRARY_ADDRESS = "libraryAddress";
	public static final String SETTINGS_SERVER_ADDRESS = "serverAddress";
	public static final String SETTINGS_WALKIE_TALKIE_ADDRESS = "walkieTalkieAddress";

	// static String SERVER_PORT = "7080";

	public AppSettings AppSettings() {
		//load();
		return appSettings;

	}

	public static AppSettings get() {
		return appSettings;
	}

	public void load() {
		Log.d("FILE load()", "");

		Thread t = new Thread(new Runnable() {
			String fileURI = Environment.getExternalStorageDirectory() + File.separator + "areyouforreal"
					+ File.separator + AppSettings.fileSettings ;


			@Override
			public void run() {

				File file = new File(fileURI);
				StringBuffer contents = new StringBuffer();
				BufferedReader reader = null;

				Log.d("FILE run", "" + fileURI);

				try {
					String text = null;
					reader = new BufferedReader(new FileReader(file));

					// repeat until all lines is read
					while ((text = reader.readLine()) != null) {
						contents.append(text).append(System.getProperty("line.separator"));
						Log.d("FILE repeat", "" + fileURI);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					// writing defaults
					save();
				} catch (IOException e) {
					e.printStackTrace();

				} finally {
					try {
						if (reader != null) {
							reader.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				JSONObject jsonObject;
				try {
					Log.d("FILE", contents.toString());
					jsonObject = new JSONObject(contents.toString());
					playerID = (String) jsonObject.getString(SETTINGS_PLAYERID);
					serverAddress = (String) jsonObject.getString(SETTINGS_SERVER_ADDRESS);
					libraryAddress = (String) jsonObject.getString(SETTINGS_LIBRARY_ADDRESS);
					walkieTalkieAddress = (String) jsonObject.getString(SETTINGS_WALKIE_TALKIE_ADDRESS);
					Log.d("FILE", "playerID" + playerID);
					Log.d("FILE", "serverAddress" + serverAddress);
					Log.d("FILE", "libraryAddress" +  libraryAddress);
					Log.d("FILE", "wTAddress" +  walkieTalkieAddress);
				} catch (JSONException e) {
					e.printStackTrace();

					// writing defaults
					save();
				}
			}
		});

		t.start();

	}

	public void save() {

		L.d("JSON", "save()");

		Thread t = new Thread(new Runnable() {
			String fileDIR = Environment.getExternalStorageDirectory() + File.separator + "areyouforreal"
					+ File.separator;
			String fileURI = fileDIR + AppSettings.fileSettings ;

			@Override
			public void run() {
				L.d("JSON", "save() run");

				File dir = new File(fileDIR); 
				if (dir.exists() == false) {
					dir.mkdirs(); 
				}
				
				File file = new File(fileURI);
				
				String content = null;

				JSONObject jsonObject;

				try {
					jsonObject = new JSONObject();
					jsonObject.put(SETTINGS_PLAYERID, playerID);
					jsonObject.put(SETTINGS_SERVER_ADDRESS, serverAddress);
					jsonObject.put(SETTINGS_LIBRARY_ADDRESS, libraryAddress);
					jsonObject.put(SETTINGS_WALKIE_TALKIE_ADDRESS, walkieTalkieAddress);
					content = jsonObject.toString();

				} catch (JSONException e) {
					e.printStackTrace();
				}

				FileOutputStream fos = null;
				Writer out = null;
				try {
					fos = new FileOutputStream(file);
					out = new OutputStreamWriter(fos, "UTF-8");

					out.write(content);
					out.flush();
				} catch (Throwable e) {
					e.printStackTrace();
				} finally {
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException ignored) {
						}
					}
					if (out != null) {
						try {
							out.close();
						} catch (IOException ignored) {
						}
					}
				}
			}
		});

		t.start();

	}

}
