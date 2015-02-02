package com.example.joshua.cityguide.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.joshua.cityguide.fragments.PlaceListFragment;

public class TabPagerAdapter extends FragmentPagerAdapter {

    private final String[] TITLES = {"Bar", "Bistro", "Cafe"};
    private final String[] PLACE_TYPES = {
            GooglePlaceStreamAdapter.PLACE_BAR,
            GooglePlaceStreamAdapter.PLACE_BISTRO,
            GooglePlaceStreamAdapter.PLACE_CAFE,
    };

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public Fragment getItem(int position) {
        return PlaceListFragment.newInstance(PLACE_TYPES[position]);
    }

}
