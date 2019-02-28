package com.rikkei.meetup.screen.home.popular;

import com.rikkei.meetup.data.model.event.Event;

import java.util.List;

public class PopularContract {
    interface View {
        void showEvents(List<Event> events);
        void showError();
    }

    interface Presenter {
        void getEvents(int pageIndex, int pageSize);
    }
}
