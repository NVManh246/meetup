package com.rikkei.meetup.adapter;

import com.rikkei.meetup.R;
import com.rikkei.meetup.data.model.event.Event;

import java.util.List;

public class EventHorizontalAdapter extends BaseEventAdapter {

    public EventHorizontalAdapter(List<Event> events, OnItemClickListener listener) {
        super(events, listener);
    }

    @Override
    public int getLayout() {
        return R.layout.item_horizontal_event;
    }
}
