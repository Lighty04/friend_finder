package com.example.friendfinder;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		GoogleMap mMap;
		mMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		mMap.setTrafficEnabled(true);
		
		LatLng SYDNEY = new LatLng(-33.88,151.21);
		LatLng MOUNTAIN_VIEW = new LatLng(37.4, -122.1);
		//workssdafdas
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY, 15));
		mMap.animateCamera(CameraUpdateFactory.zoomIn());
		mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 20000, null);
		CameraPosition cameraPosition = new CameraPosition.Builder()
		    .target(MOUNTAIN_VIEW)      // Sets the center of the map to Mountain View
		    .zoom(17)                   // Sets the zoom
		    .bearing(90)                // Sets the orientation of the camera to east
		    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
		    .build();                   // Creates a CameraPosition from the builder
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

}


