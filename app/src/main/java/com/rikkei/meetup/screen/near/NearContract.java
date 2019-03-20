package com.rikkei.meetup.screen.near;

import com.rikkei.meetup.data.model.event.Event;

import java.util.List;

public interface NearContract {
    interface View {
        void showEvents(List<Event> events);
        void showError();
    }

    interface Presenter {
        void getNearEvents(String token, int radius, String geoLong, String geoLat);
    }
}
