package com.example.friendfinder;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class GeolocService extends Service {

	private final IBinder mBinder = new MyBinder();
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// code here what's to be done when the service is started
		// launch geoloc
		
		// When geoloc ok, send to the corresponding user in the database his position
		
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
