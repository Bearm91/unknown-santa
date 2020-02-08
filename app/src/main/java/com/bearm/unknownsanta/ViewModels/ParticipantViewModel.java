package com.bearm.unknownsanta.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.bearm.unknownsanta.Model.Participant;
import com.bearm.unknownsanta.Repositories.ParticipantRepository;

import java.util.List;

public class ParticipantViewModel extends AndroidViewModel {


    private ParticipantRepository participantRepository;
    private LiveData<List<Participant>> searchByLiveData;
    private MutableLiveData<String> filterLiveData = new MutableLiveData<String>();


    public ParticipantViewModel(@NonNull Application application) {
        super(application);

        participantRepository = new ParticipantRepository(application);
        searchByLiveData = Transformations.switchMap(filterLiveData,
                new Function<String, LiveData<List<Participant>>>() {
                    @Override
                    public LiveData<List<Participant>> apply(String v) {
                        return participantRepository.getParticipantList(Integer.parseInt(v));
                    }
                });
    }

    public void insert(Participant participant) {
        participantRepository.insert(participant);
    }

    public void delete(Participant participant) {
        participantRepository.delete(participant);
    }

    public void update(List<Participant> participants) {
        participantRepository.update(participants);
    }


    public LiveData<List<Participant>> getParticipantList() {
        return searchByLiveData;
    }

    public void setFilter(String filter) { filterLiveData.setValue(filter); }


}
