package com.vanillax.darkskiesutility.adapter;







import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vanillax.darkskiesutility.fragment.DailyFragment;
import com.vanillax.darkskiesutility.fragment.HoursFragment;
import com.vanillax.darkskiesutility.fragment.TempFragment;

/**
 * Created by mitch on 2/3/14.
 */
public class TabsPagerAdapter  extends FragmentPagerAdapter {


    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index )
        {
            case 0:
                return new HoursFragment();
            case 1:
               return new DailyFragment();
            case 2:
               return new TempFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        //Tab count
        return 3;
    }
}
