package com.bearm.unknownsanta.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.bearm.unknownsanta.DAO.ParticipantDao;
import com.bearm.unknownsanta.Database.AppDatabase;
import com.bearm.unknownsanta.Model.Participant;

import java.util.List;

public class ParticipantRepository {

    private ParticipantDao participantDao;
    private Participant participant;
    private LiveData<List<Participant>> participantList;

    public ParticipantRepository(Application application) {

        AppDatabase db = AppDatabase.getInstance(application.getApplicationContext());

        participant = new Participant();
        participantDao = db.participantDao();

    }

    public LiveData<List<Participant>> getParticipantList(int eventId) {
        return participantDao.findByEventId(eventId);
    }


    public void insert(Participant participant) {
        new InsertParticipantAsyncTask(participantDao, participant).execute();
    }

    private class InsertParticipantAsyncTask extends AsyncTask<ParticipantDao, Participant, String> {

        InsertParticipantAsyncTask(ParticipantDao partDao, Participant participantoSave) {
            participantDao = partDao;
            participant = participantoSave;
        }

        @Override
        protected String doInBackground(ParticipantDao... participantDaos) {
            participantDao.insertParticipant(participant);
            return null;
        }
    }


    public void delete(Participant participant) {
        new DeleteParticipantAsyncTask(participantDao, participant).execute();
    }

    private class DeleteParticipantAsyncTask extends AsyncTask<ParticipantDao, Participant, String> {

        DeleteParticipantAsyncTask(ParticipantDao eDao, Participant part) {
            participantDao = eDao;
            participant = part;
        }

        @Override
        protected String doInBackground(ParticipantDao... eventDaos) {
            participantDao.delete(participant);
            return null;
        }
    }
}



