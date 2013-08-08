package com.example.friendfinder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.Switch;

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

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		Boolean GPSSwitchStatus;
		Boolean autoGeolocSwitchStatus;
		Boolean dataSwitchStatus;

		try {
			GPSSwitchStatus = preferences.getBoolean("GPSSwitchStatus", true);
			autoGeolocSwitchStatus = preferences.getBoolean(
					"autoServiceStatus", true);
			dataSwitchStatus = preferences.getBoolean("dataSwitchStatus", true);
		} catch (Exception e) {
			GPSSwitchStatus = true;
			autoGeolocSwitchStatus = true;
			dataSwitchStatus = true;
		}

		GPSSwitch.setChecked(GPSSwitchStatus);
		autoGeolocSwitch.setChecked(autoGeolocSwitchStatus);
		dataSwitch.setChecked(dataSwitchStatus);

		// Set an event listener on the Switches
		autoGeolocSwitch
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							// The toggle is enabled
							// We launch the automatic geolocalization service
							Intent i = new Intent(SettingsActivity.this,
									PositionService.class);
							startService(i);

						} else {
							// The toggle is disabled
							// We stop the automatic geolocalization service
							Intent i = new Intent(SettingsActivity.this,
									PositionService.class);
							stopService(i);
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
		getMenuInflater().inflate(R.menu.options_menu, menu);
		return true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		SharedPreferences pref = getSharedPreferences("Settings", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean("autoServiceStatus", autoGeolocSwitch.isChecked());
		editor.putBoolean("GPSSwitchStatus", GPSSwitch.isChecked());
		editor.putBoolean("dataSwitchStatus", dataSwitch.isChecked());
		editor.commit();
		
	}

	private void setMobileDataEnabled(Context context, boolean enabled) {

		try {
			final ConnectivityManager conman = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final Class conmanClass = Class
					.forName(conman.getClass().getName());
			final Field iConnectivityManagerField = conmanClass
					.getDeclaredField("mService");
			iConnectivityManagerField.setAccessible(true);
			final Object iConnectivityManager = iConnectivityManagerField
					.get(conman);
			final Class iConnectivityManagerClass = Class
					.forName(iConnectivityManager.getClass().getName());
			final Method setMobileDataEnabledMethod = iConnectivityManagerClass
					.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			setMobileDataEnabledMethod.setAccessible(true);

			setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
		} catch (Exception e) {
			Log.d("SettingsActivity", e.getMessage());
		}
	}
}