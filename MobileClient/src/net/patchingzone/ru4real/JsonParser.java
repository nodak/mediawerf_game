package net.patchingzone.ru4real;

import java.io.IOException;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.util.Log;

public class JsonParser {
	
	public JsonParser(){
		
	}
	
	public static void SendMessage(String Message)
	{
		try {
			
			//MainApp.ioWebSocket.getWebSocket().sendMessage(Message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String SendJsonMessage(String Name,String[] Values)
	{	
		try {
			return (new JSONObject().put("args",Arrays.toString(Values)).put("name", Name)) + "";
			//MainApp.ioWebSocket.getWebSocket().sendMessage((new JSONObject().put("args",Arrays.toString(Values)).put("name", Name))+"");	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return e + "";
		}
	}
	
	public static String Parse(String Message)
	{
		try{
		JSONObject jsonObject = new JSONObject(Message);
		JSONArray jsonArray = jsonObject.getJSONArray("args");
		JSONObject jsonObject2 = jsonArray.getJSONObject(0);
		
		String gotMsg = jsonObject2.getString("message");
		
		Log.d("Chat", "The message is " + gotMsg);
		return gotMsg;
		
		}
		catch(JSONException Jex)
		{
			Log.d("error", "Message not parsed " + Jex);
			return Jex.toString();
		}
	}
}
