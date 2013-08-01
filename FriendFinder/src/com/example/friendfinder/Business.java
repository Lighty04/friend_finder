package com.example.friendfinder;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class Business  {

	public static boolean NewUser(final ParseUser user, final ParseObject metaData, Context context) {
		// TODO Auto-generated method stub		
		DatabaseHelper.SignUpUser(user, metaData, context);
		return false;
	}

	static public boolean Connect(String user, String password, Context context) {
		DatabaseHelper.SignInUser(user, password, context);
		return false;
	}

	static public boolean AddFriend(String userName, int id) {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean DeleteFriend(ParseUser user) {
		// TODO Auto-generated method stub
		return false;
	}
//

	public static ParseUser FindAFriend(HashMap<String, String> dictionary, Context context) {
		// TODO Auto-generated method stub
		DatabaseHelper.CheckOutAFriend(dictionary, context);

		return null;
	}

	public static List<ParseUser> FindAllFriend(Context context) {
		
		DatabaseHelper.CheckOutAllFriend(context);
		
		return null;
	}

}
