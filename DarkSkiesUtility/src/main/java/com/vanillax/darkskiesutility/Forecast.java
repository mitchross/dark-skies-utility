package com.vanillax.darkskiesutility;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Mitch on 12/23/13.
 */
public interface Forecast{
    @GET("/{latlong}")
    void weatherData (
            @Path( "latlong" ) String latlong ,
            Callback< WeatherInfo > callback ) ;

}
