package com.vanillax.darkskiesutility;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;

/**
 * Created by Mitch on 12/23/13.
 */
public interface Forecast{
	@Headers("Cache-Control: public, max-age=600, s-maxage=600")
    @GET("/{latlong}")
    void weatherData (
            @Path( "latlong" ) String latlong ,
            Callback< WeatherInfo > callback ) ;

}
