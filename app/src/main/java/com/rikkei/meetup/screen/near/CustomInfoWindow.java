package com.rikkei.meetup.screen.near;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.rikkei.meetup.R;
import com.rikkei.meetup.data.model.event.Event;
import com.rikkei.meetup.ultis.StringUtils;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    private Context mContext;

    public CustomInfoWindow(Context context) {
        mContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_info_window, null);
        TextView mTextName = view.findViewById(R.id.text_name_event);
        TextView mTextAddress = view.findViewById(R.id.text_address_venue);
        TextView mTextDistance = view.findViewById(R.id.text_distance);

        Event event = (Event) marker.getTag();
        mTextName.setText(event.getName());
        mTextAddress.setText(event.getVenue().getName());
        mTextDistance.setText(StringUtils.getDistance(Integer.valueOf(marker.getSnippet())));

        return view;
    }
}
