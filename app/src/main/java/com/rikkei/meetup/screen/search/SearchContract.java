package com.rikkei.meetup.screen.search;

import com.rikkei.meetup.data.model.event.Event;

import java.util.List;

public interface SearchContract {
    interface View {
        void showEventsUpComing(List<Event> events);
        void showEventsPass(List<Event> events);
        void setCount();
        void showError();
        void noResultSearching();
    }

    interface Presenter {
        void getEventsByKeyword(String token, String keyword, int pageIndex, int pageSize);
    }
}
