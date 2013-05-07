package net.patchingzone.ru4real.fragments;

import net.patchingzone.ru4real.R;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements InfoWindowAdapter {

	LayoutInflater inflater = null;

	CustomInfoWindowAdapter(LayoutInflater inflater) {
		this.inflater = inflater;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		return (null);
	}

	@Override
	public View getInfoContents(Marker marker) {
		View popup = inflater.inflate(R.layout.popupinfowindow, null);

		TextView tv = (TextView) popup.findViewById(R.id.title);

		tv.setText(marker.getTitle());
		tv = (TextView) popup.findViewById(R.id.snippet);
		tv.setText(marker.getSnippet());

		return (popup);
	}

	public void setName() {
		
	}
	
}
