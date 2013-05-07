package net.patchingzone.ru4real.fragments;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class TouchableWrapper extends FrameLayout {

	private GoogleMap map;
	private MapCustomFragment mapCustomFragment;

	public TouchableWrapper(MapCustomFragment mapCustomFragment, Context context) {
		super(context);
		this.map = mapCustomFragment.mapView.getMap();
		this.mapCustomFragment = mapCustomFragment;

	}


	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// MainActivity.mMapIsTouched = true;
			break;
		case MotionEvent.ACTION_UP:
			// MainActivity.mMapIsTouched = false;
			break;

		case MotionEvent.ACTION_MOVE:
			int x = (int) event.getX();
			int y = (int) event.getY();

			Point point = new Point(x, y);
			LatLng latLng = map.getProjection().fromScreenLocation(point);
			//Point pixels = map.getProjection().toScreenLocation(latLng);;
			mapCustomFragment.setTouch(latLng);
			
			Log.d("qq2", x + " " + y + " " + latLng.latitude + " " + latLng.longitude);
			break;
		}

		return super.dispatchTouchEvent(event);
	}
}