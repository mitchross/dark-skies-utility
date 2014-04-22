package com.vanillax.darkskiesutility.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.vanillax.darkskiesutility.DarkSkiesClient;
import com.vanillax.darkskiesutility.Forecast;
import com.vanillax.darkskiesutility.R;
import com.vanillax.darkskiesutility.WeatherInfo;
import com.vanillax.darkskiesutility.listviews.HourlyAdapter;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by mitch on 2/3/14.
 */
public class HoursFragment extends Fragment {


	ListView hoursListview;
	HourlyAdapter myHourlyAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hours_tab , container , false);

		hoursListview = (ListView) rootView.findViewById( R.id.hours_listview );


        return rootView;
    }

	@Override
	public void onResume()
	{
		super.onResume();
		connectForecastIO();
	}


	public void connectForecastIO()
	{
		//Create the simple Rest adapter which points to the Forecast.io endpoints
		RestAdapter restAdapter = new RestAdapter.Builder().setServer( DarkSkiesClient.Dark_URL).build();

		//Create a instance of our forecastIO api interface
		Forecast forecast = restAdapter.create(Forecast.class);
		forecast.weatherData("42,-85", new forecastResponseHandler() );

	}


	public class forecastResponseHandler implements Callback<WeatherInfo>
	{

		@Override
		public void success( WeatherInfo weatherInfo, Response response )
		{
//			List<DataForHourly.Hour> myHoursReceivedList = null;
//
//			//myHourlyAdapter = new HourlyAdapter( getActivity().getApplicationContext() )
//
//			for( DataForHourly.Hour myHour: weatherInfo.hourly.hourlyList)
//			{
//				myHoursReceivedList.add( myHour );
//
//			}
			myHourlyAdapter = new HourlyAdapter( getActivity().getApplicationContext() ,android.R.id.list, weatherInfo.hourly.hourlyList );
			hoursListview.setAdapter( myHourlyAdapter );

		}

		@Override
		public void failure( RetrofitError retrofitError )
		{

		}
	}

}
