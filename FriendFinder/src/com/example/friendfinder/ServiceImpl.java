package com.example.friendfinder;

import java.util.List;

import android.content.Context;
import android.util.Log;

public class ServiceImpl implements ServiceInterface {

	@Override
	public boolean NewUser(User user, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Connect(String user, String password, Context context) {
		// TODO Auto-generated method stub
		DatabaseHelper.SignInUser(user, password, context);
		return false;
	}

	@Override
	public boolean AddFriend(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean DeleteFriend(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User FindAFriend(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> FindAllFriend() {
		// TODO Auto-generated method stub
		return null;
	}

}
