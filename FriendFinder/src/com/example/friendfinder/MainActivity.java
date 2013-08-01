package com.example.friendfinder;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.graphics.*;

public class MainActivity extends FragmentActivity implements LocationListener {

    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getApplicationContext());
        map = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker")
                .draggable(true)
                .snippet("Juan")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
      
      /*  public void ParseQueryMap() {
            ParseQuery query = new ParseQuery("MyObject");
            query.findInBackground(new FindCallback() {
            public void done(List<ParseObject> myObject, ParseException e) {
            if (e == null) {

                      for ( int i = 0; i < myObject.size(); i++) {

                            Object commGet = myObject.get(i).getString("Comment");

                            double geo1Dub = myObject.get(i).getParseGeoPoint("location").getLatitude();
                            geo2Dub = myObject.get(i).getParseGeoPoint("location").getLongitude();

                           Location aLocation = new Location("first");
                           aLocation.setLatitude(geo1Dub);
                           aLocation.setLongitude(geo2Dub);
                           Location bLocation = new Location("second");
                           bLocation.setLatitude(location.getLatitude());
                           bLocation.setLongitude(location.getLongitude());
                           int distance = (int)aLocation.distanceTo(bLocation);
                                if (distance<rad) {  // where "rad" radius display points
                                    map.addMarker(new MarkerOptions().position(new LatLng(geo1Dub,geo2Dub)).title((String) commGet)                                   .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));     

                                 } else {
                                 }                                                               

                           }

               } else {
                      Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
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
        
        
        
        	map.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){
        		
        		@Override
        	    public void onInfoWindowClick(Marker marker) {
                // When touch InfoWindow on the market, display another screen.
        			      			
        			
        			LatLng position = marker.getPosition();
        	    	Location location1 = new Location("");
        	    	location1.setLatitude(position.latitude);
        	    	location1.setLongitude(position.longitude);
        	    	
        	    	 double lat1 = map.getMyLocation().getLatitude();
        	    	 double lon1 = map.getMyLocation().getLongitude();
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
        	
        
        
        
        
        map.setOnMarkerDragListener(new OnMarkerDragListener() {

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

   

}
