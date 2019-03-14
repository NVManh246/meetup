package com.rikkei.meetup.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rikkei.meetup.R;
import com.rikkei.meetup.data.model.news.News;
import com.rikkei.meetup.ultis.StringUtils;

import java.text.ParseException;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private List<News> mNews;
    private OnItemClickListener mListener;

    public NewsAdapter(List<News> news, OnItemClickListener listener) {
        mNews = news;
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return mNews.get(position) != null ? VIEW_TYPE_ITEM : VIEW_TYPE_LOADING;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_news, viewGroup, false);
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
            ((ItemViewHolder) viewHolder).bindView(mNews.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return mNews != null ? mNews.size() : 0;
    }

    public void insertData(List<News> listNews) {
        if(mNews.size() == 0) {
            mNews.addAll(listNews);
            mNews.add(null);
            notifyDataSetChanged();
        } else {
            int oldSize = mNews.size();
            mNews.remove(mNews.size() - 1);
            mNews.addAll(listNews);
            mNews.add(null);
            notifyItemRangeInserted(oldSize, mNews.size());
        }
    }

    public void removeItemNull() {
        if (!mNews.isEmpty() && mNews.get(mNews.size() - 1) == null) {
            mNews.remove(mNews.size() - 1);
            notifyItemRemoved(mNews.size());
        }
    }

    public void clearAll() {
        mNews.clear();
        notifyDataSetChanged();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private Context mContext;

        private ImageView mImageNews;
        private TextView mTextTitle;
        private TextView mTextPublicDate;
        private TextView mTextAuthor;
        private TextView mTextFeed;
        private ConstraintLayout mLayoutNews;

        private OnItemClickListener mListener;

        public ItemViewHolder(@NonNull View itemView, Context context, OnItemClickListener listener) {
            super(itemView);
            mContext = context;
            mListener = listener;
            initView(itemView);
        }

        private void initView(View itemView) {
            mImageNews = itemView.findViewById(R.id.image_news);
            mTextTitle = itemView.findViewById(R.id.text_title_news);
            mTextPublicDate = itemView.findViewById(R.id.text_public_date_news);
            mTextAuthor = itemView.findViewById(R.id.text_author_news);
            mTextFeed = itemView.findViewById(R.id.text_feed_news);
            mLayoutNews = itemView.findViewById(R.id.layout_news);
            mLayoutNews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(getAdapterPosition());
                }
            });
        }

        private void bindView(News news) {
            if(news.getThumbImg() != null) {
                Glide.with(mContext).load(news.getThumbImg()).into(mImageNews);
            } else {
                mImageNews.setVisibility(View.GONE);
            }
            mTextTitle.setText(news.getTitle());
            try {
                mTextPublicDate.setText(StringUtils.covertDate(mContext, news.getPublicDate()));
            } catch (ParseException e) {
            }
            if (!TextUtils.isEmpty(news.getAuthor())) {
                mTextAuthor.setText(news.getAuthor());
            }
            mTextFeed.setText(news.getFeed());
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
