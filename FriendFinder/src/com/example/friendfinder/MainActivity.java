package com.example.friendfinder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
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
import com.parse.ParseObject;
import com.parse.ParseUser;

public class MainActivity extends FragmentActivity implements LocationListener {

    GoogleMap Mmap;
    private ParseUser user = null;
	private final String DebugLoginTag = "LOGIN";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getApplicationContext());
        Mmap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        Mmap.setMyLocationEnabled(true);
        Mmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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
	
	public void processFoundAllFriend(List<ParseUser> usrList)
	{
		Log.d(DebugLoginTag, "ListFriend");
		for (ParseUser parseUser : usrList) 
		{
			Log.d("Friends", parseUser.getUsername());
		}
	}
	
	
	public void errorFriendCircles(String errorMessage)
	{
		Log.d(DebugLoginTag, errorMessage);
	}

}
