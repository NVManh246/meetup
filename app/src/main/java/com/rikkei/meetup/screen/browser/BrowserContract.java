package com.rikkei.meetup.screen.browser;

import com.rikkei.meetup.data.model.genre.Genre;

import java.util.List;

public interface BrowserContract {
    interface View {
        void showEvents(List<Genre> genres);
        void showError();
    }

    interface Presenter {
        void getGenres();
    }
}
