package com.example.friendfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class Business  {

	public static void NewUser(final ParseUser user, final ParseObject metaData, Context context) {
			
		DatabaseHelper.SignUpUser(user, metaData, context);
	}

	static public void Connect(String user, String password, Context context) {
		DatabaseHelper.SignInUser(user, password, context);
	}

	public static void SaveAFriend(ParseUser user , Context context)
	{
		// TODO Vrifier que UserCircle est bien actualis en mm temps
		//Fonction qui sauvgarde un new ami du current_user dans la DataBase
		DatabaseHelper.SaveFriend(user, context);
	}

	public static void GetallMarkerOfTheCurrentUser(Context context)
	{
		// pour l'instant cette fonction n'a pas de fonction de retour dans le main activity
		DatabaseHelper.GetAllMarkerCurrent(context);
	}

	public static void SaveAMarker(String info, String title, ParseGeoPoint position)
	{
		//Fonction qui sauvgarde un nouveau marker dans la dataBase
		// TODO Vrifier le marker est bien assosi a l'user qui le cre
		DatabaseHelper.SaveMarker(info, title, position);
	}

	public static void DeleteFriend(ParseUser user, Context context) {
		//Fonction qui suprime un ami
		//TODO Effacer la relation dans UserCircle
		DatabaseHelper.DeleteFriend(user, context);
		
	}

	public static void FindAllFriendToPrintMarkers(Context context) {
		
		// fonction qui renvoi a la fonction printMarkers du main activity les list de marker dees amis du current user pour etre print sur la map
		
		DatabaseHelper.CheckOutAllFriendToPrintMarker(context);
	}
	
public static void PrintAllFriend(Context context) {
		// Fonction qui grace la fonction PlaceAllFriend de main activity print tous les amis du current user sur la map
		DatabaseHelper.PrintOutAllFriend(context);
	}
	
	public static void CheckLogout(Context context)
	{
		SharedPreferences pref = context.getSharedPreferences("Settings", 0); //0 is for mode private
		boolean keep_login = pref.getBoolean("keepLogin", false);
		if(!keep_login && ParseUser.getCurrentUser() != null)
		{
			ParseUser.logOut();
		}
		
		if(keep_login)
			Log.d("keep_login is ", "true");
		else
			Log.d("keep_login is ", "false");
		
	}
	
	public static void fbLogin(Context context)
	{
		DatabaseHelper.fbLogin(context);
	}

	public static void searchFirstLastName(Context context, String name) {
		
		DatabaseHelper.searchFirstLastName(context, name);
		Log.v("call", "Business.searchFirstLastName");
	}
}
