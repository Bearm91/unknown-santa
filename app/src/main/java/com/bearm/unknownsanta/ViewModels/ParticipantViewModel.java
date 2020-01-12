package com.bearm.unknownsanta.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bearm.unknownsanta.Model.Participant;
import com.bearm.unknownsanta.Repositories.ParticipantRepository;

import java.util.List;

public class ParticipantViewModel extends AndroidViewModel {


    private ParticipantRepository participantRepository;

    public ParticipantViewModel(@NonNull Application application) {
        super(application);

        participantRepository = new ParticipantRepository(application);

    }

    public void insert(Participant participant) {
        participantRepository.insert(participant);
    }

    public void delete(Participant participant) {
        participantRepository.delete(participant);
    }


    public LiveData<List<Participant>> getParticipantList(int currentEventId) {
        return participantRepository.getParticipantList(currentEventId);
    }

    public List<Participant> getParticipants(int currentEventId) {
        return participantRepository.getParticipants (currentEventId);
    }
}
