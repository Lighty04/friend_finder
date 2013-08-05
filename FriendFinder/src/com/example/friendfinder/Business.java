package com.example.friendfinder;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class Business  {

	public static void NewUser(final ParseUser user, final ParseObject metaData, Context context) {
			
		DatabaseHelper.SignUpUser(user, metaData, context);
	}

	static public void Connect(String user, String password, Context context) {
		DatabaseHelper.SignInUser(user, password, context);
	}
	
	static public void GetAllPosition( Context context) {
		DatabaseHelper.GetPositions(context);
	}

	public static void SaveAFriend(ParseUser user , Context context)
	{
		DatabaseHelper.SaveFriend(user, context);
	}
	
	public static void GetaMarker(String title, Context context)
	{
		DatabaseHelper.GetAMarker(context, title);
	}
	
	public static void GetallMarker(Context context)
	{
		DatabaseHelper.GetAllMarker(context);
	}
	
	public static void SaveAMarker(String info, String title)
	{
		DatabaseHelper.SaveMarker(info, title);
	}

	public static void DeleteFriend(ParseUser user, Context context) {
		
		DatabaseHelper.DeleteFriend(user, context);
		
	}

	public static void FindAFriend(HashMap<String, String> dictionary, Context context) {
		
		DatabaseHelper.CheckOutAFriend(dictionary, context);

		
	}

	public static void FindAllFriend(Context context) {
		
		DatabaseHelper.CheckOutAllFriend(context);
		
		
	}
	
	public static void CheckLogout(Context context)
	{
		SharedPreferences pref = context.getSharedPreferences("Settings", 0); //0 is for mode private
		boolean keep_login = pref.getBoolean("keepLogin", false);
		if(!keep_login && ParseUser.getCurrentUser() != null)
		{
			ParseUser.logOut();
		}
		
		if(keep_login)
			Log.d("keep_login is ", "true");
		else
			Log.d("keep_login is ", "false");
		
	}

}
