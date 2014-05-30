package com.vanillax.darkskiesutility.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.HttpResponseCache;
import com.squareup.okhttp.OkHttpClient;
import com.vanillax.darkskiesutility.DarkSkiesClient;
import com.vanillax.darkskiesutility.DataForDaily;
import com.vanillax.darkskiesutility.Forecast;
import com.vanillax.darkskiesutility.R;
import com.vanillax.darkskiesutility.WeatherInfo;
import com.vanillax.darkskiesutility.activity.ShowLocationTestActivity;
import com.vanillax.darkskiesutility.listviews.ExpandableListAdapterDaily;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by mitch on 2/3/14.
 */
public class DailyFragment extends Fragment  implements LocationListener{

    DarkSkiesClient client;
    TextView testText;
    ListView listView;
    ArrayList<String> cList;
    ArrayAdapter arrayAdapter;
    Button refresh , gps;

    //location
    private LocationManager locationManager;
    private String provider;
    private TextView latituteField;
    private TextView longitudeField;
    public String latLong;

    protected ExpandableListAdapterDaily myListAdapter;

    //ExpandableListViewExampleData
    //Custom List Adapter
    ExpandableListView expandableListView;
    List<String> listDataHeader;
    HashMap<String , List<String>> listDataChildMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_temp_tab, container , false);

        latituteField = (TextView) rootView.findViewById(R.id.lat);
        longitudeField = (TextView) rootView.findViewById(R.id.longg);

        refresh = (Button) rootView.findViewById(R.id.refreshButton);
        refresh.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View view )
            {
                connectForecastIO();
            }
        } );

        gps = (Button)rootView.findViewById(R.id.gpsButton);
        gps.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View view )
            {
                Intent i = new Intent( getActivity().getApplicationContext(), ShowLocationTestActivity.class );
                startActivity( i );
            }
        } );

        //setUpLocationManager();



        //Get the listview
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);


        cList = new ArrayList<String>(  );

        //connectService();
		connectForecastIO();


        return rootView;
    }

	@Override
	public void onResume()
	{
		super.onResume();
		locationManagerSetupTwo();


	}

	private void locationManagerSetupTwo()
	{

		// Get the location manager
		locationManager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE);
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


    public void connectForecastIO()
    {
		OkHttpClient okHttpClient = new OkHttpClient();

		File cacheDir = new File( getActivity().getApplicationContext().getCacheDir(), UUID.randomUUID().toString() );

				HttpResponseCache cache = null;
				try {
					cache = new HttpResponseCache(cacheDir, 8L * 1024 * 1024);
				} catch (IOException e) {
					Log.d( "mitch", "test fail" );
				}

		okHttpClient.setResponseCache( cache );


        //Create the simple Rest adapter which points to the Forecast.io endpoints
        RestAdapter restAdapterTwo = new RestAdapter.Builder()
				.setClient(new OkClient( okHttpClient ) )
				.setServer(DarkSkiesClient.Dark_URL).build();

        //Create a instance of our forecastIO api interface
        Forecast forecastTwo = restAdapterTwo.create(Forecast.class);
        forecastTwo.weatherData(latLong, new forecastResponseHandler() );

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

            myListAdapter = new ExpandableListAdapterDaily(getActivity().getApplicationContext());

            for( DataForDaily.Day someDay: weatherInfo.daily.dailyDataList)
            {
                myListAdapter.addDays( someDay );

            }
            expandableListView.setAdapter(myListAdapter);

        }

        @Override
        public void failure( RetrofitError retrofitError )
        {
			Log.d("Fail" , "fail");
		}
    }
  }
