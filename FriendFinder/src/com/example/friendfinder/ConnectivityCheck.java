package com.example.friendfinder;

import android.content.Context;
import android.location.LocationManager;

public class ConnectivityCheck {

	// contains the functions to check if :
	// GPS
	// Data
	public boolean checkLocalization(Context context) {
		LocationManager lm = null;
		boolean gps_enabled = false;
		if (lm == null)
			lm = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
			// Log
		}
		return gps_enabled;
	}

	public boolean checkDataConnection(Context context) {
		boolean network_enabled = false;
		LocationManager lm = null;
		if (lm == null)
			lm = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
		try {
			network_enabled = lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
			// Log
		}
		return network_enabled;
	}

}
