package com.example.friendfinder;

import java.util.List;

import android.content.Context;
import android.util.Log;

public class Business  {

	public static boolean NewUser(User user, String password) {
		// TODO Auto-generated method stub
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

	public static boolean DeleteFriend(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	public static User FindAFriend(String username, Context context) {
		// TODO Auto-generated method stub
		DatabaseHelper.CheckOutAFriend(username, context);
		return null;
	}

	public static List<User> FindAllFriend(Context context) {
		// TODO Auto-generated method stub
		
		return null;
	}

}
