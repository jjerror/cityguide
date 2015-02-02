package com.example.joshua.cityguide.fragments;

import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.joshua.cityguide.R;
import com.example.joshua.cityguide.adapters.GooglePlaceStreamAdapter;
import com.example.joshua.cityguide.events.LocationChangedEvent;
import com.example.joshua.cityguide.models.GooglePlacesModel;
import com.example.joshua.cityguide.models.pojo.MapSearchResults;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A fragment representing a list of Items.
 */
public class PlaceListFragment extends BaseFragment {

    private static final String TAG = "PL";
    private static final String PLACE_TYPE = "PLACE_TYPE";

    /* When this amount of items is left in the ListView yet to be displayed we will start downloading more data (if available). */
    private static final int RUNNING_LOW_ON_DATA_THRESHOLD = 10;
    /* Scroll-handler for the ListView which can auto-load the next page of data. */
    private AbsListView.OnScrollListener mScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // Nothing to do
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            // Detect if the ListView is running low on data
            if (totalItemCount > 0 && totalItemCount - (visibleItemCount + firstVisibleItem) <= RUNNING_LOW_ON_DATA_THRESHOLD) {
                loadNextPage();
            }
        }
    };
    @InjectView(R.id.list_view) ListView mListView;
    @Inject GooglePlacesModel googlePlacesModel;
    private Subscription mCompositeSubscription;
    private String mPlaceType;
    private GooglePlaceStreamAdapter mAdapter;
    private Location mCurrentLocation;
    private MapSearchResults mMapSearchResults;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PlaceListFragment() {
    }

    /**
     * Create the fragment object
     *
     * @param placeType the type of place
     * @return the created fragment
     */
    public static PlaceListFragment newInstance(String placeType) {
        PlaceListFragment fragment = new PlaceListFragment();
        Bundle args = new Bundle();
        args.putString(PLACE_TYPE, placeType);
        fragment.setArguments(args);
        return fragment;
    }

    public void onPause() {
        super.onPause();
        clearSubscription();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlaceType = getArguments().getString(PLACE_TYPE);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

        mAdapter = new GooglePlaceStreamAdapter(getActivity(), 0, mPlaceType);
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(mScrollListener);
    }

    private void subscribeToAPI(Observable<MapSearchResults> observable) {
        synchronized (this) {
            // TODO better handling on error case
            mCompositeSubscription = observable
                    .subscribeOn(Schedulers.io())
                    .cache()
                    .retry(5)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(processMapSearchResults(),
                            new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    throwable.printStackTrace();
                                    clearSubscription();
                                }
                            }
                    );
        }
    }

    private Action1<MapSearchResults> processMapSearchResults() {
        return new Action1<MapSearchResults>() {
            @Override
            public void call(MapSearchResults result) {
                if (result == null) {
                    Log.e(TAG, "no result for " + mPlaceType);
                    return;
                }

                if (result.status.equals("OK")) {
                    if (mMapSearchResults == null) {
                        mMapSearchResults = result;
                    } else {
                        mMapSearchResults.next_page_token = result.next_page_token;
                        mMapSearchResults.results.addAll(result.results);
                    }
                    mAdapter.clear();
                    mAdapter.addAll(mMapSearchResults.results);
                    mAdapter.notifyDataSetChanged();

                    if (TextUtils.isEmpty(mMapSearchResults.next_page_token)) {
                        // TODO add footer to indicator end of stream
                        Log.i(TAG, "loaded all result for " + mPlaceType);
                    }
                }
                clearSubscription();

                Log.i(TAG, String.format("loaded %d for %s", result.results.size(), mPlaceType));
            }
        };
    }

    private void clearSubscription() {
        synchronized (this) {
            if (mCompositeSubscription != null) {
                mCompositeSubscription.unsubscribe();
                mCompositeSubscription = null;
            }
        }
    }

    private boolean hasSubscription() {
        synchronized (this) {
            return mCompositeSubscription != null && !mCompositeSubscription.isUnsubscribed();
        }
    }

    private void loadNextPage() {

        if (hasSubscription() || mCurrentLocation == null) {
            return;
        }

        Observable<MapSearchResults> o;
        if (mMapSearchResults == null) {
            o = googlePlacesModel.searchPlaces(mCurrentLocation, mPlaceType);
        } else if (!TextUtils.isEmpty(mMapSearchResults.next_page_token)) {
            o = googlePlacesModel.searchPlaces(mMapSearchResults.next_page_token);
        } else {
            return;
        }

        subscribeToAPI(o);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapSearchResults != null) {
            mAdapter.clear();
            mAdapter.addAll(mMapSearchResults.results);
        }
    }

    @Subscribe
    public void onLocationChangedEvent(LocationChangedEvent event) {
        mCurrentLocation = event.location;
        loadNextPage();
    }

}
