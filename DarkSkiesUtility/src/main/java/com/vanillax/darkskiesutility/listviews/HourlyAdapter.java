package com.vanillax.darkskiesutility.listviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vanillax.darkskiesutility.DataForHourly;
import com.vanillax.darkskiesutility.R;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by mitch on 4/21/14.
 */
public class HourlyAdapter extends ArrayAdapter<DataForHourly.Hour>
{
	protected Context context;

	public HourlyAdapter( Context context, int resource , List<DataForHourly.Hour> objects )
	{
		super( context, resource , objects);

		this.context = context;
	}

	@Override
	public View getView( int position, View view, ViewGroup parent )
	{
		ViewHolder myView;

		DataForHourly.Hour myHour = this.getItem( position );

		if ( view == null )
		{
			view = LayoutInflater.from ( getContext() ).inflate( R.layout.hours_row , parent , false );
			myView = new ViewHolder( view );
			view.setTag( myView );
		}
		else
		{
			myView = (ViewHolder) view.getTag();
		}

		myView.time.setText( "Time " + String.valueOf( convertUnixTimeStampToClockTime( myHour.time  ) ) );
		myView.summaryDetail.setText( String.valueOf( myHour.summary ) );
		myView.chance.setText("Chance " + String.valueOf( myHour.probability )  );
		myView.temp.setText("Temperature " + String.valueOf( myHour.temperature ) + " F" );
		myView.windSpeed.setText("WindSpeed " + String.valueOf( myHour.windSpeed ) + " MPH" );
		myView.cloudCover.setText( "Cloud Cover " +String.valueOf( myHour.cloudCover ) );

		return view;

	}

	public static String convertUnixTimeStampToClockTime ( long unixTime )
	{
		Long test = unixTime;
		DateTime normalTime = new DateTime(unixTime* 1000L);
		Date d = new Date(unixTime * 1000L);
		SimpleDateFormat f = new SimpleDateFormat("h:mm a");
		f.setTimeZone( TimeZone.getDefault());
		String s = f.format(d);
		return s;
	}

	protected static class ViewHolder
	{
		@InjectView( R.id.time )
		TextView time;

		@InjectView( R.id.summary_detail )
		TextView summaryDetail;

		@InjectView( R.id.chance )
		TextView chance;

		@InjectView( R.id.temperature )
		TextView temp;

		@InjectView( R.id.wind_speed )
		TextView windSpeed;

		@InjectView( R.id.cloud_cover )
		TextView cloudCover;


		public ViewHolder( View view )
		{
			ButterKnife.inject( this, view );
		}
	}

}
