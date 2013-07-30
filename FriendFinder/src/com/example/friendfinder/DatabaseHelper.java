package com.example.friendfinder;

import android.content.Context;
import android.util.Log;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class DatabaseHelper {

	private static final String AppId = "U0iq171pd4a1DaYOuFjWR7oeeCl0miyVcuQcQgzA";
	private static final String ClientKey = "BRShzq5tvwIuMfwns6ib1HhxJOu0giIo7pRzVth7";
	
	public static void initializeParse(Context context)
	{
		//TO DO check return value
		Parse.initialize(context, AppId, ClientKey);
	}
	
	public static void SignInUser(String username, String password, final Context context)
	{
		 ParseUser.logInInBackground(username, password, new LogInCallback() {
			 
			 @Override
			 public void done(ParseUser user, ParseException e) {
				 if (e == null && user != null) {			    	 
					 ((MainActivity) context).loginSuccessfull(user);
			     } else if (user == null) {
			    	 ((MainActivity) context).loginFailedBadPassword();
			     } else {
			    	 ((MainActivity) context).loginError();
			     }
			 }
		 });
	}
	
	public static void SignUpUser(final ParseUser user, final Context context)
	{
		user.signUpInBackground(new SignUpCallback() {
			
			@Override
			public void done(ParseException e) {
				if(e == null)
				{
					((MainActivity) context).signUpSuccessfull(user);
				}
				else
				{
					((MainActivity) context).signUpFailed();
				}
			}
		});
	}
}
