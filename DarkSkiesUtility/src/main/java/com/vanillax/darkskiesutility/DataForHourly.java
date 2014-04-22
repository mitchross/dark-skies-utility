package com.vanillax.darkskiesutility;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mitch on 12/23/13.
 */
public class DataForHourly{
	@SerializedName( "summary" )
	String overViewSummary;


	//This "data' has to match JSON key
	@SerializedName( "data" )
	public List<Hour> hourlyList;

	public class Hour
	{
		public long time;
		public String summary;

		@SerializedName( "precipProbability" )
		public double probability;

		public double temperature;
		public double windSpeed;
		public double cloudCover;

	}

}