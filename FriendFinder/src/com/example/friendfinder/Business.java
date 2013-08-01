package com.example.friendfinder;

import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.parse.ParseUser;

public class Business  {

	public static void NewUser(ParseUser user, String password) {
		// TODO Auto-generated method stub
		
	}

	public static void SaveAFriend(ParseUser user , Context context)
	{
		DatabaseHelper.SaveFriend(user, context);
	}
	
	static public void Connect(String user, String password, Context context) {
		DatabaseHelper.SignInUser(user, password, context);
		
	}



	public static void DeleteFriend(ParseUser user, Context context) {
		
		DatabaseHelper.DeleteFriend(user, context);
		
	}
//

	public static void FindAFriend(HashMap<String, String> dictionary, Context context) {
		// TODO Auto-generated method stub
		DatabaseHelper.CheckOutAFriend(dictionary, context);

		
	}

	public static void FindAllFriend(Context context) {
		
		DatabaseHelper.CheckOutAllFriend(context);
		
		
	}

}
