package com.example.friendfinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class DatabaseHelper {

	private static final String AppId = "U0iq171pd4a1DaYOuFjWR7oeeCl0miyVcuQcQgzA";
	private static final String ClientKey = "BRShzq5tvwIuMfwns6ib1HhxJOu0giIo7pRzVth7";
	
	public static void initializeParse(Context context)
	{
		//TO DO check return value
		Parse.initialize(context, AppId, ClientKey);
	}
	
	
	public static void GetPositions( final Context context)
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
					 ArrayList<ParseGeoPoint> listPosition = new ArrayList<ParseGeoPoint>();
					 for (ParseObject parseObject : listUser) {
						
						 listPosition.add(parseObject.getParseGeoPoint(""));
					}
					 
					 
					 ((MainActivity) context).processGetdAllPositions(listPosition);
				 }
				 else
				 {
					 ((MainActivity) context).errorFriendCircles(e.getMessage());
				 }
			}
		});

	}
	
	public static void SignInUser(String username, String password, final Context context)
	{
		 ParseUser.logInInBackground(username, password, new LogInCallback() {
			 
			 @Override
			 public void done(ParseUser user, ParseException e) {
				 if (e == null && user != null) {	
					 Log.d("signIn","ok");
					 ((LoginActivity) context).loginSuccessfull(user);
			     } else if (user == null) {
			    	 ((LoginActivity) context).loginFailedBadPassword();
			     } else {
			    	 ((LoginActivity) context).loginError();
			     }
			 }
		 });
	}
	
	public static void SignUpUser(final ParseUser user, final ParseObject metaData, final Context context)
	{
		metaData.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				if(e == null)
				{
					user.put("Metadata", metaData);
					user.signUpInBackground(new SignUpCallback() {
						
						@Override
						public void done(ParseException e) {							
							if(e == null)
							{
								Log.d("signUp","ok");
								((SignUpActivity) context).signUpSuccessfull();
							}
							else
							{
								((SignUpActivity) context).signUpFailed(e.getMessage());
							}
						}
					});
					
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
		 query.include("UserId.Metadata");
		 query.include("UserFriendId.Metadata");
		 
		 
		 
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
					 
					 ((MainActivity) context).PlaceAllFriend(listUser);
					
				 }
				 else
				 {
					 ((MainActivity) context).errorFriendCircles(e.getMessage());
				 }
			}
		});

	}
	
	public static void searchFirstLastName(final Context context, String name) {
		
		String[] nameParts = name.split(" ");		
		
		
		 List<ParseQuery<ParseObject>> listQ = new ArrayList<ParseQuery<ParseObject>>();
		 
		 
		 
		 ParseQuery<ParseObject> queryMetadata1 = ParseQuery.getQuery("Metadata");
		 queryMetadata1.whereEqualTo("FirstName", nameParts[0]);
		 queryMetadata1.whereEqualTo("LastName", nameParts[1]);
		 
		 ParseQuery<ParseObject> queryMetadata2 = ParseQuery.getQuery("Metadata");
		 queryMetadata2.whereEqualTo("FirstName", nameParts[1]);
		 queryMetadata2.whereEqualTo("LastName", nameParts[0]);		 
		 			 
		 listQ.add(queryMetadata1);
		 listQ.add(queryMetadata2);
		 
		 ParseQuery<ParseObject> queryMetadata = ParseQuery.or(listQ);
		 
		 
		 queryMetadata.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {	                    		
            		List<String> objectIdsMetadata = new ArrayList<String>(); 
            		          		
            	    for (int i = 0; i < objects.size(); i++) {
                    	
                    	boolean add = true;
                    	String currentObjectId = (String)objects.get(i).getObjectId();
                    	
						//check if id was already saved
                    	for(int j = 0; j<objectIdsMetadata.size(); j++) {
                    		if(objectIdsMetadata.get(j) == currentObjectId) {
                    			add = false;
                    			break;
                    		}
                    	}
                    	if (add == true) {
                    		/*ParseQuery<ParseUser> queryUser = ParseUser.getQuery();
                    		
                    		//ParseQuery<ParseUser> queryUser = ParseUser.
                    		//queryUser.whereEqualTo("Metadata", currentObjectId);
                    		
                    		//ParseUser usr1 = parseObject.getParseUser("UserFriendId");
                                  		
                    		
                    		queryUser.findInBackground(new FindCallback<ParseObject>() {
                                public void done(List<ParseObject> objects1, ParseException e1) {
                                    if (e1 == null) {
                                    	//Log.v("call", "null");
                                    	Log.v("call", ""+objects1.size());
                                    	
                                    	for(int k = 0; k < objects1.size(); k++) {
                                    		
                                    		String currentPosition = (String)objects1.get(k).get("username");
                                    		
                                    		Log.v("call", currentPosition);
                                    	}
                                    } else {
                                    	((MainActivity) context).errorFriendCircles(e1.getMessage());                                    	
                                    }
                                }
                    		});*/
                    		
                    		
                    		
                    		objectIdsMetadata.add(currentObjectId);
                    	} 
                    }
                    
				 	((MainActivity) context).processSearchFirstLastName(objectIdsMetadata);
   				 }
   				 else
   				 {
   					 ((MainActivity) context).errorFriendCircles(e.getMessage());
   				 }
            }
	    });
	}
}
