package com.vanillax.darkskiesutility.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import com.vanillax.darkskiesutility.R;

/**
 * Created by mitch on 2/3/14.
 */
public class HoursFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hours_tab , container , false);
        return rootView;
    }
}
