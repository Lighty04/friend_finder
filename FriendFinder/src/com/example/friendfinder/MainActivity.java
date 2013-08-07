package com.example.friendfinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
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

public class MainActivity extends FragmentActivity implements
		OnMarkerClickListener, LocationListener {

	private final long friendsUpdateDelay = 60 * 1000;
	private final long friendsMarkersUpdateDelay = 120 * 1000;
	private final long myMarkersUpdateDelay = 120 * 1000;
	GoogleMap Mmap;
	private ParseUser user = null;
	private final String DebugLoginTag = "LOGIN";
	private Button bLogOut;
	private HashMap<String, Marker> friendMarkersHashmap = new HashMap<String, Marker>();
	private HashMap<String, Marker> friendsMarkersPOI = new HashMap<String, Marker>();
	private HashMap<String, Marker> MyMarkersPOI = new HashMap<String, Marker>();
	private ArrayList<Marker> tempMarkers = new ArrayList<Marker>();
	private Handler friendHandler = new Handler();
	private Runnable friendRunnable = null;
	private Handler friendsPOIHandler = new Handler();
	private Runnable friendsPOIRunnable = null;
	private boolean cancelUpdate = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Added by Dany to configure the Action Bar and the search
		// functionnality in it
		configureActionBar();

		cancelUpdate = false;
		user = ParseUser.getCurrentUser();

		Log.d("positionService", "here4");
		Intent intentLaunchService = new Intent(getApplicationContext(),
				PositionService.class);
		Log.d("positionService", "here5");
		startService(intentLaunchService);

		GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getApplicationContext());
		Mmap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		Mmap.setMyLocationEnabled(true);
		Mmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		bLogOut = (Button) findViewById(R.id.logOut);
		// bLogOut.setVisibility(View.INVISIBLE);

		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		// Creating a criteria object to retrieve provider??
		Criteria criteria = new Criteria();

		// Getting the name of the best provider???
		String provider = locationManager.getBestProvider(criteria, true);

		// Getting Current Location From GPS
		Location location = locationManager.getLastKnownLocation(provider);

		if (location != null) {
			onLocationChanged(location);
		}

		//locationManager.requestLocationUpdates(provider, 20000, 0, this);

		// Search
		handleIntent(getIntent());

		friendRunnable = new Runnable() {
			private List<ParseObject> list = null;
			private ArrayList<ParseUser> listUser = null;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.d("intent", "here1");
				// TODO Auto-generated method stub
				final ParseUser current_user = ParseUser.getCurrentUser();

				List<ParseQuery<ParseObject>> listQ = new ArrayList<ParseQuery<ParseObject>>();

				ParseQuery<ParseObject> query1 = ParseQuery
						.getQuery("UserCircle");
				query1.whereEqualTo("UserFriendId", current_user);

				ParseQuery<ParseObject> query2 = ParseQuery
						.getQuery("UserCircle");
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
						 
						 if(usr1.get("username").toString().equals(user.getUsername().toString()))
						 {
							 listUser.add(usr2);
						 }
						 else
						 {
							 listUser.add(usr1);
						 }	
					 }
				} catch (com.parse.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("cancel", e.getMessage());
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
        
        friendsPOIRunnable = new Runnable() {
        	private List<ParseObject> list = null;
        	private ArrayList<ParseUser> listUser = null;
        	private List<ParseObject> listMarkers = null;
        	private HashMap<String, ParseObject> listMarkersPOI = null;
			@Override
			public void run() {
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
					 
					 try
					 {
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
						 if(list.size() > 0)
						 {
							 ParseQuery<ParseObject> queryMarker = ParseQuery.getQuery("Marker");
							 queryMarker.include("UserId.Metadata");
							 queryMarker.whereContainedIn("UserId", listUser);
							 try
							 {
								 listMarkers = queryMarker.find();
								 listMarkersPOI = new HashMap<String, ParseObject>();
								 for(ParseObject marker: listMarkers)
								 {
									 listMarkersPOI.put(marker.getObjectId(), marker);
								 }
								 
							 }
							 catch(com.parse.ParseException e)
							 {
								 Log.d("cancel", e.getMessage()); 
							 }
							 finally
							 {
								 runOnUiThread(new Runnable() {
										public void run() {
											Log.d("intent", "here4");
											processFoundAllFriendToPrintMarker(listMarkersPOI);
										}
									});
							 }
						 }
					 }
					 catch(com.parse.ParseException e)
					{
						 Log.d("cancel", e.getMessage());	 
					}
					 finally
					 {
						 
					 }
				
			}
		};

		Mmap.setOnMarkerClickListener(this);

		Mmap.addMarker(new MarkerOptions()
				.snippet("pos")
				.position(new LatLng(0, 0))
				.title("First Last")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
		
		Mmap.setOnMapLongClickListener(new OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng point) {
				// TODO Auto-generated method stub
				Mmap.addMarker(new MarkerOptions().position(point).snippet("newPoi").draggable(true));
			}
		});
		
		Mmap.setOnMarkerDragListener(new OnMarkerDragListener() {

			@Override
			public void onMarkerDragStart(Marker marker) {
					Log.d("poi", "dragStart");
			
			}

			@Override
			public void onMarkerDragEnd(Marker marker) {

			}

			@Override
			public void onMarkerDrag(Marker arg0) {
				// TODO Auto-generated method stub
			}
		});

		// after we created the activity, we log that the rotation is over, so
		// if we quit right now we can logout
		// depeding on current settings;
		SharedPreferences pref = getSharedPreferences("Settings", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		
		Mmap.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){
			
			
			
			@Override
		    public void onInfoWindowClick(Marker marker) {
		    // When touch InfoWindow on the market, display another screen.
		      			
				showDistance(marker);
					        	               
        	            
            }
			
		});		
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
    


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		friendHandler.post(friendRunnable);
		friendsPOIHandler.post(friendsPOIRunnable);
	}

	public boolean onMarkerClick(final Marker arg0) {
		if("pos".equals(arg0.getSnippet()))
		{
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
    		popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
			}
    		else if("poi".equals(arg0.getSnippet()))
    		{
    			LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
    					.getSystemService(LAYOUT_INFLATER_SERVICE);

    			View popupView = layoutInflater.inflate(R.layout.popup_details_poi, null);
    			final PopupWindow popupWindow = new PopupWindow(popupView,
    					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

    			TextView poiTitle = (TextView) popupView
    					.findViewById(R.id.tPOITitle);
    			
    			poiTitle.setText(arg0.getTitle());
    			ImageButton btnBack = (ImageButton) popupView.findViewById(R.id.btnBack);
    			btnBack.setOnClickListener(new Button.OnClickListener() {
    				public void onClick(View v) {
    					popupWindow.dismiss();
    				}
    			});
    			popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    		}
    		else if("newPoi".equals(arg0.getSnippet()))
    		{
    			LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
    					.getSystemService(LAYOUT_INFLATER_SERVICE);

    			View popupView = layoutInflater.inflate(R.layout.popup_new_poi, null);
    			final PopupWindow popupWindow = new PopupWindow(popupView,
    					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

    			final EditText poiTitle = (EditText) popupView
    					.findViewById(R.id.tPOISetTitle);
    			poiTitle.setText("ok");
    			Button btnBack = (Button) popupView.findViewById(R.id.btnBack);
    			btnBack.setOnClickListener(new Button.OnClickListener() {
    				public void onClick(View v) {
    					popupWindow.dismiss();
    				}
    			});
    			
    			Button btnSave = (Button) popupView.findViewById(R.id.btnSave);
    			btnSave.setOnClickListener(new Button.OnClickListener() {
    				public void onClick(View v) {
    					String newTitle = poiTitle.getText().toString().trim();
    					if(newTitle.length() > 0)
    					{
    						Business.SaveAMarker("NA", newTitle, new ParseGeoPoint(arg0.getPosition().latitude, arg0.getPosition().longitude));
    						tempMarkers.add(arg0);
    						arg0.setDraggable(false);
    						popupWindow.dismiss();
    					}
    					else
    					{
    						poiTitle.setError("Fill in the title");
    					}
    					
    				}
    			});
    			
    			popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    		}
    		
    		return true;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		friendHandler.removeCallbacks(friendRunnable);
		friendsPOIHandler.removeCallbacks(friendsPOIRunnable);
	}

	@Override
	// Search
	protected void onNewIntent(Intent intent) {
		setIntent(intent); //used to be on the same activity
		handleIntent(intent);
	}

	// Search
	private void handleIntent(Intent intent) {

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);

			Log.v("call", "Query: " + query);

			// Search
			Business.searchFirstLastName(this, query);
		}
	}

	public void processFriendCircles(List<ParseObject> objects) {
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

	public void processFoundFriend(ParseUser usr) {
		// Apres l'appel de la fonction Business.FindAFriend c'est ici qu'on
		// recoit l'ami cherche
		Log.d(DebugLoginTag,
				((ParseObject) usr.get("Metadata")).get("FirstName").toString());
		Log.d(DebugLoginTag, ((ParseObject) usr.get("Metadata"))
				.get("LastName").toString());
	}

	public void processSearchFirstLastName(List<ParseUser> usrList) {

		for (ParseUser user : usrList) {
			ParseGeoPoint geoPoint = (ParseGeoPoint) user.get("position");

			double longitude = geoPoint.getLongitude();
			double latitude = geoPoint.getLatitude();
			if (!friendMarkersHashmap.containsKey(user.getObjectId())) {
				String name = ((ParseObject) user.get("Metadata")).get(
						"FirstName").toString()
						+ " "
						+ ((ParseObject) user.get("Metadata")).get("LastName")
								.toString();

				Marker m = Mmap
						.addMarker(new MarkerOptions()
								.snippet("pos")
								.position(new LatLng(latitude, longitude))
								.title((String) name)
								.icon(BitmapDescriptorFactory
										.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
				friendMarkersHashmap.put(user.getObjectId(), m);
			} else {
				friendMarkersHashmap
						.get(user.getObjectId())
						.setPosition(new LatLng(latitude, longitude));
			}
		}

		Log.v("call", "MainActivity.processSearchFirstLastName");

	}

	// TO DELETE
	/*
	 * public void processFoundAllFriend(List<ParseUser> usrList) { //fonction
	 * relai entre le main activity et la couche business
	 * Business.PrintaAllMarkerFriends(this, listUser); }
	 */

	public void processFoundAMarker(ParseObject marker) {
		// Aprs l'appel de la fonction GetaMarker on retrouve ici le marker
		// correspondant au titre demand
	}

	public void processFoundAllMarkerCurrent(ParseObject marker) {

	}

	public void processFoundAllFriendToPrintMarker(HashMap<String, ParseObject> markers)
	{
		if(!cancelUpdate)
		{
		for(Map.Entry<String, ParseObject> entry : markers.entrySet())
		{
			ParseGeoPoint geoPoint = (ParseGeoPoint) entry.getValue().get("position");
            
            double longitude = geoPoint.getLongitude();
            double latitude = geoPoint.getLatitude();
			if(friendsMarkersPOI.containsKey(entry.getKey()))
			{
				friendsMarkersPOI.get(entry.getKey()).setPosition(new LatLng(latitude, longitude));
			}
			else
			{
				ParseUser usr = ((ParseUser)entry.getValue().get("UserId"));
				ParseObject meta = ((ParseObject)usr.get("Metadata"));
				
				String userName = meta.get("FirstName") + " " + meta.get("LastName");
				String name = userName + " - " + entry.getValue().get("title").toString();
		
				Marker m = Mmap.addMarker(new MarkerOptions().snippet("poi").position(new LatLng(latitude, longitude)).title((String) name)
        		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        
				friendsMarkersPOI.put(entry.getKey(), m);
			}
		}
		
		Iterator<Entry<String, Marker>> it = friendsMarkersPOI.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<String, Marker> pairs = (Map.Entry<String, Marker>)it.next();
			if(!markers.containsKey(pairs.getKey()))
			{
				pairs.getValue().remove();
				it.remove();
			}
		}
		friendsPOIHandler.postDelayed(friendsPOIRunnable, friendsMarkersUpdateDelay);
		}
	}

	public void processGetdAllPositions(List<ParseGeoPoint> PositionsList) {
		// Apres l'appel de la fonction Business.GetAllPosition on rcupre ici
		// l'ensemble des positions des amis du user connected

		Log.d(DebugLoginTag, "ListPosition");

	}

	public boolean printMarkers(List<ParseObject> markerList) {
		// TODO Fonction qui apres l'appel de
		// Business.FindAllFriendToPrintMarkers doit imprimer les lists de
		// markers des amis du current user sur la map
		for (ParseObject parseObject : markerList) {

			Log.d("remi", parseObject.getObjectId());
		}
		return true;
	}

	public boolean PlaceAllFriend(List<ParseUser> friendList) {

		if (!cancelUpdate) {
			for (ParseUser user : friendList) {
				ParseGeoPoint geoPoint = (ParseGeoPoint) user.get("position");

				double longitude = geoPoint.getLongitude();
				double latitude = geoPoint.getLatitude();
				if (!friendMarkersHashmap.containsKey(user.getObjectId())) {
					String name = ((ParseObject) user.get("Metadata")).get(
							"FirstName").toString()
							+ " "
							+ ((ParseObject) user.get("Metadata")).get(
									"LastName").toString();
					Log.d("test", name);

					Marker m = Mmap
							.addMarker(new MarkerOptions()
									.snippet("pos")
									.position(new LatLng(latitude, longitude))
									.title((String) name)
									.icon(BitmapDescriptorFactory
											.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
					// friendsMarkers.add(m);
					friendMarkersHashmap.put(user.getObjectId(), m);
				} else {
					friendMarkersHashmap.get(user.getObjectId()).setPosition(
							new LatLng(latitude, longitude));
					Log.d("test", "nada");
				}

			}
			friendHandler.postDelayed(friendRunnable, friendsUpdateDelay);
		}
		return true;
	}

	public void errorFriendCircles(String errorMessage) {
		Log.d(DebugLoginTag, errorMessage);
		if(!cancelUpdate)
		{
			friendHandler.postDelayed(friendRunnable, friendsUpdateDelay);
			friendsPOIHandler.postDelayed(friendsPOIRunnable, friendsMarkersUpdateDelay);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		// we log the fact that we're going to rotate the screen so that in
		// destroy will not log out the user
		SharedPreferences pref = getSharedPreferences("Settings", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.commit();

	}

	@Override
	protected void onDestroy() {
		/*friendHandler.removeCallbacks(friendRunnable);
		friendsPOIHandler.removeCallbacks(friendsPOIRunnable);*/
		// TODO Auto-generated method stub
		super.onDestroy();

		Log.d("logout", "onDestroy called, lets log out");
		SharedPreferences pref = getSharedPreferences("Settings", MODE_PRIVATE); // 0
																					// is
																					// for
																					// mode
																					// private
		boolean goingToRotate = pref.getBoolean("goingToRotate", true);
		Log.d("onDestroy",
				"going to rotate is " + String.valueOf(goingToRotate));
		// if we are not going to rotate(for example we're going to kill the
		// app) then we can try to logout;
		if (!goingToRotate)
			Business.CheckLogout(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		double mLatitude = location.getLatitude();
		double mLongitude = location.getLongitude();
		LatLng pont = new LatLng(mLatitude, mLongitude);

		Mmap.moveCamera(CameraUpdateFactory.newLatLng(pont));
		Mmap.animateCamera(CameraUpdateFactory.zoomTo(10));

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	// Dany for the search
	ActionBar actionBar;
	SearchView searchView;

	public void configureActionBar() {
		actionBar = getActionBar();
	}
}
