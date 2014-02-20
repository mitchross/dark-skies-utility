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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mitch on 12/29/13.
 */
public class ExpandableListAdapterTwo extends BaseExpandableListAdapter {

    private Context context;

    protected HashMap<String , ArrayList<String> > individualDayTitleHash;
    protected ArrayList<String> individualDayTitles;
    protected HashMap<String , ArrayList<DataForDaily.Day> > individualDayInfoHash;







    public ExpandableListAdapterTwo(Context context)
    {
        this.context = context;
        individualDayTitleHash = new HashMap<String, ArrayList<String>>();
        individualDayTitles = new ArrayList<String>();
        individualDayInfoHash = new HashMap<String, ArrayList<DataForDaily.Day>>();



    }

    public void addDays ( DataForDaily.Day aDay)
    {
        String key;
        //Titles aka groups aka days
        long dv = Long.valueOf(aDay.time)*1000;// its need to be in milisecond
        DateTime day = new DateTime( dv );
        String dayName = day.dayOfWeek().getAsText();
        key = dayName;


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


        individualDayTitles.add(key);
        individualDayTitleHash.put(key, individualDayTitles);

    }

    public void addDailyWeatherInfo ( DataForDaily.Day myDayToAdd)
    {
        String key = myDayToAdd.time;

        individualDayTitles.add( key );
        //Add the timestamped hour to the child hashmap
        if( individualDayInfoHash.containsKey( key ) )
        {
            ArrayList<DataForDaily.Day> myDayInHash = individualDayInfoHash.get(key);
            myDayInHash.add( myDayToAdd );
        }
        else
        {
            ArrayList<DataForDaily.Day> myList = new ArrayList();
            myList.add( myDayToAdd );
            individualDayInfoHash.put( key , myList);
        }
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
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {



        final String key = individualDayTitles.get(groupPosition).toString();

        if( individualDayInfoHash.containsKey(key ))
        {
            DataForDaily.Day  day = individualDayInfoHash.get(key).get(childPosition);
            Log.d("test", "this is a tset");


            if(convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item, null);
            }

            TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
            txtListChild.setText(day.summary);

            TextView txtListChildTemp = (TextView) convertView.findViewById(R.id.lblListItemTemp);
             txtListChildTemp.setText(day.temperatureMax);



        }



        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }
}
