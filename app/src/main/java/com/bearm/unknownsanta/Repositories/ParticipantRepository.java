package com.bearm.unknownsanta.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.bearm.unknownsanta.DAO.ParticipantDao;
import com.bearm.unknownsanta.Database.AppDatabase;
import com.bearm.unknownsanta.Model.Participant;

import java.util.ArrayList;
import java.util.List;

public class ParticipantRepository {

    private ParticipantDao participantDao;
    private Participant participant;
    private List<Participant> participantList;
    private Integer id;

    public ParticipantRepository(Application application) {

        AppDatabase db = AppDatabase.getInstance(application.getApplicationContext());

        participant = new Participant();
        participantList = new ArrayList<>();
        participantDao = db.participantDao();
    }

    public LiveData<List<Participant>> getParticipantList(int eventId) {
        return participantDao.findByEventId(eventId);
    }

    public void insert(Participant participant) {
        new InsertParticipantAsyncTask(participantDao, participant).execute();
    }

    private class InsertParticipantAsyncTask extends AsyncTask<ParticipantDao, Participant, String> {

        InsertParticipantAsyncTask(ParticipantDao partDao, Participant participantSave) {
            participantDao = partDao;
            participant = participantSave;
        }

        @Override
        protected String doInBackground(ParticipantDao... participantDaos) {
            participantDao.insert(participant);
            return null;
        }
    }


    public void delete(Participant participant) {
        new DeleteParticipantAsyncTask(participantDao, participant).execute();
    }

    private class DeleteParticipantAsyncTask extends AsyncTask<ParticipantDao, Participant, String> {

        DeleteParticipantAsyncTask(ParticipantDao pDao, Participant part) {
            participantDao = pDao;
            participant = part;
        }

        @Override
        protected String doInBackground(ParticipantDao... participantDaos) {
            participantDao.delete(participant);
            return null;
        }
    }

    public void update(List<Participant> participant) {
        new UpdateParticipantAsyncTask(participantDao, participant).execute();
    }

    private class UpdateParticipantAsyncTask extends AsyncTask<ParticipantDao, List<Participant>, String> {
        UpdateParticipantAsyncTask(ParticipantDao pDao, List<Participant> pList) {
            participantDao = pDao;
            participantList = pList;
        }

        @Override
        protected String doInBackground(ParticipantDao... participantDaos) {
            participantDao.update(participantList);
            return null;
        }
    }
}



