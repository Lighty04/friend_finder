package com.example.friendfinder;

import java.util.List;

public interface ServiceInterface {
	
	
	public boolean NewUser (User user,String password);
	
	public boolean Connect (User user,String password);
	
	public boolean AddFriend (User user);
	
	public boolean DeleteFriend (User user);
	
	public User FindAFriend (User user);
	
	public List<User> FindAllFriend ();


}
