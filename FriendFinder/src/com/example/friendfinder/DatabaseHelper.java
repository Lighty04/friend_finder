package com.example.friendfinder;

import android.content.Context;
import android.util.Log;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class DatabaseHelper {

	private static final String AppId = "U0iq171pd4a1DaYOuFjWR7oeeCl0miyVcuQcQgzA";
	private static final String ClientKey = "BRShzq5tvwIuMfwns6ib1HhxJOu0giIo7pRzVth7";
	
	public static void initializeParse(Context context)
	{
		//TO DO check return value
		Parse.initialize(context, AppId, ClientKey);
	}
	
	public static void SignInUser(String username, String password, final MainActivity mainActivity)
	{
		//InputStream res = null;
		//HttpClient client =  new DefaultHttpClient();
		 ParseUser.logInInBackground(username, password, new LogInCallback() {
			   public void done(ParseUser user, ParseException e) {
			     if (e == null && user != null) {			    	 
			    	 //mainActivity.ok();
			     } else if (user == null) {
			    	 Log.d("SI", "nok");
			     } else {
			    	 Log.d("SI", "ko");
			     }
			   }
			 });
	}
}
