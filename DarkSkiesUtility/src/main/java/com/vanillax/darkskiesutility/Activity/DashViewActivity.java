package com.vanillax.darkskiesutility.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.vanillax.darkskiesutility.DarkSkiesClient;
import com.vanillax.darkskiesutility.DataForDaily;
import com.vanillax.darkskiesutility.Forecast;
import com.vanillax.darkskiesutility.R;
import com.vanillax.darkskiesutility.WeatherInfo;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DashViewActivity extends ActionBarActivity implements LocationListener
{

    DarkSkiesClient client;
	TextView testText;
	ListView listView;
	ArrayList <String> cList;
	ArrayAdapter arrayAdapter;
	Button refresh , gps;

	//location
	private LocationManager locationManager;
	private String provider;
	private TextView latituteField;
	private TextView longitudeField;
	public String latLong;



    //ExpandableListViewExampleData
    //Custom List Adapter
    ExpandableListView expandableListView;
    List<String> listDataHeader;
    HashMap<String , List<String>> listDataChildMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.main);

		latituteField = (TextView) findViewById( R.id.lat );
		longitudeField = (TextView) findViewById( R.id.longg);

		refresh = (Button)findViewById( R.id.refreshButton );
		refresh.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View view )
			{
				connectForecastIO();
			}
		} );

		gps = (Button)findViewById( R.id.gpsButton );
		gps.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View view )
			{
				Intent i = new Intent( getApplicationContext(), ShowLocationTestActivity.class );
				startActivity( i );
			}
		} );





        //Get the listview
        expandableListView = (ExpandableListView) findViewById(R.id.lvExp);


		cList = new ArrayList<String>(  );

		//connectService();
        connectForecastIO();
    }

	@Override
	protected void onResume()
	{
		super.onResume();
		//setUpLocationManager();
		locationManagerSetupTwo();
		//locationManager.requestLocationUpdates(provider, 400, 1, this);

	}

	@Override
	protected void onPause()
	{
		super.onPause();
		locationManager.removeUpdates(this);

	}

	private void locationManagerSetupTwo()
	{
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location provider.

				int lat = (int) (location.getLatitude());
				int lng = (int) (location.getLongitude());
				latituteField.setText(String.valueOf(lat));
				longitudeField.setText(String.valueOf(lng));

				latLong = lat + "," + lng;
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {}

			public void onProviderEnabled(String provider) {}

			public void onProviderDisabled(String provider) {}
		};

		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

	}

	private void setUpLocationManager()
	{
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


    public void connectForecastIO()
    {
        //Create the simple Rest adapter which points to the Forecast.io endpoints
        RestAdapter restAdapter = new RestAdapter.Builder().setServer(DarkSkiesClient.Dark_URL).build();

        //Create a instance of our forecastIO api interface
        Forecast forecast = restAdapter.create(Forecast.class);



        Callback cb = new Callback<WeatherInfo> (){
            @Override
            public void success(WeatherInfo weatherInfo, Response response) {

				listDataHeader = new ArrayList<String>();
				listDataChildMap = new HashMap<String, List<String>>();

                System.out.println( weatherInfo.timezone + " break2 " + weatherInfo.currently.summary );
                System.out.println( weatherInfo.timezone + " break2 " + weatherInfo.currently.cloudCover );


                List<DataForDaily.Day> days = weatherInfo.daily.dailyDataList;
				List<String> test = new ArrayList<String>(  );

                for(DataForDaily.Day d : days)
                {

                    long dv = Long.valueOf(d.time)*1000;// its need to be in milisecond
					DateTime day = new DateTime( dv );
					String dayName = day.dayOfWeek().getName();
					test.add( "day " + dayName + " "  + d.summary);
					System.out.println( "HERE" + dayName );

                }

				listDataHeader.add( "Tester" );

				listDataChildMap.put( listDataHeader.get( 0 ) , test );


				//listAdapter = new ExpandableListAdapter(getApplicationContext(), listDataHeader , listDataChildMap);
				//Setting list adapter
				//expandableListView.setAdapter(listAdapter);

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e("error", "error message");
            }
        };
        forecast.weatherData(latLong, new forecastResponseHandler() );


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onLocationChanged( Location location )
	{
		int lat = (int) (location.getLatitude());
		int lng = (int) (location.getLongitude());
		latituteField.setText(String.valueOf(lat));
		longitudeField.setText(String.valueOf(lng));

		latLong = lat + "," + lng;


	}

	@Override
	public void onStatusChanged( String s, int i, Bundle bundle )
	{

	}

	@Override
	public void onProviderEnabled( String s )
	{

	}

	@Override
	public void onProviderDisabled( String s )
	{

	}


	public class forecastResponseHandler implements Callback<WeatherInfo>
	{

		@Override
		public void success( WeatherInfo weatherInfo, Response response )
		{
			listDataHeader = new ArrayList<String>();
			listDataChildMap = new HashMap<String, List<String>>();

			System.out.println( weatherInfo.timezone + " break2 " + weatherInfo.currently.summary );
			System.out.println( weatherInfo.timezone + " break2 " + weatherInfo.currently.cloudCover );


			List<DataForDaily.Day> hours = weatherInfo.daily.dailyDataList;
			List<String> test = new ArrayList<String>(  );

			for(DataForDaily.Day d : hours)
			{
				long dv = Long.valueOf(d.time)*1000;// its need to be in milisecond
				DateTime day = new DateTime( dv );

				String dayName = day.dayOfWeek().getAsText();
				test.add( dayName + " Summary: "  + d.summary);
				System.out.println( "HERE" + dayName );

				//System.out.println("hour " + vv + " summary " + d.summary);
				//test.add( "day" + vv + d.summary );
			}

			listDataHeader.add( "Tester" );

			listDataChildMap.put( listDataHeader.get( 0 ) , test );


			//listAdapter = new ExpandableListAdapter(getApplicationContext(), listDataHeader , listDataChildMap);
			//Setting list adapter
			//expandableListView.setAdapter(listAdapter);
		}

		@Override
		public void failure( RetrofitError retrofitError )
		{

		}
	}

}
