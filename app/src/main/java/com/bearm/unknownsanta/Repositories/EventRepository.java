package com.bearm.unknownsanta.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.bearm.unknownsanta.DAO.EventDao;
import com.bearm.unknownsanta.Database.AppDatabase;
import com.bearm.unknownsanta.Model.Event;

import java.util.List;

public class EventRepository {

    private EventDao eventDao;
    private Event event;
    private LiveData<List<Event>> eventList;
    private int eventId;

    public EventRepository(Application application) {

        AppDatabase db = AppDatabase.getInstance(application.getApplicationContext());

        eventDao = db.eventDao();
        eventList = eventDao.getEventList();


    }

    public LiveData<List<Event>> getEventList() {
        return eventList;
    }

    public void insert(Event event) {
        new InsertEventAsyncTask(eventDao, event).execute();
    }

    private class InsertEventAsyncTask extends AsyncTask<EventDao, Event, String> {

        InsertEventAsyncTask(EventDao eDao, Event eventoSave) {
            eventDao = eDao;
            event = eventoSave;
        }

        @Override
        protected String doInBackground(EventDao... eventDaos) {
            eventDao.insertEvent(event);
            return null;
        }
    }

    public void delete(int eventId) {
        new DeleteEventAsyncTask(eventDao, eventId).execute();
    }

    private class DeleteEventAsyncTask extends AsyncTask<EventDao, String, String> {

        DeleteEventAsyncTask(EventDao eDao, int id) {
            eventDao = eDao;
            eventId = id;
        }

        @Override
        protected String doInBackground(EventDao... eventDaos) {
            eventDao.delete(eventId);
            return null;
        }
    }
}


