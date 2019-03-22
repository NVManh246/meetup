package com.rikkei.meetup.common.observer;

import java.util.ArrayList;
import java.util.List;

public class NetworkData implements Subject{
    private List<Observer> mObservers;

    public NetworkData() {
        mObservers = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer observer) {
        mObservers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        mObservers.remove(observer);
    }

    @Override
    public void notifyObservers(int status) {
        for(Observer observer : mObservers) {
            observer.update(status);
        }
    }
}
