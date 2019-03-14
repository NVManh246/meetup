package com.rikkei.meetup.screen.EventDetail;

public interface EventDetailContract {
    interface View {
        void showUpdateSuccess();
        void showUpdateError(String error);
        void showFollowSuccess();
        void showFollowError(String error);
        void  showErrorServer();
    }

    interface Presenter {
        void followVenue(String token, long venueId);
        void updateStatusEvent(String token, long status, long eventId);
    }
}
