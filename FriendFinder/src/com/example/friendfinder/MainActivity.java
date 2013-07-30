package com.example.friendfinder;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		DatabaseHelper.initializeParse(MainActivity.this);
		//DatabaseHelper.SignInUser("Seb", "lol");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		DatabaseHelper.SignInUser("Seb", "lol", this);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

