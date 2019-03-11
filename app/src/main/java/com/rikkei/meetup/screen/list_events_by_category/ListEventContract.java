package com.rikkei.meetup.screen.list_events_by_category;

import com.rikkei.meetup.data.model.event.Event;

import java.util.List;

public interface ListEventContract {
    interface View {
        void showEvents(List<Event> events);
        void showEventsToday(List<Event> events);
        void showEventsTomorrow(List<Event> events);
        void showEventsWeekend(List<Event> events);
        void showEventsNextWeek(List<Event> events);
        void showEventsEndMonth(List<Event> events);
        void showEventsNextMonth(List<Event> events);
        void showEventsForever(List<Event> events);
        void showError();
        void endData();
    }

    interface Presenter {
        void getEventsByCategory(int categoryId, int pageIndex, int pageSize);
    }
}
