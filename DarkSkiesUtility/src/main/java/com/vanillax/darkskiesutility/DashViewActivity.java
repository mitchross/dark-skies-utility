package com.vanillax.darkskiesutility;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DashViewActivity extends ActionBarActivity {

    DarkSkiesClient client;
	TextView testText;
	ListView listView;
	ArrayList <String> cList;
	ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		cList = new ArrayList<String>(  );

		connectService();
        connectForecastIO();



		testText = (TextView) findViewById( R.id.testTextView );
		listView = (ListView)findViewById( R.id.listView );








    }

    public void connectForecastIO()
    {
        //Create the simple Rest adapter which points to the Forecast.io endpoints
        RestAdapter restAdapter = new RestAdapter.Builder().setServer(DarkSkiesClient.Dark_URL).build();

        //Create a instance of our forecastIO api interface
        Forecast forecast = restAdapter.create(Forecast.class);



        Callback cb = new Callback< WeatherInfo > (){
            @Override
            public void success(WeatherInfo weatherInfo, Response response) {
                System.out.println( weatherInfo.timezone + " break2 " + weatherInfo.currently.summary );
                System.out.println( weatherInfo.timezone + " break2 " + weatherInfo.currently.cloudCover );


                List<DataForHourly> hours = weatherInfo.hourly.data;
                for(DataForHourly d : hours)
                {

                    long dv = Long.valueOf(d.time)*1000;// its need to be in milisecond
                    Date df = new java.util.Date(dv);
                    String vv = new SimpleDateFormat("MM dd, yyyy hh:mma").format(df);

                    System.out.println("hour " + vv + " summary " + d.summary);
                }

                System.out.println(" size "  + weatherInfo.hourly.data.size());


            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e("error", "error message");
            }
        };
        forecast.weatherData("42,-85", cb);


    }



	public  void connectService()
	{
		// Create a very simple REST adapter which points the GitHub API endpoint.
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setServer(GitHubClient.API_URL)
				.build();

		// Create an instance of our GitHub API interface.
		GitHub github = restAdapter.create(GitHub.class);



		Callback callback = new  Callback<List< Contributor >>()
		{
			@Override
			public void success( List<Contributor> contributors, Response response )
			{
				for (Contributor contributor : contributors) {
					Log.d( "mytag", contributor.myLogin + " (" + contributor.contributions + ")" );
					testText.setText( contributor.myLogin );
					cList.add( contributor.getMyLogin() );
				}

				arrayAdapter = new ArrayAdapter( getApplicationContext(), android.R.layout.simple_list_item_1 , cList );
				listView.setAdapter( arrayAdapter );
			}

			@Override
			public void failure( RetrofitError retrofitError )
			{

			}
		};

		// Fetch and print a list of the contributors to this library.
		github.contributors("square", "retrofit" , callback);

	}




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
