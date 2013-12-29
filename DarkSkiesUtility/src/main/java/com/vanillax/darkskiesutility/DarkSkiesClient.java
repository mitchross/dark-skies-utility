package com.vanillax.darkskiesutility;

import java.util.List;

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

}
