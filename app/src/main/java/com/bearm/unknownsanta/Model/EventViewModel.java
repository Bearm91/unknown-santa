package com.bearm.unknownsanta.Model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bearm.unknownsanta.Repositories.EventRepository;

import java.util.List;

public class EventViewModel extends AndroidViewModel {


    private EventRepository eventRepository;
    private LiveData<List<Event>> eventList;

    public EventViewModel(@NonNull Application application) {
        super(application);

        eventRepository = new EventRepository(application);

        Event event = new Event();

        eventList = eventRepository.getEventList();

    }

    public void insert(Event event) {
        eventRepository.insert(event);
    }


    public void deleteEvent(int eventId){
        eventRepository.delete(eventId);
    }

    public LiveData<List<Event>> getEventList() {
        return eventList;
    }
}
