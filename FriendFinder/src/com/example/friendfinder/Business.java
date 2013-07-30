package com.example.friendfinder;

import java.util.List;

import android.content.Context;
import android.util.Log;

public class Business  {

	
	static public boolean NewUser(User user, String password) {
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


	static public boolean DeleteFriend(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	
	static public User FindAFriend(String username, String id, Context context) {
		
		DatabaseHelper.CheckOutAFriend(username, id, context);
		return null;
	}

	
	static public List<User> FindAllFriend() {
		// TODO Auto-generated method stub
		return null;
	}

}
