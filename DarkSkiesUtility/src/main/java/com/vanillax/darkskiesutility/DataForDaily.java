package com.vanillax.darkskiesutility;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mitch on 12/24/13.
 */
public class DataForDaily {
    public String summary;
    @SerializedName("data")
    public List<Day> dailyDataList;

public static class Day
    {
        public String time;
        public String summary;
        public String temperatureMin;
        public String temperatureMax;
        @SerializedName("temperatureMaxTime")
        public long maxTempTime;
        @SerializedName("temperatureMinTime")
        public long minTempTime;
        public double windSpeed;


    }

}