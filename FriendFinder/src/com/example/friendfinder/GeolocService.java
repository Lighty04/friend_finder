package com.example.friendfinder;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

public class GeolocService extends Service {

	private final IBinder mBinder = new MyBinder();
	private LocationClient mLocationClient;
	private Location mCurrentLocation;

	public class GooglePlayServicesGeoloc extends FragmentActivity implements
			GooglePlayServicesClient.ConnectionCallbacks,
			GooglePlayServicesClient.OnConnectionFailedListener {


		@Override
		public void onConnectionFailed(ConnectionResult result) {
			try {

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onConnected(Bundle connectionHint) {
			Toast.makeText(getApplicationContext(), "Connected",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onDisconnected() {
			Toast.makeText(getApplicationContext(),
					"Disconnected. Please reconnect", Toast.LENGTH_SHORT)
					.show();

		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			mLocationClient = new LocationClient(this, this, this);
		}

		@Override
		protected void onStart() {
			super.onStart();
			// Connect the client.
			mLocationClient.connect();
		}

		@Override
		protected void onStop() {
			// Disconnecting the client invalidates it.
			mLocationClient.disconnect();
			super.onStop();
		}

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		// code here what's to be done when the service is started
		// launch geoloc
		mCurrentLocation = mLocationClient.getLastLocation();

		// When geoloc ok, send to the corresponding user in the database
		// his
		// position

		return Service.START_NOT_STICKY;
	}

	public class MyBinder extends Binder {
		GeolocService getService() {
			return GeolocService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
