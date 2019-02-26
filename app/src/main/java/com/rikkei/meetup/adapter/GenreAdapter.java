package com.rikkei.meetup.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rikkei.meetup.R;
import com.rikkei.meetup.data.model.genre.Genre;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {

    private List<Genre> mGenres;
    private OnItemClickListener mListener;

    public GenreAdapter(List<Genre> genres, OnItemClickListener listener) {
        mGenres = genres;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_genre, viewGroup, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindView(mGenres.get(i));
    }

    @Override
    public int getItemCount() {
        return mGenres != null ? mGenres.size() : 0;
    }

    public void insertData(List<Genre> genres) {
        mGenres.addAll(genres);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_name_genre) TextView mTextNameGenre;
        private OnItemClickListener mListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            mListener = listener;
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.layout_genre)
        public void onItemClick() {
            mListener.onItemClick(getAdapterPosition());
        }

        private void bindView(Genre genre) {
            mTextNameGenre.setText(genre.getName());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
