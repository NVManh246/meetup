package com.rikkei.meetup.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rikkei.meetup.R;
import com.rikkei.meetup.data.model.event.Event;
import com.rikkei.meetup.ultis.StringUtils;

import java.text.ParseException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventSmallAdapter extends RecyclerView.Adapter<EventSmallAdapter.ViewHolder> {

    private List<Event> mEvents;
    private OnItemClickListener mListener;

    public EventSmallAdapter(List<Event> events, OnItemClickListener listener) {
        mEvents = events;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_event_small, viewGroup, false);
        return new ViewHolder(view, viewGroup.getContext(), mEvents, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindItemView(mEvents.get(i));
    }

    @Override
    public int getItemCount() {
        return mEvents != null ? mEvents.size() : 0;
    }

    public void insertData(List<Event> events) {
        mEvents.addAll(events);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private Context mContext;
        private List<Event> mEvents;
        private OnItemClickListener mListener;

        @BindView(R.id.image_event) ImageView mImageEvent;
        @BindView(R.id.text_name_event) TextView mTextName;
        @BindView(R.id.text_date_event) TextView mTextDate;
        @BindView(R.id.text_joiner) TextView mTextJoinerCount;
        @BindView(R.id.image_status) ImageView mImageStatus;
        @BindView(R.id.text_description_event) TextView mTextDescription;

        public ViewHolder(@NonNull final View itemView, Context context,
                          List<Event> events, OnItemClickListener listener) {
            super(itemView);
            mContext = context;
            mEvents = events;
            mListener = listener;
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.layout_event)
        public void onItemClick() {
            mListener.onItemClick(mEvents.get(getAdapterPosition()));
        }

        private void bindItemView(Event event) {
            if(event.getPhoto() != null) {
                Glide.with(mContext).load(event.getPhoto()).into(mImageEvent);
            }
            mTextName.setText(event.getName());
            if(!TextUtils.isEmpty(event.getDescriptionHtml())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mTextDescription.setText(Html.fromHtml(
                            event.getDescriptionHtml(),
                            Html.FROM_HTML_MODE_COMPACT)
                    );
                } else {
                    mTextDescription.setText(Html.fromHtml(event.getDescriptionHtml()));
                }
            }
            try {
                mTextDate.setText(StringUtils.getDateEventField(mContext, event));
            } catch (ParseException e) {
            }
            mTextJoinerCount.setText(String.valueOf(event.getGoingCount()));
            switch (event.getMyStatus()) {
                case 0:
                    mImageStatus.setImageResource(View.VISIBLE);
                    break;
                case Event.STATUS_GOING:
                    mImageStatus.setImageResource(R.drawable.ic_star_red_24dp);
                    break;
                case Event.STATUS_WENT:
                    mImageStatus.setImageResource(R.drawable.ic_star_star_24dp);
                    break;
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }
}
