package net.patchingzone.ru4real.fragments;

import net.patchingzone.ru4real.R;
import net.patchingzone.ru4real.base.AppSettings;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SettingsFragment extends Fragment {


	private View v;
	private Button loadButton;
	private Button saveButton;
	private EditText editTextServerAddress;
	private EditText editTextLibraryAddress;
	//private EditText editTextWalkieTalkieAddress;
	private EditText editTextPlayerID;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		v = inflater.inflate(R.layout.fragment_settings, container, false);
		loadButton = (Button) v.findViewById(R.id.btnLoadSettings);
		saveButton = (Button) v.findViewById(R.id.btnSaveSettings);
		
		editTextServerAddress = (EditText) v.findViewById(R.id.editServerAddress);
		editTextLibraryAddress = (EditText) v.findViewById(R.id.editLibraryAddress);
		//editTextWalkieTalkieAddress = (EditText) v.findViewById(R.id.editWalkieTalkieAddress);
		editTextPlayerID = (EditText) v.findViewById(R.id.editPhoneID);
		
		loadContentInUI(); 
		
		loadButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				AppSettings.get().load(); 
				loadContentInUI();
			}
		});
		
		
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				AppSettings appSettings = AppSettings.get();
				appSettings.serverAddress = editTextServerAddress.getText().toString();
				appSettings.libraryAddress = editTextLibraryAddress.getText().toString();
				appSettings.playerID = editTextPlayerID.getText().toString();
				//appSettings.walkieTalkieAddress = editTextPlayerID.getText().toString();
				appSettings.save();
			}
		});
		
		
		return v;

	}

	private void loadContentInUI() { 
		AppSettings appSettings = AppSettings.get();
		editTextPlayerID.setText(appSettings.playerID);
		editTextServerAddress.setText(appSettings.serverAddress);
		editTextLibraryAddress.setText(appSettings.libraryAddress);
		//editTextWalkieTalkieAddress.setText(appSettings.walkieTalkieAddress);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}


}
