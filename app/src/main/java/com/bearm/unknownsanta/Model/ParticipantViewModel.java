package com.bearm.unknownsanta.Model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bearm.unknownsanta.Repositories.ParticipantRepository;

import java.util.List;

public class ParticipantViewModel extends AndroidViewModel {


    private ParticipantRepository participantRepository;
    private LiveData<List<Participant>> participantList;

    public ParticipantViewModel(@NonNull Application application) {
        super(application);

        participantRepository = new ParticipantRepository(application);
        participantList = participantRepository.getParticipantList();

    }

    public void insert(Participant participant) {
        participantRepository.insert(participant);
    }

    public void delete(Participant participant) {
        participantRepository.delete(participant);
    }

    public LiveData<List<Participant>> getParticipantList() {
        return participantList;
    }
}
