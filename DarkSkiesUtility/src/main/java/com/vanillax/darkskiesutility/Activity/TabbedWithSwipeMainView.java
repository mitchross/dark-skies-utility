package com.vanillax.darkskiesutility.activity;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;


import com.vanillax.darkskiesutility.adapter.TabsPagerAdapter;

/**
 * Created by mitch on 2/3/14.
 */
public class TabbedWithSwipeMainView extends FragmentActivity implements ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter myTabsPagerAdapter;
    private ActionBar actionBar;

    private String[] tabs = { "Hours" , "Temp"  , "Daily" };

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(com.vanillax.darkskiesutility.R.layout.tabbed_view);

        //Initialize
        viewPager = (ViewPager) findViewById(com.vanillax.darkskiesutility.R.id.pager);
        actionBar = getActionBar();
        myTabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager() );

        viewPager.setAdapter(myTabsPagerAdapter);
        actionBar.setHomeButtonEnabled( false );
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for ( String tab_name : tabs)
        {
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
        }

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

    }




    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {



    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}
