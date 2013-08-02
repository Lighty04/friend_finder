package com.example.friendfinder;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.parse.ParseUser;

public class MainActivity extends Activity {

	private ParseUser user = null;
	private final String DebugLoginTag = "LOGIN";
	
	public Button queryButton;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		DatabaseHelper.initializeParse(MainActivity.this);
		
		queryButton = (Button) findViewById(R.id.query);
		queryButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	public void loginSuccessfull(ParseUser user)
	{
		this.user = user;
		Log.d(DebugLoginTag, "Login successfull");
		//do other UI stuff;
	}
	
	public void loginFailedBadPassword()
	{		
		Log.d(DebugLoginTag, "Login failed bad password");
		//do other UI stuff;
	}
	
	public void loginError()
	{
		Log.d(DebugLoginTag, "Login error");
		//do other UI stuff;
	}
	
	public void signUpSuccessfull(ParseUser user)
	{
		Log.d(DebugLoginTag, "Sign up successfull");
		this.user = user;
		//do other UI stuff;
	}
	
	public void signUpFailed()
	{
		Log.d(DebugLoginTag, "Sign up failed");
		//do other UI stuff;
	}

}

