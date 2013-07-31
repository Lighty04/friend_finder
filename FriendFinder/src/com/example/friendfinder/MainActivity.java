package com.example.friendfinder;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class MainActivity extends Activity {

	private ParseUser user = null;
	private final String DebugLoginTag = "LOGIN";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		DatabaseHelper.initializeParse(MainActivity.this);
		
		
		Business.Connect("Remi", "remi", this);
		
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Business.Connect("Seb", "lol", this);
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
		Business.FindAFriend("Danny", this);
		Log.d(DebugLoginTag, "Login successfull");
		//do other UI stuff;
		Business.FindAFriend("Seb", this);
		
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
	
	public void processFriendCircles(List<ParseObject> objects)
	{
		StringBuilder sb = new StringBuilder();
    	for (int i=0; i<objects.size(); i++) {
    		sb.append(objects.get(i).get("tamere"));
    		sb.append("---");
    		//sb.append(objects.size());
    		sb.append("\n");
    	}
    	sb.append(objects.size());
    	
    	Toast.makeText(MainActivity.this,
        		sb.toString(), Toast.LENGTH_LONG).show();
	}
	
	public void processFoundFriend(ParseUser usr)
	{
		Log.d(DebugLoginTag, usr.get("username").toString());
	}
	
	public void processFoundAllFriend(List<ParseUser> usrList)
	{
		Log.d(DebugLoginTag, "ListFriend");
	}
	
	
	public void errorFriendCircles(String errorMessage)
	{
		Log.d(DebugLoginTag, errorMessage);
	}

}

