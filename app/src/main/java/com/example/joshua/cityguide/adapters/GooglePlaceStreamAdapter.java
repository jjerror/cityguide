package com.example.joshua.cityguide.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joshua.cityguide.R;
import com.example.joshua.cityguide.common.BusProvider;
import com.example.joshua.cityguide.events.DistanceNeededEvent;
import com.example.joshua.cityguide.models.pojo.MapSearchResults;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;

public class GooglePlaceStreamAdapter extends ArrayAdapter<MapSearchResults.Place> {

    public static final String PLACE_BAR = "bar";
    public static final String PLACE_CAFE = "cafe";
    public static final String PLACE_BISTRO = "restaurant";
    private LayoutInflater mInflater;
    private String mPlaceType;

    public GooglePlaceStreamAdapter(Context context, int textViewResourceId, String placeType) {
        super(context, textViewResourceId);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPlaceType = placeType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            // View doesn't exist so create it and create the holder
            view = mInflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            // Just get our existing holder
            holder = (ViewHolder) view.getTag();
        }

        // Populate via the holder for speed
        MapSearchResults.Place place = getItem(position);

        // Populate the item contents
        holder.placeName.setText(place.name);

        switch (mPlaceType) {
            case PLACE_BAR:
                holder.icon.setImageResource(R.drawable.ic_bar);
                break;
            case PLACE_CAFE:
                holder.icon.setImageResource(R.drawable.ic_cafe);
                break;
            case PLACE_BISTRO:
                holder.icon.setImageResource(R.drawable.ic_bistro);
                break;
            default:
                holder.icon.setImageResource(R.drawable.ic_unknown);
        }

        if (place.rating != null) {
            for (int i = 0; i < Math.round(place.rating); i++) {
                holder.stars.get(i).setImageResource(R.drawable.star_pink);
            }
        }
        if (TextUtils.isEmpty(place.distance)) {
            BusProvider.getInstance().post(new DistanceNeededEvent(place));
        }
        // TODO subcribe to distance updated event to update the UI
        holder.distance.setText(place.distance);

        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.place_name) TextView placeName;
        @InjectView(R.id.icon) ImageView icon;
        @InjectView(R.id.distance) TextView distance;
        @InjectViews({R.id.star1, R.id.star2, R.id.star3, R.id.star4, R.id.star5})
        List<ImageView> stars;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

    }

}
