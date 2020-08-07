package com.bearm.unknownsanta.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.bearm.unknownsanta.DAO.EventDao;
import com.bearm.unknownsanta.database.AppDatabase;
import com.bearm.unknownsanta.model.Event;

import java.util.List;

public class EventRepository {

    private EventDao eventDao;
    private Event event;
    private LiveData<List<Event>> eventList;
    private int eventId;

    public EventRepository(Application application) {

        AppDatabase db = AppDatabase.getInstance(application.getApplicationContext());

        eventDao = db.eventDao();
        eventList = eventDao.findAll();

    }

    public LiveData<List<Event>> getEventList() {
        return eventList;
    }

    public void insert(Event event) {
        new InsertEventAsyncTask(eventDao, event).execute();
    }

    public void deleteAll() {
        new DeleteEventAsyncTask(eventDao, 0).execute();
    }

    private class InsertEventAsyncTask extends AsyncTask<EventDao, Event, String> {

        InsertEventAsyncTask(EventDao eDao, Event eventSave) {
            eventDao = eDao;
            event = eventSave;
        }

        @Override
        protected String doInBackground(EventDao... eventDaos) {
            eventDao.insert(event);
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
            if(eventId == 0){
                eventDao.deleteAll();
            } else {
                eventDao.delete(eventId);
            }
           return null;
        }
    }

    public void update(Event event) {
        new UpdateEventAsyncTask(eventDao, event).execute();
    }

    private class UpdateEventAsyncTask extends AsyncTask<EventDao, Event, String> {

        UpdateEventAsyncTask(EventDao eDao, Event e) {
            eventDao = eDao;
            event = e;
        }

        @Override
        protected String doInBackground(EventDao... eventDaos) {
            eventDao.update(event);
            //Log.e("UPDATE RESULT", String.valueOf(result));
            return null;
        }
    }
}


