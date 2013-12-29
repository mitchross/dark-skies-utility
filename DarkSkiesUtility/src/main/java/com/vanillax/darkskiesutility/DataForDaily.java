package com.vanillax.darkskiesutility;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mitch on 12/24/13.
 */
public class DataForDaily {
    String summary;
    @SerializedName("data")
    List<Day> dailyDataList;

public static class Day
    {
        String time;
        String summary;

    }

}