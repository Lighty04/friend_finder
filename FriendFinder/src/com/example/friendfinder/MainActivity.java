package com.example.friendfinder;

import java.util.List;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
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
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class MainActivity extends FragmentActivity implements OnMarkerClickListener {

    GoogleMap Mmap;
    private ParseUser user = null;
	private final String DebugLoginTag = "LOGIN";
	private Button bLogOut;
	//private OrientationEventListener customOr;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = ParseUser.getCurrentUser();
        GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getApplicationContext());
        Mmap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        Mmap.setMyLocationEnabled(true);
        Mmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Business.FindAllFriend(this);
        bLogOut = (Button) findViewById(R.id.logOut);
        bLogOut.setVisibility(View.INVISIBLE);
       
        
        
        
        //Search
        handleIntent(getIntent());
        
        bLogOut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(user != null)
				{
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
	public boolean onMarkerClick(Marker arg0) {
		
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
    		Button btnBack = (Button) popupView.findViewById(R.id.btnFacebookChat);
    		btnBack.setOnClickListener(
    		new Button.OnClickListener() {
    			public void onClick(View v) {
    				popupWindow.dismiss();
    			}
    		});
    		
    		//Phone Button
    		Button btnPhone = (Button) popupView.findViewById(R.id.btnPhone);
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
	
	public void processSearchFirstLastName(List<LatLng> positionsUsers) {
		
		
		for(int i=0; i<positionsUsers.size(); i++) {			
						
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
           
            Mmap.addMarker(new MarkerOptions()
            	.position(new LatLng(longitude,latitude))
            	.title((String) name)
            	.icon(BitmapDescriptorFactory
            	.defaultMarker(BitmapDescriptorFactory.HUE_RED)));     

			
		}
		 bLogOut.setVisibility(View.VISIBLE);
		 return true;
	}
	
	public void errorFriendCircles(String errorMessage)
	{
		Log.d(DebugLoginTag, errorMessage);
		bLogOut.setVisibility(View.VISIBLE);
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

