package com.example.friendfinder;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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
	
	public static void CheckOutAFriend(String username, final Context context)
	{
		 ParseUser current_user = ParseUser.getCurrentUser();
		 
		 List<ParseQuery<ParseObject>> listQ = new ArrayList<ParseQuery<ParseObject>>();
		 
		 ParseQuery<ParseUser> queryUser = ParseUser.getQuery();
		 queryUser.whereEqualTo("firstName", username);
		 
		 ParseQuery<ParseObject> query1=ParseQuery.getQuery("UserCircle");
		 query1.whereEqualTo("UserFriendId", current_user);
		 query1.whereMatchesQuery("UserId", queryUser);
		 
		 ParseQuery<ParseObject> query2=ParseQuery.getQuery("UserCircle");
		 query2.whereEqualTo("UserId", current_user);
		 query2.whereMatchesQuery("UserFriendId", queryUser);
		 
		 listQ.add(query1);
		 listQ.add(query2);
		 
		 ParseQuery<ParseObject> query = ParseQuery.or(listQ);
		 query.include("UserId");
		 query.include("UserFriendId");
		 
		 query.getFirstInBackground(new GetCallback<ParseObject>() {			 
			@Override
			public void done(ParseObject p, ParseException e) {
				// TODO Auto-generated method stub
				 if(e == null && p != null)
				 {
					 ParseUser usr1 = p.getParseUser("UserFriendId");
					 ParseUser usr2 = p.getParseUser("UserId");
					 if(usr1.get("username").toString().equals(ParseUser.getCurrentUser().getUsername().toString()))
						 ((MainActivity) context).processFoundFriend(usr2);
					 else
						 ((MainActivity) context).processFoundFriend(usr1);
				 }
				 else
				 {
					 ((MainActivity) context).errorFriendCircles(e.getMessage());
				 }
			}
		});
	}
}