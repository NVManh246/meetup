package com.rikkei.meetup.screen.profile;

import com.rikkei.meetup.data.model.event.Event;
import com.rikkei.meetup.data.model.event.Venue;

import java.util.List;

public interface EventStatusContract {
    interface View {
        void hideProgress();
        void showEvents(List<Event> events);
        void showVenues(List<Venue> venues);
        void showError();
    }

    interface Presenter {
        void getMyEvetns(String token, int status);
        void getVenuesFollowed(String token);
    }
}
