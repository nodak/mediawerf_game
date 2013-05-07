package net.patchingzone.ru4real.walkietalkie;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.patchingzone.ru4real.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;
import android.util.Log;

public class FileManager {

	public String ip = Network.walkieTalkieFilesAddress;

	public FileManager() {

	}

	public String upload(String File) {
		Log.e("upload", "start");
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;
		DataInputStream inputStream = null;

		// String pathToOurFile = "/data/file_to_send.mp3";
		String pathToOurFile = File;
		String urlServer = "http://" + ip + "/handle_upload.php";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;

		try {
			FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile));

			URL url = new URL(urlServer);
			connection = (HttpURLConnection) url.openConnection();

			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			// Enable POST method
			connection.setRequestMethod("POST");

			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile
					+ "\"" + lineEnd);
			outputStream.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// Read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// Responses from the server (code and message)
			int serverResponseCode = connection.getResponseCode();
			String serverResponseMessage = connection.getResponseMessage();
			// String test = connection.getInputStream();

			StringBuffer text = new StringBuffer();
			InputStreamReader in = new InputStreamReader((InputStream) connection.getContent());
			BufferedReader buff = new BufferedReader(in);
			String line;
			do {
				line = buff.readLine();
				text.append(line + "\n");
			} while (line != null);

			/*
			 * Log.d("serverResponseCode", serverResponseCode+ "");
			 * Log.d("serverResponseMessage", serverResponseMessage);
			 * Log.d("test", text.toString());
			 */
			fileInputStream.close();
			outputStream.flush();
			outputStream.close();
			return text.toString();
		} catch (Exception ex) {
			Log.e("upload", ex.toString());
			return null;
		}
	}

	public boolean download(String naam) {
		try {
			URL url = new URL("http://" + ip + "/talk/" + naam);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.connect();

			File sdcard = Environment.getExternalStorageDirectory();
			File file = new File(sdcard, "TMP-audio.3gp");

			FileOutputStream fileOutput = new FileOutputStream(file);
			InputStream inputStream = urlConnection.getInputStream();

			byte[] buffer = new byte[1024];
			int bufferLength = 0;

			while ((bufferLength = inputStream.read(buffer)) > 0) {
				fileOutput.write(buffer, 0, bufferLength);
			}
			fileOutput.close();
			return true;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void BroadcastFile(String name) {
		JSONArray ja = new JSONArray();
		JSONObject ob = new JSONObject();

		String tmp[] = name.split("\n");

		try {
			ob.put("url", tmp[0]);
			ob.put("channel", Network.channel);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ja.put(ob);
		// ja.put(MainApp.chanel);

		try {
			Network.walkieTalkieWebSocket.emit("fileUp", ja);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
