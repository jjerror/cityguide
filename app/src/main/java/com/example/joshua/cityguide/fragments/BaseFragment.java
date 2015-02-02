package com.example.joshua.cityguide.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.joshua.cityguide.CityGuideApplication;
import com.example.joshua.cityguide.common.BusProvider;

public class BaseFragment extends Fragment {

    public BaseFragment() {
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        ((CityGuideApplication) getActivity().getApplication()).inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        BusProvider.getInstance().unregister(this);
        super.onPause();
    }
}
