package com.example.friendfinder;

import java.util.ArrayList;
import java.util.List;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseFacebookUtils;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MainActivity extends FragmentActivity implements OnMarkerClickListener {

	private final long friendsUpdateDelay = 10000;
    GoogleMap Mmap;
    private ParseUser user = null;
	private final String DebugLoginTag = "LOGIN";
	private ArrayList<Marker> friendsMarkers = new ArrayList<Marker>();
	private Handler friendHandler = new Handler();
	private Runnable friendRunnable = null; 
	private boolean cancelUpdate = false;
	//private OrientationEventListener customOr;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cancelUpdate = false;
        user = ParseUser.getCurrentUser();

        GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        Mmap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        Mmap.setMyLocationEnabled(true);
        Mmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
       
        //Search
        handleIntent(getIntent());
        
        friendRunnable = new Runnable(){
        	private List<ParseObject> list = null;
        	private ArrayList<ParseUser> listUser = null;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.d("intent", "here1");
				// TODO Auto-generated method stub
				final ParseUser current_user = ParseUser.getCurrentUser();

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
				} catch (com.parse.ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					Log.d("cancel", e1.getMessage());
				}
				 finally
				 {
					 runOnUiThread(new Runnable() {
						public void run() {
							Log.d("intent", "here2");
							PlaceAllFriend(listUser);
						}
					});
					 
				 }
				
			}
        	
        };
        friendHandler.postDelayed(friendRunnable, friendsUpdateDelay);
        
        
        Mmap.setOnMarkerClickListener(this);        
 
        Mmap.addMarker(new MarkerOptions()
        		.position(new LatLng(0, 0))
		    	.title("First Last")
		    	.icon(BitmapDescriptorFactory
		    	.defaultMarker(BitmapDescriptorFactory.HUE_RED))); 
		       
		Mmap.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){
			
			
			
			@Override
		    public void onInfoWindowClick(Marker marker) {
		    // When touch InfoWindow on the market, display another screen.
		      			
				showDistance(marker);
					        	               
        	            
            }
			
		});  
		
		
		
        
        
        Mmap.setOnMarkerDragListener(new OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
                // TODO Auto-generated method stub
                // Here your code
                Toast.makeText(MainActivity.this, "Dragging Start",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
            /*	LatLng position = marker.getPosition(); //
            	Toast.makeText(
            	                MainActivity.this,
            	                "Lat " + position.latitude + " "
            	                        + "Long " + position.longitude,
            	                Toast.LENGTH_LONG).show(); */
            	
            /*	Location location1 = new Location("");
            	location1.setLatitude(position.latitude);
            	location1.setLongitude(position.longitude);
            	
            	
            	Toast.makeText(
    	                MainActivity.this,
    	                "Lat " + location1.getLatitude() + " "
    	                        + "Long " + location1.getLongitude(),
    	                Toast.LENGTH_LONG).show();*/
            }
           
            @Override
            public void onMarkerDrag(Marker marker) {
                // TODO Auto-generated method stub
                // Toast.makeText(MainActivity.this, "Dragging",
                // Toast.LENGTH_SHORT).show();
                System.out.println("Draagging");
            }
        });
        
        //after we created the activity, we log that the rotation is over, so if we quit right now we can logout
        //depeding on current settings;
        SharedPreferences pref = getSharedPreferences("Settings", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();			
		editor.putBoolean("goingToRotate", false);
		editor.commit();

    }
    
    
    private void showDistance(Marker marker) {
		
		LatLng position = marker.getPosition();
		Location location1 = new Location("");
		location1.setLatitude(position.latitude);
		location1.setLongitude(position.longitude);
		
		 double lat1 = Mmap.getMyLocation().getLatitude();
		 double lon1 = Mmap.getMyLocation().getLongitude();
		 double lat2 = location1.getLatitude();
		 double lon2 = location1.getLongitude();
    	    	 


       float[] results = new float[1]; // 1, 2 or 3 depending on what information
       Location.distanceBetween(lat1, lon1, lat2, lon2, results);
       float distance = results[0]/1000;	
       
       Toast.makeText(MainActivity.this,
               "Your friend is " +distance+" kilometers away from you!",
               Toast.LENGTH_SHORT).show();
		
	}
    
	@Override //Search
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options_menu, menu);

	    // Associate searchable configuration with the SearchView
	    SearchManager searchManager =
	           (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView =
	            (SearchView) menu.findItem(R.id.search).getActionView();
	    searchView.setSearchableInfo(
	            searchManager.getSearchableInfo(getComponentName()));
	    return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.logOut:
	        	logoutFunction();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	
	
	private void logoutFunction() {

			cancelUpdate = true;
				//while(!cancel);
				Log.d("cancel", "going to cancel");
				friendHandler.removeCallbacks(friendRunnable);
				//while(!finishedUpdatingFriendsMap);
				
			
			if(user != null)
			{
				if(ParseFacebookUtils.getSession() != null)
					ParseFacebookUtils.getSession().closeAndClearTokenInformation();
				ParseUser.logOut();
				Log.d("logout", "going to log out");
				finish();
			}
			else
			{
				Log.d("logout", "cant log out");
			}
		
	}
	
	
	

    @Override //Search
    protected void onNewIntent(Intent intent) {
    	
    	handleIntent(intent);
    }
    
    //Search
    private void handleIntent(Intent intent) {
    	
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
                       
            Log.v("call", "Query: "+query);
            
            //Search     
            Business.searchFirstLastName(this, query); 
        }
    }

	@Override
	public boolean onMarkerClick(final Marker arg0) {
		
			String[] nameParts = arg0.getTitle().split(" ");	
	
		
		//if (v.getId() == R.id.btnShowPopup) {
    		LayoutInflater layoutInflater = 
    				(LayoutInflater) getBaseContext()
    				.getSystemService(LAYOUT_INFLATER_SERVICE);   		
    		
    		View popupView = layoutInflater.inflate(
    				R.layout.popup_details_person, null);
    		final PopupWindow popupWindow = new PopupWindow(
    				popupView,
    				LayoutParams.WRAP_CONTENT,
    				LayoutParams.WRAP_CONTENT);    		
    		
    		
    		TextView textViewPersonGivenName = (TextView) popupView.findViewById(R.id.tPersonGivenName);
    		TextView textViewPersonFamilyName = (TextView) popupView.findViewById(R.id.tPersonFamilyName);
    		
    		textViewPersonGivenName.setText(nameParts[0]);
    		textViewPersonFamilyName.setText(nameParts[1]);
    		
    		
    		//Back Button
    		ImageButton btnBack = (ImageButton) popupView.findViewById(R.id.btnBack);
    		btnBack.setOnClickListener(
    		new Button.OnClickListener() {
    			public void onClick(View v) {
    				popupWindow.dismiss();
    			}
    		});
    		
    		//Phone Button
    		Button btnPhone = (Button) popupView.findViewById(R.id.btnPhone);
    		//btnPhone.setVisibility(View.GONE);
    		btnPhone.setOnClickListener(
    		new Button.OnClickListener() {
    			public void onClick(View v) {
    				//make phone call
    				Uri uriCall = Uri.parse("tel:12345");
    				Intent callIntent = new Intent(Intent.ACTION_DIAL, uriCall);
    			    startActivity(callIntent);
    				//popupWindow.dismiss();

    			}
    		});
    		
    		//Email Button
    		Button btnEmail = (Button) popupView.findViewById(R.id.btnEmail);
    		btnEmail.setOnClickListener(
    		new Button.OnClickListener() {
    			public void onClick(View v) {
    				//send email   				 
    				Uri uriEmail = Uri.parse("mailto:email@hotmail.com");
    				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uriEmail);
    				emailIntent.putExtra(Intent.EXTRA_TEXT, "The email body text");     
    				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "The email subject text");   
    				startActivity(emailIntent);
    			}
    		});
    		
    		//Show Distance
    		Button btnShowDistance = (Button) popupView.findViewById(R.id.btnShowDistance);
    		btnShowDistance.setOnClickListener(
    		new Button.OnClickListener() {
    			public void onClick(View v) {
    				showDistance(arg0);
    			}
    		});
    		
    		
    		
    		

    		//popupWindow.showAsDropDown(btnPop);
    		popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    		
    	
    	//}
	
    		return true;
	}
	
	/*
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
    

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
	}
	


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}*/

 
	public void processFriendCircles(List<ParseObject> objects)
	{
		StringBuilder sb = new StringBuilder();
    	for (int i=0; i<objects.size(); i++) {
    		//sb.append(objects.get(i).get("tamere"));
    		sb.append("---");
    		//sb.append(objects.size());
    		sb.append("\n");
    	}
    	sb.append(objects.size());
    	
    	Toast.makeText(MainActivity.this,
        		sb.toString(), Toast.LENGTH_LONG).show();
	}
	
	public void processFoundFriend(ParseUser usr)
	{
		//Apres l'appel de la fonction Business.FindAFriend c'est ici qu'on recoit l'ami cherche
		Log.d(DebugLoginTag, ((ParseObject) usr.get("Metadata")).get("FirstName").toString());
		Log.d(DebugLoginTag, ((ParseObject) usr.get("Metadata")).get("LastName").toString());
	}
	
	public void processSearchFirstLastName(List<LatLng> positionsUsers) {
		
		
		for(int i=0; i<positionsUsers.size(); i++) {
			
			Log.v("call", ""+positionsUsers.get(i).latitude+", "+positionsUsers.get(i).longitude);
			
			Mmap.clear();
			
			Business.PrintAllFriend(this);
			
			
			Mmap.addMarker(new MarkerOptions()
			.position(positionsUsers.get(i))
	    	.title("Kony S")
	    	.icon(BitmapDescriptorFactory
	    	.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))); 
			
			Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionsUsers.get(i), 5));
		}
		
		Log.v("call", "MainActivity.processSearchFirstLastName");
		
	}
	
	//TO DELETE
	/*public void processFoundAllFriend(List<ParseUser> usrList)
	{
		//fonction relai entre le main activity et la couche business
		Business.PrintaAllMarkerFriends(this, listUser);
	}*/
	
	public void processFoundAMarker(ParseObject marker)
	{
		// Aprs l'appel de la fonction GetaMarker on retrouve ici le marker correspondant au titre demand
	}
	public void processFoundAllMarkerCurrent(ParseObject marker)
	{
		
	}
	
	public void processFoundAllFriendToPrintMarker(ArrayList<ParseUser> listUser)
	{
		
	}
	
	public void processGetdAllPositions(List<ParseGeoPoint> PositionsList)
	{
		// Apres l'appel de la fonction Business.GetAllPosition on rcupre ici l'ensemble des positions des amis du user connected
		
		
		Log.d(DebugLoginTag, "ListPosition");
		
	}
	
	public boolean printMarkers(List<ParseObject> markerList)
	{
		// TODO Fonction qui apres l'appel de Business.FindAllFriendToPrintMarkers doit imprimer les lists de markers des amis du current user sur la map
		for (ParseObject parseObject : markerList) {
			
			Log.d("remi",parseObject.getObjectId());
		}
		return true;
	}
	
	public boolean PlaceAllFriend(List<ParseUser> friendList)
	{

		for(int i = 0; i < friendsMarkers.size(); i++)
		{
			friendsMarkers.get(i).remove();
		}
		friendsMarkers.clear();
		if(!cancelUpdate)
		{
		 for (ParseUser user : friendList) {
			 
		
			//ParseUser user = friendList.get(1);
			String name = ((ParseObject)user.get("Metadata")).get("FirstName").toString() + " " +
							((ParseObject)user.get("Metadata")).get("LastName").toString();
			Log.d("test", name);
             ParseGeoPoint geoPoint = (ParseGeoPoint) user.get("position");
             
             double longitude = geoPoint.getLongitude();
             double latitude = geoPoint.getLatitude();
             
            Marker m = Mmap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title((String) name)
            		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            friendsMarkers.add(m);

		}
		}
		 if(!cancelUpdate)
		 {
			 friendHandler.postDelayed(friendRunnable, friendsUpdateDelay);
		 }
		 return true;
	}
	
	public void errorFriendCircles(String errorMessage)
	{
		Log.d(DebugLoginTag, errorMessage);
		if(!cancelUpdate)
		{
			friendHandler.postDelayed(friendRunnable, friendsUpdateDelay);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		//we log the fact that we're going to rotate the screen so that in destroy will not log out the user
		SharedPreferences pref = getSharedPreferences("Settings", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.commit();
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("logout", "onDestroy called, lets log out");
		SharedPreferences pref = getSharedPreferences("Settings", MODE_PRIVATE); //0 is for mode private
		boolean goingToRotate = pref.getBoolean("goingToRotate", true);
		Log.d("onDestroy", "going to rotate is " + String.valueOf(goingToRotate));
		//if we are not going to rotate(for example we're going to kill the app) then we can try to logout;
		if(!goingToRotate)
			Business.CheckLogout(this);
	}
}

