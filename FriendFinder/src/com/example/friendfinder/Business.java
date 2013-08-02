package com.example.friendfinder;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class Business  {

	public static void NewUser(final ParseUser user, final ParseObject metaData, Context context) {
		// TODO Auto-generated method stub		
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
