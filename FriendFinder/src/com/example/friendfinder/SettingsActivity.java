package com.example.friendfinder;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		populateSettingsListView();
	}

	private void populateSettingsListView()
	{
		// Create list of items
		String[] myItems = {"blue", "green", "red"};
		
		// Build adapter
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this, 
				R.layout.activity_settings, 
				myItems);
		
		// Configure the ListView
		ListView settingsListView = (ListView) findViewById(R.id.action_settings);
		settingsListView.setAdapter(adapter);
	}
	

}
