package com.vanillax.darkskiesutility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by mitchross on 12/11/13.
 */
public class DarkSkiesClient
{

	public static final String Dark_URL = "https://api.forecast.io/forecast/6ed261da1e77a14fd6e183cec77f642e";


	public static class WeatherInfo{

		String timezone;
        //Objects in the JSON Response
		Currently currently;
		Hourly hourly;
	}

	public static class Hourly {
		String summary;
		//This "data' has to match JSON key
		List<DataForHourly> data;
	}

	public static class DataForHourly{
		int time;
		String summary;
	}

	public interface Forecast{
		@GET("/{latlong}")
		WeatherInfo  weatherLocation(
            @Path( "latlong" ) String latlong ) ;
	}


	public static void main(String [] args) {



		RestAdapter restAdapter = new RestAdapter.Builder().setServer( Dark_URL ).build();

		Forecast forecast = restAdapter.create( Forecast.class );


		WeatherInfo w = forecast.weatherLocation( "42,-85");

		System.out.println( w.timezone + " break " + w.currently.summary );
		System.out.println( w.timezone + " break " + w.currently.cloudCover );

		List<DataForHourly> hours = w.hourly.data;
		for(DataForHourly d : hours)
		{

			long dv = Long.valueOf(d.time)*1000;// its need to be in milisecond
			Date df = new java.util.Date(dv);
			String vv = new SimpleDateFormat("MM dd, yyyy hh:mma").format(df);

			System.out.println("hour " + vv + " summary " + d.summary);
		}

		System.out.println(" size "  + w.hourly.data.size());





	}
}
