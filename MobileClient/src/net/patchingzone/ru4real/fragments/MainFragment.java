package net.patchingzone.ru4real.fragments;

import net.patchingzone.ru4real.Credits;
import net.patchingzone.ru4real.Network;
import net.patchingzone.ru4real.Options;
import net.patchingzone.ru4real.R;
import net.patchingzone.ru4real.R.id;
import net.patchingzone.ru4real.R.layout;
import net.patchingzone.ru4real.base.MainApp;
import net.patchingzone.ru4real.game.Game;
import net.patchingzone.ru4real.sensors.GPSManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class MainFragment extends Fragment {

	public ImageButton BT_home;
	public RelativeLayout body;

	private View v;

	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		super.onCreateView(inflater, container, savedInstanceState);

		v = inflater.inflate(R.layout.activity_main, container, false);

		return v;
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		this.body = (RelativeLayout) v.findViewById(R.id.body);
		this.BT_home = (ImageButton) v.findViewById(R.id.button1);

		MainApp.app_preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		GPSManager gpsManager = new GPSManager(getActivity());
		gpsManager.startGPS();

		Network network = new Network();
		network.connect();

		this.BT_home.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (true) {
					// connect to server.
					final EditText input = new EditText(getActivity());
					input.setHint("Your name");
					input.setText("lallaa");
					new AlertDialog.Builder(getActivity()).setTitle("Set Name")
					// .setMessage("")
							.setView(input).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									String value = input.getText().toString();
									Log.d("hans", value);

									SharedPreferences.Editor editor = MainApp.app_preferences.edit();
									editor.putString("ID", input.getText().toString().trim());
									editor.commit();

								}
							}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									// Do nothing.
									// ma.closeConnection();
								}
							}).show();
				} else {
				}

			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.Cred:
			Intent cred = new Intent(getActivity(), Credits.class);
			startActivity(cred);
			return true;
		case R.id.Options:
			Intent Pref = new Intent(getActivity(), Options.class);
			startActivity(Pref);
			return true;
		case R.id.Disconnect:

			return true;
		case R.id.Exit:
			getActivity().finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
	public void startGame() {
		Intent intent = new Intent(getActivity(), Game.class);
		startActivity(intent);
	}
	

}
