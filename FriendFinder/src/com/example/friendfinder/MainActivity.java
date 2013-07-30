package com.example.friendfinder;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		DatabaseHelper.initializeParse(MainActivity.this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		ServiceImpl x = new ServiceImpl();
		x.Connect("Seb", "lol", this);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

	public void ok() {
		//Log.d("SI", "ca marche");
		// TODO Auto-generated method stub
		TextView t = (TextView) findViewById(R.id.tvRes);
		t.setText("ieeeeeei");
	}
	
	
	

}

