package com.vanillax.darkskiesutility.activity;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.vanillax.darkskiesutility.R;

/**
 * Created by mitchross on 1/7/14.
 */
public class ShowLocationTestActivity extends Activity implements LocationListener
{

	private LocationManager locationManager;
	private String provider;
	private TextView latituteField;
	private TextView longitudeField;


	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.location_test);
		latituteField = (TextView) findViewById( R.id.lat );
		longitudeField = (TextView) findViewById( R.id.longg);


		// Get the location manager
		locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE);
		// Define the criteria how to select the locatioin provider -> use
		// default
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);

		// Initialize the location fields
		if (location != null) {
			System.out.println("Provider " + provider + " has been selected.");
			onLocationChanged(location);
		} else {
			latituteField.setText("Location not available");
			longitudeField.setText("Location not available");
		}
	}



	@Override
	protected void onResume()
	{
		super.onResume();
		locationManager.requestLocationUpdates(provider, 400, 1, this);

	}

	@Override
	protected void onPause()
	{
		super.onPause();
		locationManager.removeUpdates(this);

	}

	@Override
	public void onLocationChanged( Location location )
	{
		int lat = (int) (location.getLatitude());
		int lng = (int) (location.getLongitude());
		latituteField.setText(String.valueOf(lat));
		longitudeField.setText(String.valueOf(lng));

	}

	@Override
	public void onStatusChanged( String s, int i, Bundle bundle )
	{

	}

	@Override
	public void onProviderEnabled( String s )
	{
		Toast.makeText( this, "Enabled new provider " + provider,
				Toast.LENGTH_SHORT ).show();

	}

	@Override
	public void onProviderDisabled( String s )
	{
		Toast.makeText(this, "Disabled provider " + provider,
				Toast.LENGTH_SHORT).show();

	}


}
