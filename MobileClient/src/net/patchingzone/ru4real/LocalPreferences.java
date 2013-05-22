package net.patchingzone.ru4real;

import net.patchingzone.ru4real.base.MainApp;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LocalPreferences extends Activity {
	
	MainApp ma =(MainApp)getApplication();
	EditText Server_IP, Server_Port, Phone_ID;
	Button Set, Get;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_settings);
		
		/*
		Server_IP = (EditText) findViewById(R.id.Server_IP_textbox);
		Server_Port = (EditText) findViewById(R.id.Server_Port_textbox);
		Phone_ID = (EditText) findViewById(R.id.Phone_ID_textbox);
		Set = (Button) findViewById(R.id.Settings_save_bt);
		*/
		
		
		
		//fillSettings();
		
		Set.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences.Editor editor = null; // = MainApp.app_preferences.edit();
				
				editor.putString("IP", Server_IP.getText().toString().trim());
				//Log.d("ServerIP" , Server_IP.getText().toString().trim());
				editor.commit();
				
				editor.putString("Port", Server_Port.getText().toString().trim());
				//Log.d("ServerPort" , Server_Port.getText().toString().trim());
				editor.commit();
				
				editor.putString("ID", Phone_ID.getText().toString().trim());
				//Log.d("PhoneID" , Phone_ID.getText().toString().trim());
				editor.commit();
				
				Toast.makeText(getApplicationContext(), "Settings Saved", Toast.LENGTH_SHORT).show();
				
				finish();
			}
		});	
	}
	
	/*
	public void fillSettings(){
		String ServerIP = MainApp.app_preferences.getString("IP", "");
		Server_IP.setText(ServerIP);
		//Log.d("ServerIP", ServerIP);
		
		String ServerPort = MainApp.app_preferences.getString("Port", "");
		Server_Port.setText(ServerPort);
		//Log.d("ServerPort" , ServerPort);
		
		String PhoneID = MainApp.app_preferences.getString("ID", "");
		Phone_ID.setText(PhoneID);
		//Log.d("PhoneID", PhoneID); 
		
	} 
	*/
	
}
