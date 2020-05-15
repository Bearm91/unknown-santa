package com.bearm.unknownsanta.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bearm.unknownsanta.model.Event;
import com.bearm.unknownsanta.repositories.EventRepository;

import java.util.List;

public class EventViewModel extends AndroidViewModel {

    private EventRepository eventRepository;
    private LiveData<List<Event>> eventList;

    public EventViewModel(@NonNull Application application) {
        super(application);

        eventRepository = new EventRepository(application);

        eventList = eventRepository.getEventList();
    }

    public void insert(Event event) {
        eventRepository.insert(event);
    }

    public void deleteEvent(int eventId) {
        eventRepository.delete(eventId);
    }

    public void update(Event event) {
        eventRepository.update(event);
    }

    public LiveData<List<Event>> getEventList() {
        return eventList;
    }
}
