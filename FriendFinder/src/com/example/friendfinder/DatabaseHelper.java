package com.example.friendfinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.maps.GeoPoint;
import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
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
		ParseFacebookUtils.initialize(context.getString(R.string.app_id));
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
	
	public static void GetAllMarkerCurrent (final Context context)
	{
		final ParseUser current_user = ParseUser.getCurrentUser();
		   Log.d("remi", "data");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Marker");
		query.whereEqualTo("UserId", current_user);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null) {
		            Log.d("score", "Retrieved " + scoreList.size() + " scores");
		        } else {
		            Log.d("score", "Error: " + e.getMessage());
		        }
		    }
		});
		
		
	}
	
	public static void GetAllMarkerFromAnUser (final Context context, ParseUser user)
	{
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Marker");
		query.whereEqualTo("UserId", user);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> markerList, ParseException e) {
		        if (e == null) {
		        	
		        	((MainActivity) context).printMarkers(markerList);
		            
		        } else {
		            Log.d("score", "Error: " + e.getMessage());
		        }
		    }
		});
		
	}
	
	
	public static void GetAMarker (final Context context, String title)
	{
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Marker");
		query.whereEqualTo("title", title);
		query.getFirstInBackground(new GetCallback<ParseObject>() {
		  public void done(ParseObject object, ParseException e) {
		    if (object == null) {
		      Log.d("remi", "The getFirst request failed.");
		    } else {
		      Log.d("remi", "Retrieved the object.");
		      ((MainActivity) context).processFoundAMarker(object);
		    }
		  }
		});
		
	}
	
	public static boolean SaveMarker (String info, String title)
	{
		try
		{
			final ParseUser current_user = ParseUser.getCurrentUser();
			
			ParseObject marker = new ParseObject("Marker");
			
			marker.put("title", title);
			marker.put("description", info);
			marker.put("UserId",current_user);
			
			
			ParseGeoPoint point = (ParseGeoPoint) current_user.get("position");
			marker.put("position", point);
			Log.d("remi", "point");
			//marker.put("position", value)
			
			
			marker.saveInBackground();
		
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
	
	public static void PrintOutAllFriend( final Context context)
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
		 
		 
		 ParseQuery<ParseUser> queryUser = ParseUser.getQuery(); 	
 		queryUser.whereMatchesQuery("Metadata", queryMetadata);
 		queryUser.include("Metadata");
		 
 		queryUser.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> listUser, ParseException e) {
                if (e == null) {
                	ArrayList<LatLng> listPosition = new ArrayList<LatLng>();
					 for (ParseObject parseObject : listUser) {
						 Log.v("call", String.valueOf(parseObject.getParseGeoPoint("position").getLongitude()));
						 
						 listPosition.add(new LatLng(parseObject.getParseGeoPoint("position").getLatitude(), parseObject.getParseGeoPoint("position").getLongitude()));
					}
					 ((MainActivity) context).processSearchFirstLastName(listPosition);
   				 }
   				 else
   				 {
   					 ((MainActivity) context).errorFriendCircles(e.getMessage());
   				 }
            }
	    });
	}

	public static void fbLogin(final Context context)
	{
		ParseFacebookUtils.logIn(((LoginActivity) context), new LogInCallback() {
			
			@Override
			public void done(final ParseUser user, ParseException err) {
			    if (user == null) {
			      Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
			      ((LoginActivity) context).fbLoginCancelled();
			    } else if (user.isNew()) {
			      Log.d("MyApp", "User signed up and logged in through Facebook!");
			      Request.executeMeRequestAsync(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {

			            // callback after Graph API response with user object
			            @Override
			            public void onCompleted(final GraphUser userFb, Response response) {
			              if (userFb != null) {
			            	  final ParseObject metadata = new ParseObject("Metadata");
			            	  metadata.put("FirstName", userFb.getFirstName());
			            	  metadata.put("LastName", userFb.getLastName());
			            	  metadata.saveInBackground(new SaveCallback() {
								
								@Override
								public void done(ParseException arg0) {
											
			            	  ParseUser.getCurrentUser().put("Metadata", metadata);
			            	  ParseUser.getCurrentUser().put("fbId", userFb.getId());
			            	  ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
								
								@Override
								public void done(ParseException arg0) {
									if(arg0 != null)
										Log.d("FromParse", arg0.getMessage());
									else
									{
										Log.d("FromParse", "done");
										Request.executeMyFriendsRequestAsync(ParseFacebookUtils.getSession(), new GraphUserListCallback() {
											
											@Override
											public void onCompleted(List<GraphUser> users, Response response) {
												//CHECK RESPONSE
												if(users != null)
												{
													//search for users logged with fb
													List<String> friendsIds = new ArrayList<String>();
													for(GraphUser user : users)
													{
														friendsIds.add((String) user.getId());
													}
													ParseQuery<ParseUser> queryUser = ParseUser.getQuery();
													queryUser.whereContainedIn("fbId", friendsIds);
													queryUser.findInBackground(new FindCallback<ParseUser>() {
														public void done(java.util.List<ParseUser> parseUsers, ParseException e) {
															if(e == null)
															{
																if(parseUsers.size() > 0)
																{
																	//create UserCircle
																	List<ParseObject> objects = new ArrayList<ParseObject>();
																	for(ParseUser usr : parseUsers)
																	{
																		ParseObject obj = new ParseObject("UserCircle");
																		obj.put("UserFriendId", ParseUser.getCurrentUser());
																		obj.put("UserId", usr);
																		objects.add(obj);
																	}
																	
																	ParseObject.saveAllInBackground(objects, new SaveCallback() {
																		
																		@Override
																		public void done(ParseException e) {
																			if(e == null)
																			{
																				Log.d("FromParse", "done");
																				((LoginActivity) context).loginSuccessfull(user);
																			}
																			else
																			{
																				Log.d("FromParse", e.getMessage());
																			}
																		}
																	});
																}
															}
															else
															{
																Log.d("AddFriends", e.getMessage());
															}
														};
													});
												}
												
											}
										});
									}
									
								}
							});
								}
								});
			              }
			            }
			        });
			     
			    } else {
			      Log.d("MyApp", "User logged in through Facebook!");
			      ((LoginActivity) context).loginSuccessfull(user);
			    }
			  }
		});
	}

	public static void CheckOutAllFriendToPrintMarker( final Context context)
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
					 
					 ((MainActivity) context).processFoundAllFriendToPrintMarker(listUser);
					
				 }
				 else
				 {
					 ((MainActivity) context).errorFriendCircles(e.getMessage());
				 }
			}
		});

	}
}
