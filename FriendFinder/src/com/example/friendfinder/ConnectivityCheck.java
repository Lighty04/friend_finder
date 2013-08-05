package com.example.friendfinder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class ConnectivityCheck extends FragmentActivity {

	public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	public static class ErrorDialogFragment extends DialogFragment {

		private Dialog mDialog;

		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_OK:
				// try the request again here
				break;
			}
			break;
		}
	}

	private boolean servicesConnected() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getApplicationContext()); // Verify
																			// that
																			// it
																			// is
																			// the
																			// good
																			// context
		if (ConnectionResult.SUCCESS == resultCode) {
			Log.d("Location updates", "Google Play sevices is available");
			return true;
		} else { // google play services not available :(
			Log.d("Location updates", "There was an error. ");
			return false;
		}

	}

	// contains the functions to check if :
	// GPS
	// Data
	// Google play services

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
