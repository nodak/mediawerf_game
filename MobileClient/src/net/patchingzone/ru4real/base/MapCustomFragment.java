package net.patchingzone.ru4real.base;

import net.patchingzone.ru4real.R;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapCustomFragment extends SupportMapFragment {

	static final LatLng ROTTERDAM_HOME = new LatLng(51.908815, 4.503193);
	static final LatLng ROTTERDAM_DAVID = new LatLng(51.934496,4.465889);
	static final LatLng ROTTERDAM_CENTRAAL = new LatLng(51.925405, 4.469494);
	static final LatLng ROTTERDAM_ZADKINE = new LatLng(51.885088,4.496005);
	static final LatLng ROTTERDAM_LIBRARY = new LatLng(51.921138, 4.488409);

	private GoogleMap map;
	public TouchableWrapper mTouchView;
	Marker touch;

	
	@Override
	public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
		View v = super.onCreateView(arg0, arg1, arg2);

		map = getMap();
		mTouchView = new TouchableWrapper(this, getActivity());
		mTouchView.addView(v);

		UiSettings settings = getMap().getUiSettings();
		settings.setAllGesturesEnabled(false);
		settings.setMyLocationButtonEnabled(false);
		settings.setZoomControlsEnabled(false);

		Marker rCentraal = map.addMarker(new MarkerOptions().position(ROTTERDAM_CENTRAAL).title("Rotterdam Centraal"));

		Marker rZadkine = map.addMarker(new MarkerOptions().position(ROTTERDAM_ZADKINE).title("Zadkine")
				.snippet("Zadkine lalala").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));

		Marker rLibrary = map.addMarker(new MarkerOptions().position(ROTTERDAM_LIBRARY).title("Library")
				.snippet("Zadkine lalala").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));

		Marker rHome = map.addMarker(new MarkerOptions().position(ROTTERDAM_HOME).title("Home")
				.snippet("Zadkine lalala").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));

		Marker rDavid = map.addMarker(new MarkerOptions().position(ROTTERDAM_DAVID).title("David")
				.snippet("Zadkine lalala").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));

		touch = map.addMarker(new MarkerOptions().position(ROTTERDAM_DAVID).title("Touch")
				.snippet("Touching").icon(BitmapDescriptorFactory.fromResource(R.drawable.logo)));
		
	

		// Move the camera instantly 
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(ROTTERDAM_CENTRAAL, 11));

		// Zoom in, animating the camera.
		map.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);

		// map.add
		return mTouchView;

	}
	
	public void setTouch(LatLng latLng) {
		touch.setPosition(latLng);
		
	}

}
