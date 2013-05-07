package net.patchingzone.ru4real.nuevo;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringEscapeUtils;

import android.os.Environment;
import android.util.Log;

/**
 * An example of subclassing NanoHTTPD to make a custom HTTP server.
 */
public class MyHTTPServer extends NanoHTTPD {
	public MyHTTPServer() throws IOException {
		super(8080);
	}

	public Response serve(String uri, String method, Properties header, Properties parms,
			Properties files) {
		Log.d("HTTP Request", "" + method + " '" + uri + " " + /* header + */ " " + parms);

		String escapedCode = parms.getProperty("code");
		String unescapedCode = StringEscapeUtils.unescapeEcmaScript(escapedCode);
		Log.d("HTTP Code", "" + escapedCode); 
		Log.d("HTTP Code", "" + unescapedCode); 
		
		
		File sdDir = Environment.getExternalStorageDirectory();
		// create a File object for the parent directory
		File servingFolder = new File(sdDir + "/prototypr"); 
		// have the object build the directory structure, if needed. 
	
		return serveFile(uri, header, servingFolder, true);

	}

	
}
