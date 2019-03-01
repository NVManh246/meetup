package com.rikkei.meetup.screen.search;

import com.rikkei.meetup.data.model.event.Event;

import java.util.List;

public interface SearchContract {
    interface View {
        void showEvents(List<Event> events);
        void showError();
        void noResultSearching();
    }

    interface Presenter {
        void getEventsByKeyword(String keyword, int pageIndex, int pageSize);
    }
}
