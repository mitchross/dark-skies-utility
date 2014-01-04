package com.vanillax.darkskiesutility;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mitch on 12/23/13.
 */
public class Hourly {
    String summary;
    //This "data' has to match JSON key
    @SerializedName("data")
    List<DataForHourly> hourlyList;

    public class DataForHourly{
        int time;
        String summary;
    }

}