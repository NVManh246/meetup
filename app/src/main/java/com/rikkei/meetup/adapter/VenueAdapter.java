package com.rikkei.meetup.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rikkei.meetup.R;
import com.rikkei.meetup.data.model.event.Venue;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class VenueAdapter extends RecyclerView.Adapter<VenueAdapter.ViewHolder> {

    private List<Venue> mVenues;

    public VenueAdapter(List<Venue> venues) {
        mVenues = venues;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_venue, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindItem(mVenues.get(i));
    }

    @Override
    public int getItemCount() {
        return mVenues.size();
    }

    public void insertData(List<Venue> venues) {
        mVenues.addAll(venues);
        notifyDataSetChanged();
    }

    public void clearAll() {
        mVenues.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_des) TextView mTextDes;
        @BindView(R.id.text_name_venue) TextView mTextNameVenues;
        @BindView(R.id.text_address_venue) TextView mTextAddressVenues;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindItem(Venue venue) {
            mTextDes.setText(venue.getDescription());
            mTextNameVenues.setText(venue.getName());
            mTextAddressVenues.setText(venue.getContactAddress());
        }
    }
}
