package com.vanillax.darkskiesutility;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Mitch on 12/29/13.
 */
public class ExpandableListAdapterTwo extends BaseExpandableListAdapter {

    private Context context;

    protected ArrayList<String> individualDayTitles;
    protected HashMap<String , ArrayList<DataForDaily.Day> > individualDayInfoHash;
    protected ArrayList<String> groupTitlesKey;







    public ExpandableListAdapterTwo(Context context)
    {
        this.context = context;
        individualDayTitles = new ArrayList<String>();
        individualDayInfoHash = new HashMap<String, ArrayList<DataForDaily.Day>>();
        groupTitlesKey = new ArrayList<String>();



    }

    public void addDays ( DataForDaily.Day aDay)
    {
        String key = aDay.time;
        //Titles aka groups aka days
        long dv = Long.valueOf(aDay.time)*1000;// its need to be in milisecond
        DateTime day = new DateTime( dv );
        String dayName = day.dayOfWeek().getAsText();



        //Add the timestamped hour to the child hashmap
        if( individualDayInfoHash.containsKey( key ) )
        {
            ArrayList<DataForDaily.Day> myDayInHash = individualDayInfoHash.get(key);
            myDayInHash.add( aDay );
        }


        //Children aka days
        ArrayList<DataForDaily.Day> myDays = new ArrayList();
        myDays.add( aDay );
        individualDayInfoHash.put(key, myDays);


        individualDayTitles.add(dayName);
        groupTitlesKey.add(key);


    }



    @Override
    public int getGroupCount() {
     return   individualDayTitles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        int count = 1;
        String key = individualDayTitles.get(groupPosition).toString();
        if(individualDayInfoHash.containsKey(key))
        {
            count = individualDayInfoHash.get(key).size();
        }
        return count;
    }

    @Override
    public Object getGroup(int groupPostition) {
        return individualDayTitles.get(groupPostition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        String key = individualDayTitles.get(groupPosition).toString();
        return individualDayInfoHash.get(key).get(childPosition);

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPostion, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpandable, View convertView, ViewGroup parent) {

        String headerTitle = individualDayTitles.get(groupPosition);
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);

        }



        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View view, ViewGroup parent) {



        final String key2 = groupTitlesKey.get(groupPosition);

            DataForDaily.Day  day = individualDayInfoHash.get(key2).get(childPosition);
            ChildViewHolder viewHolder;

            if(view != null )
            {
                viewHolder = (ChildViewHolder) view.getTag();

            }
            else
            {
                view = LayoutInflater.from( context ).inflate(R.layout.list_item , parent , false);
                viewHolder = new ChildViewHolder(view);
                view.setTag(viewHolder);
            }


            viewHolder.summaryText.setText("Summary: " + day.summary);
            viewHolder.tempMinText.setText("Min Temp(F) :" + day.temperatureMin);
            viewHolder.tempMinTimeText.setText("Min Temp Time: " + convertUnixTimeStampToClockTime(day.minTempTime) );
            viewHolder.tempMaxText.setText("Max Temp (F): " + day.temperatureMax);
            viewHolder.tempMaxTimeText.setText("Max Temp Time: " +String.valueOf( convertUnixTimeStampToClockTime(day.maxTempTime)));
            viewHolder.windSpeedText.setText(String.valueOf("Wind Speed(MPH): " + day.windSpeed));

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }

    public static String convertUnixTimeStampToClockTime ( long unixTime )
    {
        Long test = unixTime;
        DateTime normalTime = new DateTime(unixTime* 1000L);
        Date d = new Date(unixTime * 1000L);
        SimpleDateFormat f = new SimpleDateFormat("h:mm a");
        f.setTimeZone(TimeZone.getDefault());
        String s = f.format(d);
        return s;
    }


    static class ChildViewHolder
    {
        @InjectView(R.id.summary)
        TextView summaryText;

        @InjectView(R.id.temp_min)
        TextView tempMinText;

        @InjectView(R.id.temp_min_time)
        TextView tempMinTimeText;

        @InjectView(R.id.temp_max)
        TextView tempMaxText;

        @InjectView(R.id.temp_max_time)
        TextView tempMaxTimeText;

        @InjectView(R.id.wind_speed)
        TextView windSpeedText;

        public ChildViewHolder( View view)
        {
            ButterKnife.inject(this, view);
        }

    }



}
