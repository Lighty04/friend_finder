package com.example.friendfinder;

import java.util.List;

import android.content.Context;

public interface ServiceInterface {
	
	
	public boolean NewUser (User user,String password);
	
	public boolean Connect (String user,String password, Context context);
	
	public boolean AddFriend (User user);
	
	public boolean DeleteFriend (User user);
	
	public User FindAFriend (User user);
	
	public List<User> FindAllFriend ();


}
