package com.vanillax.darkskiesutility.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.vanillax.darkskiesutility.DarkSkiesClient;
import com.vanillax.darkskiesutility.DataForDaily;
import com.vanillax.darkskiesutility.listviews.ExpandableListAdapterDaily;
import com.vanillax.darkskiesutility.Forecast;
import com.vanillax.darkskiesutility.R;
import com.vanillax.darkskiesutility.WeatherInfo;
import com.vanillax.darkskiesutility.activity.ShowLocationTestActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
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
        View rootView = inflater.inflate(R.layout.fragment_temp_tab , container , false);

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

        setUpLocationManager();



        //Get the listview
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);


        cList = new ArrayList<String>(  );

        //connectService();
        connectForecastIO();


        return rootView;
    }


    private void setUpLocationManager()
    {
        // Get the location manager
        locationManager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE);
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
        forecast.weatherData(latLong, new forecastResponseHandler() );

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

        }
    }
  }
