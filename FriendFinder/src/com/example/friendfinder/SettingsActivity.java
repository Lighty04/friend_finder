package com.example.friendfinder;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

public class SettingsActivity extends Activity {

	Switch autoGeolocSwitch;
	Switch GPSSwitch;
	Switch dataSwitch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		// Get the elements
		GPSSwitch = (Switch) findViewById(R.id.GPSSwitch);
		autoGeolocSwitch = (Switch) findViewById(R.id.geolocSwitch);
		dataSwitch = (Switch) findViewById(R.id.dataSwitch);

		// Set an event listener on the Switches
		autoGeolocSwitch
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							// The toggle is enabled
							// We launch the automatic geolocalization service

						} else {
							// The toggle is disabled
							// We stop the automatic geolocalization service
						}
					}
				});

		GPSSwitch
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						// in fact we can't touch to the GPS settings because of
						// privacy and securities permissions in Android.
						// So we open the Location manager in the settings

						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(intent);
						// if (isChecked) {
						// // The toggle is enabled
						// // We enable the GPS
						// } else {
						// // The toggle is disabled
						// // We desable the GPS
						// }
					}
				});

		dataSwitch
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							// The toggle is enabled
							// We activate the data connection
							setMobileDataEnabled(getBaseContext(), true);

						} else {
							// The toggle is disabled
							// We stop the data connection
							setMobileDataEnabled(getBaseContext(), false);
						}
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	private void setMobileDataEnabled(Context context, boolean enabled) {
		   final ConnectivityManager conman = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
		   final Class conmanClass = Class.forName(conman.getClass().getName());
		   final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
		   iConnectivityManagerField.setAccessible(true);
		   final Object iConnectivityManager = iConnectivityManagerField.get(conman);
		   final Class iConnectivityManagerClass =  Class.forName(iConnectivityManager.getClass().getName());
		   final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
		   setMobileDataEnabledMethod.setAccessible(true);

		   setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
		}

}
