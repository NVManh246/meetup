package com.rikkei.meetup.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
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

public abstract class BaseEventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private List<Event> mEvents;
    private EventAdapter.OnItemClickListener mListener;

    public BaseEventAdapter(List<Event> events, EventAdapter.OnItemClickListener listener) {
        mEvents = events;
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return mEvents.get(position) != null ? VIEW_TYPE_ITEM : VIEW_TYPE_LOADING;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(getLayout(), viewGroup, false);
            return new EventAdapter.ItemViewHolder(view, viewGroup.getContext(), mListener);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_loading, viewGroup, false);
            return new EventAdapter.LoadingViewHodler(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if(viewHolder instanceof EventAdapter.ItemViewHolder) {
            ((BaseEventAdapter.ItemViewHolder) viewHolder).bindView(mEvents.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return mEvents != null ? mEvents.size() : 0;
    }

    public abstract int getLayout();

    public void insertData(List<Event> events) {
        if(mEvents.size() == 0) {
            mEvents.addAll(events);
            mEvents.add(null);
            notifyDataSetChanged();
        } else {
            int oldSize = mEvents.size();
            mEvents.remove(mEvents.size() - 1);
            mEvents.addAll(events);
            mEvents.add(null);
            notifyItemRangeInserted(oldSize, mEvents.size());
        }
    }

    public void insertDataNotLoadMore(List<Event> events) {
        mEvents.clear();
        mEvents.addAll(events);
        notifyDataSetChanged();
    }

    public void clearAll() {
        mEvents.clear();
        notifyDataSetChanged();
    }

    public void removeItemNull() {
        if (!mEvents.isEmpty() && mEvents.get(mEvents.size() - 1) == null) {
            mEvents.remove(mEvents.size() - 1);
            notifyItemRemoved(mEvents.size());
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private Context mContext;

        @BindView(R.id.image_event) ImageView mImageEvent;
        @BindView(R.id.text_name_event) TextView mTextNameEvent;
        @BindView(R.id.text_description_event) TextView mTextDescription;
        @BindView(R.id.text_date_event) TextView mTextDate;
        @BindView(R.id.image_joiner) ImageView mImageJoinerCount;
        @BindView(R.id.text_joiner) TextView mTextJoinerCount;
        @BindView(R.id.image_status) ImageView mImageStatus;

        private EventAdapter.OnItemClickListener mListener;

        public ItemViewHolder(@NonNull View itemView, Context context, EventAdapter.OnItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = context;
            mListener = listener;
        }

        @OnClick(R.id.layout_event)
        public void onItemClick() {
            mListener.onItemClick(getAdapterPosition());
        }

        private void bindView(Event event) {
            if(event.getPhoto() != null) {
                Glide.with(mContext).load(event.getPhoto()).into(mImageEvent);
            }
            mTextNameEvent.setText(event.getName());
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
            if(event.getGoingCount() > 0) {
                mImageJoinerCount.setVisibility(View.VISIBLE);
                mTextJoinerCount.setVisibility(View.VISIBLE);
                mTextJoinerCount.setText(String.valueOf(event.getGoingCount()));
            } else {
                mImageJoinerCount.setVisibility(View.INVISIBLE);
                mTextJoinerCount.setVisibility(View.INVISIBLE);
            }

            switch (event.getMyStatus()) {
                case 0:
                    mImageStatus.setVisibility(View.INVISIBLE);
                    break;
                case Event.STATUS_GOING:
                    mImageStatus.setVisibility(View.VISIBLE);
                    mImageStatus.setImageResource(R.drawable.ic_star_red_24dp);
                    break;
                case Event.STATUS_WENT:
                    mImageStatus.setVisibility(View.VISIBLE);
                    mImageStatus.setImageResource(R.drawable.ic_star_star_24dp);
                    break;
            }
        }
    }

    public static class LoadingViewHodler extends RecyclerView.ViewHolder {
        public LoadingViewHodler(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
