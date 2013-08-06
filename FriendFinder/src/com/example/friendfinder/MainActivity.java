package com.example.friendfinder;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MainActivity extends FragmentActivity implements LocationListener {

	private final long friendsUpdateDelay = 2000;
    GoogleMap Mmap;
    private ParseUser user = null;
	private final String DebugLoginTag = "LOGIN";
	private Button bLogOut;
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

        GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getApplicationContext());
        Mmap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        Mmap.setMyLocationEnabled(true);
        Mmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
   
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
				} catch (ParseException e1) {
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
        
        bLogOut = (Button) findViewById(R.id.logOut);
        bLogOut.setVisibility(View.INVISIBLE);
        
        bLogOut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
		});
        Mmap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker")
                .draggable(true)
                .snippet("Juan")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
      
       
      

     
 

            /*          for ( int i = 0; i < myObject.size(); i++) {

                            Object commGet = myObject.get(i).getString("Comment");

                            double geo1Dub = myObject.get(i).getParseGeoPoint("location").getLatitude();
                            double geo2Dub = myObject.get(i).getParseGeoPoint("location").getLongitude();

                           Location aLocation = new Location("first");
                           aLocation.setLatitude(geo1Dub);
                           aLocation.setLongitude(geo2Dub);
                          ;
                         
                                    map.addMarker(new MarkerOptions().position(new LatLng(geo1Dub,geo2Dub)).title((String) commGet)                                   .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));     

                                                                                           

                           }

              
            }
        });      
        
        
        */
        
        
        
        
//------------------------------------------------------------------------------------------
      /*  final Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                
            	map.addMarker(new MarkerOptions()
                .position(new LatLng(10, 10))
                .title("Marker")
                .draggable(true)
                .snippet("Juan")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));	
            	
            	
            }
        });  */
        
        
   //---------------------------------------------------------------------------------------     
        
        
        
        	Mmap.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){
        		
        		@Override
        	    public void onInfoWindowClick(Marker marker) {
                // When touch InfoWindow on the market, display another screen.
        			      			
        			
        			LatLng position = marker.getPosition();
        	    	Location location1 = new Location("");
        	    	location1.setLatitude(position.latitude);
        	    	location1.setLongitude(position.longitude);
        	    	
        	    	 double lat1 = Mmap.getMyLocation().getLatitude();
        	    	 double lon1 = Mmap.getMyLocation().getLongitude();
        	    	 double lat2 = location1.getLatitude();
        	    	 double lon2 = location1.getLongitude();
        	    	 
        	    	 
        	        /*    Toast.makeText(MainActivity.this,
        	                    "Current location " + map.getMyLocation().getLatitude(),
        	                    Toast.LENGTH_SHORT).show(); */
        	             	            
        	    /*	Toast.makeText(
        	                MainActivity.this,
        	                "Lat " + location1.getLatitude() + " "
        	                        + "Long " + location1.getLongitude(),
        	                Toast.LENGTH_LONG).show();
        	    	System.out.println("hola"); */

       float[] results = new float[1]; // 1, 2 or 3 depending on what information
       Location.distanceBetween(lat1, lon1, lat2, lon2, results);
       float distance = results[0];	
       
       Toast.makeText(MainActivity.this,
               "Your friend is " +distance+" meters away from you!",
               Toast.LENGTH_SHORT).show();
        	               
        	            
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
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }   

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	
		
	

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

  /*  public boolean onMarkerClickListener(final Marker marker) {

    	
    	
 /*      if (marker.equals(map)) {
            // handle click here
        //    map.getMyLocation();
            System.out.println("Clicked"); 
            double lat = map.getMyLocation().getLatitude();
            System.out.println("Lat" + lat);
            Toast.makeText(MainActivity.this,
                    "Current location " + map.getMyLocation().getLatitude(),
                    Toast.LENGTH_SHORT).show();
        }
    	
    	LatLng position = marker.getPosition();
    	Location location1 = new Location("");
    	location1.setLatitude(position.latitude);
    	location1.setLongitude(position.longitude);
    	
    	
    	Toast.makeText(
                MainActivity.this,
                "Lat " + location1.getLatitude() + " "
                        + "Long " + location1.getLongitude(),
                Toast.LENGTH_LONG).show();
    	System.out.println("hola");
    	 //
    
  //  }
    	
        return false;
    } */

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
		Log.d(DebugLoginTag, ((ParseObject) usr.get("Metadata")).get("FirstName").toString());
		Log.d(DebugLoginTag, ((ParseObject) usr.get("Metadata")).get("LastName").toString());
	}
	
	//TO DELETE
	/*public void processFoundAllFriend(List<ParseUser> usrList)
	{
		//PlaceAllFriend(usrList);
		Log.d(DebugLoginTag, "ListFriend");
		for (ParseUser parseUser : usrList) 
		{
			Log.d("Friends", parseUser.getUsername());
		}
	}*/
	
	public void processGetdAllPositions(List<ParseGeoPoint> PositionsList)
	{
		Log.d(DebugLoginTag, "ListPosition");
		
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
             
            
            /*Location aLocation = new Location("first");
            aLocation.setLatitude(latitude);
            aLocation.setLongitude(geo2Dub);*/
           
            //MarkerOptions m = 
            //Marker m = new Marker();
            Marker m = Mmap.addMarker(new MarkerOptions().position(new LatLng(longitude,latitude)).title((String) name)
            		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            friendsMarkers.add(m);     

			
		}
		}
		 if(!cancelUpdate)
		 {
			 friendHandler.postDelayed(friendRunnable, friendsUpdateDelay);
		 }
		 bLogOut.setVisibility(View.VISIBLE);
		 return true;
	}
	
	public void errorFriendCircles(String errorMessage)
	{
		Log.d(DebugLoginTag, errorMessage);
		bLogOut.setVisibility(View.VISIBLE);
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
	
	/*public class ResponseReceiver extends BroadcastReceiver {
		   public static final String ACTION_RESP =
		      "com.mamlambo.intent.action.MESSAGE_PROCESSED";
		   @Override
		    public void onReceive(Context context, Intent intent) {
		       /*TextView result = (TextView) findViewById(R.id.txt_result);
		       String text = intent.getStringExtra(SimpleIntentService.PARAM_OUT_MSG);
		       result.setText(text);*/
		/*	   ((FriendsUpdateIntent) intent).listUser;
		    }
		}*/
	
}
