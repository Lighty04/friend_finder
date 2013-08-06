package com.example.friendfinder;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class UpdateFriendsTask extends AsyncTask<Void, Void, ArrayList<ParseUser>>{

	public Context context = null;
	
	@Override
	protected ArrayList<ParseUser> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		if(isCancelled())
			return null;
		final ParseUser current_user = ParseUser.getCurrentUser();
		if(current_user == null)
			 return null;
		
		 List<ParseObject> list = null;
		 ArrayList<ParseUser> listUser = null;
		try
		{
		 List<ParseQuery<ParseObject>> listQ = new ArrayList<ParseQuery<ParseObject>>();

		 
		 ParseQuery<ParseObject> query1=ParseQuery.getQuery("UserCircle");
		 query1.whereEqualTo("UserFriendId", current_user);
		 
		 
		 ParseQuery<ParseObject> query2=ParseQuery.getQuery("UserCircle");
		 query2.whereEqualTo("UserId", current_user);
		 
		 
		 listQ.add(query1);
		 listQ.add(query2);
		 
		 ParseQuery<ParseObject> query = ParseQuery.or(listQ);
		 query.include("UserId.Metadata");
		 query.include("UserFriendId.Metadata");

		 
		 try {
			 Log.d("async task", "here1");
			list = query.find();
			listUser = new ArrayList<ParseUser>();			
			 for (ParseObject parseObject : list) {
				
				 ParseUser usr1 = parseObject.getParseUser("UserFriendId");
				 ParseUser usr2 = parseObject.getParseUser("UserId");
				 
				 Log.d("usr1", usr1.getUsername());
				 Log.d("usr2", usr2.getUsername());
				 
				 if(usr1.get("username").toString().equals(ParseUser.getCurrentUser().getUsername().toString()))
				 {
					 listUser.add(usr2);
				 }
				 else
				 {
					 listUser.add(usr1);
				 }	
			 }
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.d("cancel", e1.getMessage());
		}
		}catch (Exception e)
		{
			Log.d("cancel", e.getMessage());
		}
		 
		 return listUser;
	}
	
	@Override
	protected void onPostExecute(final ArrayList<ParseUser> listUser) {
		if(listUser != null)
		{
			 ((MainActivity) context).PlaceAllFriend(listUser);
		}
		else
		{
			((MainActivity) context).errorFriendCircles("Error when searching for members");
		}
	}

}
/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
//STUDY THIS
/*public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO: attempt authentication against a network service.

		try {
			// Simulate network access.
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			return false;
		}

		/*for (String credential : DUMMY_CREDENTIALS) {
			String[] pieces = credential.split(":");
			if (pieces[0].equals(mEmail)) {
				// Account exists, return true if the password matches.
				return pieces[1].equals(mPassword);
			}
		}*/

		// TODO: register the new account here.
	/*	return true;
	}

	@Override
	protected void onPostExecute(final Boolean success) {
		mAuthTask = null;
		showProgress(false);

		if (success) {
			finish();
		} else {
			mPasswordView
					.setError(getString(R.string.error_incorrect_password));
			mPasswordView.requestFocus();
		}
	}

	@Override
	protected void onCancelled() {
		mAuthTask = null;
		showProgress(false);
	}
}*/