package com.example.friendfinder;

import java.util.List;

import android.content.Context;
import android.util.Log;

public class Business  {

	
	public boolean NewUser(User user, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean Connect(String user, String password, Context context) {
		// TODO Auto-generated method stub
		DatabaseHelper.SignInUser(user, password, context);
		return false;
	}

	
	public boolean AddFriend(String userName, int id) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean DeleteFriend(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public User FindAFriend(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<User> FindAllFriend() {
		// TODO Auto-generated method stub
		return null;
	}

}
