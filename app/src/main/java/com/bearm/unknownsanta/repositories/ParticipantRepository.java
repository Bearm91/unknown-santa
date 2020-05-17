package com.bearm.unknownsanta.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.bearm.unknownsanta.DAO.ParticipantDao;
import com.bearm.unknownsanta.database.AppDatabase;
import com.bearm.unknownsanta.model.Participant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ParticipantRepository {

    private ParticipantDao participantDao;
    private Participant participant;
    private List<Participant> participantList;
    private String participantName;
    private String name;
    private int id;

    public ParticipantRepository(Application application) {

        AppDatabase db = AppDatabase.getInstance(application.getApplicationContext());

        participant = new Participant();
        participantList = new ArrayList<>();
        participantDao = db.participantDao();


    }

    public LiveData<List<Participant>> getParticipantList(int eventId) {
        return participantDao.findByEventId(eventId);
    }

    /**
     * Gets the name of the participant with its id
     * @param participantId Id of the participant whose name will be queried.
     */
    public String getReceiverName(Integer participantId) throws ExecutionException, InterruptedException {
        return new getNameAsyncTask(participantDao, participantId) {
            @Override
            protected String doInBackground(ParticipantDao... participantDaos) {
                String nameById = participantDao.findNameById(id);
                return nameById;
            }
        }.execute().get();
    }

    private abstract class getNameAsyncTask extends AsyncTask<ParticipantDao, Integer, String> {
        getNameAsyncTask(ParticipantDao partDao, Integer partId) {
            participantDao = partDao;
            id = partId;
        }
    }


    /**********************************************************************************/

    /**
     * Insert method for participant table in database
     * @param participant Participant Object that will be inserted in db
     */
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

    /**
     * Delete method for participant table in database
     * @param participant Participant Object that will be deleted from db
     */
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

    /**
     * Update method for participant table in database
     * @param participants Participant Objects that will be updated in db
     */
    public void update(List<Participant> participants) {
        new UpdateParticipantAsyncTask(participantDao, participants).execute();
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



