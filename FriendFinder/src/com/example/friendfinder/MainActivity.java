package com.example.friendfinder;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.SearchView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMarkerClickListener{
	
	private Marker myMarker;
	private String name = "Juan";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		GoogleMap mMap;
		mMap = ((SupportMapFragment)getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		mMap.setTrafficEnabled(true);		
		
		mMap.setOnMarkerClickListener(this);

        myMarker = mMap.addMarker(new MarkerOptions()
			        .position(new LatLng(0, 0))
			        .title("Marker")
			        .draggable(false)
			        .snippet(this.name)
			        .icon(BitmapDescriptorFactory
			        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        
        //Search
        handleIntent(getIntent());
        
		
			
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
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
    		
    		
    		//TextView textView = (TextView) popupView.findViewById(R.id.tPersonGivenname);
    		//textView.setText(this.name);
    		
    		
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
            
            //Toast.makeText(getApplicationContext(), "handleIntent: " + query, Toast.LENGTH_LONG).show();
            
            Log.v("call", "Query: "+query);
            
            //use the query to search your data somehow
        }
    }

	


		
		
}



