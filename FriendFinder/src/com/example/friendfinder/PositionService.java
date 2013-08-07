package com.example.friendfinder;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationListener;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class PositionService extends Service implements LocationListener,
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener {


private class MyTimeThread extends Thread {
public void run() {

	DatabaseHelper.initializeParse(getApplicationContext());
	// Getting LocationManager object from System Service
	// LOCATION_SERVICE
	final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

	// Creating a criteria object to retrieve provider
	Criteria criteria = new Criteria();

	// Getting the name of the best provider
	final String provider = locationManager.getBestProvider(criteria,
			true);

	Handler h = new Handler(PositionService.this.getMainLooper());
	while (enabled) {
		Log.d("handler", "handler1");
		h.post(new Runnable() {
			@Override
			public void run() {
				Log.d("handler", "handler2");
				// Getting Current Location From GPS
				Location location = locationManager
						.getLastKnownLocation(provider);
				
				String isLocationNull = "false";
				
				if (location == null)
				{
					isLocationNull = "true";
				}
				else {
					isLocationNull = "false";
				}
				
				Log.d("handler", isLocationNull);

				if (location != null) {
					onLocationChanged(location);
					Log.d("handler", "location not null");
				}
				
			}
			
		});

		try {
			sleep(60000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

public void onLocationChanged(final Location location) {
	SharedPreferences pref = getApplicationContext().getSharedPreferences("Settings", 0); //0 is for mode private
	String lastUserId = pref.getString("lastUserId", "");
	Log.d("positionService", lastUserId);
	try
	{
	if(!("".equals(lastUserId)))
	{
		ParseQuery<ParseUser> queryUser = ParseUser.getQuery();
			queryUser.getInBackground(lastUserId, new GetCallback<ParseUser>() {
				
				@Override
				public void done(ParseUser usr, ParseException e) {
					// TODO Auto-generated method stub
					if(e == null)
					{
						//if(ParseUser.getCurrentUser()!= null && ParseUser.getCurrentUser().getObjectId().equals(usr.getObjectId()))
						//{
							usr.put("position", new ParseGeoPoint(location.getLatitude(), location.getLongitude()));
							Log.d("positionService", "goingToSave");
							usr.saveInBackground();
							//Log.d("positionService", "saved");
						//}
					}
					else
					{
						Log.d("positionService", e.getMessage());
					}
					
				}
			});
	}
	}catch(Exception e)
	{
		Log.d("positionService", e.getMessage());
	}
	finally
	{
		Log.d("positionService", "finally");
	}
	/*Toast.makeText(PositionService.this,
			((CharSequence) (location.getLatitude() + " " + location.getLongitude())),
			Toast.LENGTH_LONG).show();*/
}
}

private boolean enabled = true;
private static final int NOTIF_ID = 101;
private static final int PENDING_INTENT_ID = 201;
private Notification notif;
private PendingIntent pi;

@Override
public IBinder onBind(Intent intent) {
	Log.d("GoogleConnection", "onBind");
return null;
}

@Override
public void onCreate() {
super.onCreate();

Intent i = new Intent(this, MainActivity.class);
Log.d("GoogleConnection", "onCreate");
pi = PendingIntent.getActivity(this, PENDING_INTENT_ID, i,
		PendingIntent.FLAG_CANCEL_CURRENT);
notif = new Notification(R.drawable.ic_launcher, "this is the ticker",
		System.currentTimeMillis());
notif.setLatestEventInfo(this, "Status", "Time service is running", pi);
}

@Override
public void onDestroy() {
	Log.d("GoogleConnection", "onDestroy");
stopForeground(true);
enabled = false;
super.onDestroy();
}

@Override
public int onStartCommand(Intent intent, int flags, int startId) {
startForeground(NOTIF_ID, notif);
enabled = true;
new MyTimeThread().start();
return super.onStartCommand(intent, flags, startId);
}

@Override
public void onConnectionFailed(ConnectionResult result) {
// TODO Auto-generated method stub
Log.d("GoogleConnection", "failed");
}

@Override
public void onConnected(Bundle connectionHint) {
// TODO Auto-generated method stub
Log.d("GoogleConnection", "connected");
}

@Override
public void onDisconnected() {
// TODO Auto-generated method stub
Log.d("GoogleConnection", "disconnected");
}

@Override
public void onLocationChanged(Location location) {
// TODO Auto-generated method stub
Log.d("GoogleConnection", "LocationChanged");
}

}
