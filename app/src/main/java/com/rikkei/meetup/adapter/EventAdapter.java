package com.rikkei.meetup.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private List<Event> mEvents;
    private OnItemClickListener mListener;

    public EventAdapter(List<Event> events, OnItemClickListener listener) {
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
                    .inflate(R.layout.item_event, viewGroup, false);
            return new ItemViewHolder(view, viewGroup.getContext(), mListener);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_loading, viewGroup, false);
            return new LoadingViewHodler(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if(viewHolder instanceof ItemViewHolder) {
            ((ItemViewHolder) viewHolder).bindView(mEvents.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return mEvents != null ? mEvents.size() : 0;
    }

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

    public void clearAll() {
        mEvents.clear();
        notifyDataSetChanged();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private Context mContext;

        private ImageView mImageEvent;
        private TextView mTextNameEvent;
        private TextView mTextDescription;
        private TextView mTextDate;
        private ImageView mImageJoinerCount;
        private TextView mTextJoinerCount;
        private ConstraintLayout mLayoutEvent;

        private OnItemClickListener mListener;

        public ItemViewHolder(@NonNull View itemView, Context context, OnItemClickListener listener) {
            super(itemView);
            mContext = context;
            mListener = listener;
            initView(itemView);
        }

        private void initView(View itemView) {
            mImageEvent = itemView.findViewById(R.id.image_event);
            mTextNameEvent = itemView.findViewById(R.id.text_name_event);
            mTextDescription = itemView.findViewById(R.id.text_description_event);
            mTextDate = itemView.findViewById(R.id.text_date_event);
            mImageJoinerCount = itemView.findViewById(R.id.image_joiner);
            mTextJoinerCount = itemView.findViewById(R.id.text_joiner);
            mLayoutEvent = itemView.findViewById(R.id.layout_event);
            mLayoutEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(getAdapterPosition());
                }
            });
        }

        private void bindView(Event event) {
            if(event.getPhoto() != null) {
                Glide.with(mContext).load(event.getPhoto()).into(mImageEvent);
            }
            mTextNameEvent.setText(event.getName());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mTextDescription.setText(Html.fromHtml(
                        event.getDescriptionHtml(),
                        Html.FROM_HTML_MODE_COMPACT)
                );
            } else {
                mTextDescription.setText(Html.fromHtml(event.getDescriptionHtml()));
            }
            try {
                mTextDate.setText(StringUtils.getDateEventField(mContext, event));
            } catch (ParseException e) {
            }
            if(event.getGoingCount() > 0) {
                mTextJoinerCount.setText(String.valueOf(event.getGoingCount()));
            } else {
                mImageJoinerCount.setVisibility(View.GONE);
                mTextJoinerCount.setVisibility(View.GONE);
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
