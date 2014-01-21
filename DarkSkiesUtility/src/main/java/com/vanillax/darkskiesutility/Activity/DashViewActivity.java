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

import com.vanillax.darkskiesutility.Contributor;
import com.vanillax.darkskiesutility.DarkSkiesClient;
import com.vanillax.darkskiesutility.DataForDaily;
import com.vanillax.darkskiesutility.ExpandableListAdapter;
import com.vanillax.darkskiesutility.Forecast;
import com.vanillax.darkskiesutility.GitHub;
import com.vanillax.darkskiesutility.GitHubClient;
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
    ExpandableListAdapter listAdapter;
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

		setUpLocationManager();



        //Get the listview
        expandableListView = (ExpandableListView) findViewById(R.id.lvExp);

        //preparing list data
       // prepareListData();



		cList = new ArrayList<String>(  );

		//connectService();
        connectForecastIO();
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

    private void prepareListData()
    {
        listDataHeader = new ArrayList<String>();
        listDataChildMap = new HashMap<String, List<String>>();

        //Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon...");

        //Adding Child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChildMap.put(listDataHeader.get(0), top250);
        listDataChildMap.put(listDataHeader.get(1), nowShowing);
        listDataChildMap.put(listDataHeader.get(2), comingSoon);

		listAdapter = new ExpandableListAdapter(this, listDataHeader , listDataChildMap);
		//Setting list adapter
		expandableListView.setAdapter(listAdapter);

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
                   // System.out.println("hour " + vv + " summary " + d.summary);
					//test.add( "day " + vv + d.summary );
                }

				listDataHeader.add( "Tester" );

				listDataChildMap.put( listDataHeader.get( 0 ) , test );


				listAdapter = new ExpandableListAdapter(getApplicationContext(), listDataHeader , listDataChildMap);
				//Setting list adapter
				expandableListView.setAdapter(listAdapter);

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e("error", "error message");
            }
        };
        forecast.weatherData("42,-85", new forecastResponseHandler() );


    }



	public  void connectService()
	{
		// Create a very simple REST adapter which points the GitHub API endpoint.
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setServer( GitHubClient.API_URL)
				.build();

		// Create an instance of our GitHub API interface.
		GitHub github = restAdapter.create(GitHub.class);



		Callback callback = new  Callback<List<Contributor>>()
		{
			@Override
			public void success( List<Contributor> contributors, Response response )
			{
				for (Contributor contributor : contributors) {
					Log.d( "mytag", contributor.myLogin + " (" + contributor.contributions + ")" );
					testText.setText( contributor.myLogin );
					cList.add( contributor.getMyLogin() );
				}

				arrayAdapter = new ArrayAdapter( getApplicationContext(), android.R.layout.simple_list_item_1 , cList );
				listView.setAdapter( arrayAdapter );
			}

			@Override
			public void failure( RetrofitError retrofitError )
			{

			}
		};

		// Fetch and print a list of the contributors to this library.
		github.contributors("square", "retrofit" , callback );

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


			listAdapter = new ExpandableListAdapter(getApplicationContext(), listDataHeader , listDataChildMap);
			//Setting list adapter
			expandableListView.setAdapter(listAdapter);
		}

		@Override
		public void failure( RetrofitError retrofitError )
		{

		}
	}

}
