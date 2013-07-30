package com.example.friendfinder;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
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
					 Log.d("signIn","ok");
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
					Log.d("signUp","ok");
					((MainActivity) context).signUpSuccessfull(user);
				}
				else
				{
					((MainActivity) context).signUpFailed();
				}
			}
		});
	}
	
	public static void CheckOutAFriend(String username, String id, final Context context)
	{
		 ParseUser current_user = ParseUser.getCurrentUser();
		 
		 if (current_user==null)
		 {
			 Log.d("TestCurrentUsert","null");
		 }
		 else
		 {
			 Log.d("TestCurrentUsert","ok");
			 ParseQuery<ParseObject> query = ParseQuery.getQuery("UserCircle");
			 List<ParseQuery<ParseObject>> listQ = new ArrayList<ParseQuery<ParseObject>>();
			 Log.d("lol",current_user.getObjectId());
			 ParseQuery<ParseObject> query1=ParseQuery.getQuery("UserCircle");
			 query1.whereEqualTo("UserFriendId", current_user.getObjectId());
			 ParseQuery<ParseObject> query2=ParseQuery.getQuery("UserCircle");
			 query2.whereEqualTo("UserId", current_user.getObjectId());
			 
			 
			 
			 listQ.add(query1);
			 listQ.add(query2);
			 
			query.or(listQ);
			 
			 query.findInBackground(new FindCallback<ParseObject>() {
				 @Override
				public void done(List<ParseObject> object, ParseException e) {
					 if (e == null) {
						 Log.d("ListFriend","good");
						 
						 
						 
						 
					    } else {
					   	 Log.d("ListFriend","bad");
					    }
					
				}
			});
		 }
		 
	}
}
