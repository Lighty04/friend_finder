package com.example.friendfinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.util.Log;

import com.parse.FindCallback;
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
	
	public static void CheckOutAFriend(HashMap<String, String> dictionary, final Context context)
	{
		 ParseUser current_user = ParseUser.getCurrentUser();
		 
		 List<ParseQuery<ParseObject>> listQ = new ArrayList<ParseQuery<ParseObject>>();
		 
		 ParseQuery<ParseObject> queryMetadata = ParseQuery.getQuery("Metadata");
		 
		 for(Entry<String, String> entry : dictionary.entrySet())
		 {
			 queryMetadata.whereStartsWith((String) entry.getKey(), (String) entry.getValue());
		 }
		 
		 ParseQuery<ParseUser> queryUser = ParseUser.getQuery();
		 queryUser.whereMatchesQuery("Metadata", queryMetadata);
		 
		 ParseQuery<ParseObject> query1=ParseQuery.getQuery("UserCircle");
		 query1.whereEqualTo("UserFriendId", current_user);
		 query1.whereMatchesQuery("UserId", queryUser);
		 
		 ParseQuery<ParseObject> query2=ParseQuery.getQuery("UserCircle");
		 query2.whereEqualTo("UserId", current_user);
		 query2.whereMatchesQuery("UserFriendId", queryUser);
		 
		 listQ.add(query1);
		 listQ.add(query2);
		 
		 ParseQuery<ParseObject> query = ParseQuery.or(listQ);
		 query.include("UserId.Metadata");
		 query.include("UserFriendId.Metadata");
		 
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
	
	
	public static boolean SaveFriend( ParseUser user, final Context context)
	{
		try
		{
		user.saveInBackground();
		
		return true;
		}catch( Exception e)
		{
			return false;
		}
		
	}
	
	public static boolean DeleteFriend(ParseUser user, final Context context)
	{
		try
		{
		user.deleteInBackground();
		
		return true;
		}catch( Exception e)
		{
			return false;
		}
	}
	
	public static void CheckOutAllFriend( final Context context)
	{
		  final ParseUser current_user = ParseUser.getCurrentUser();
		 
		 List<ParseQuery<ParseObject>> listQ = new ArrayList<ParseQuery<ParseObject>>();

		 
		 ParseQuery<ParseObject> query1=ParseQuery.getQuery("UserCircle");
		 query1.whereEqualTo("UserFriendId", current_user);
		 
		 
		 ParseQuery<ParseObject> query2=ParseQuery.getQuery("UserCircle");
		 query2.whereEqualTo("UserId", current_user);
		 
		 
		 listQ.add(query1);
		 listQ.add(query2);
		 
		 ParseQuery<ParseObject> query = ParseQuery.or(listQ);
		 query.include("UserId");
		 query.include("UserFriendId");
		 
		 
		 
		 query.findInBackground(new FindCallback<ParseObject>() {
			 
			public void done(List<ParseObject> list, ParseException e) {
				// TODO Auto-generated method stub
				 if(e == null && list != null)
				 {
					 ArrayList<ParseUser> listUser = new ArrayList<ParseUser>();
					 
					 for (ParseObject parseObject : list) {
						
						 ParseUser usr1 = parseObject.getParseUser("UserFriendId");
						 ParseUser usr2 = parseObject.getParseUser("UserId");
						 
						 Log.d("usr1", usr1.getUsername());
						 Log.d("usr2", usr2.getUsername());
						 
						 if(usr1.get("username").toString().equals(ParseUser.getCurrentUser().getUsername().toString()))
						 {
							 listUser.add(usr2);
						 }
						 else
						 {
							 listUser.add(usr1);
						 }	 
					}
					 
					 ((MainActivity) context).processFoundAllFriend(listUser);
					
				 }
				 else
				 {
					 ((MainActivity) context).errorFriendCircles(e.getMessage());
				 }
			}
		});

	}
	
	
	
	
	
	
	
}
