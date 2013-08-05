package com.example.friendfinder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyStartServiceReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, GeolocService.class);
	    context.startService(service);		
	}

}
