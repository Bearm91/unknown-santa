package com.bearm.unknownsanta.Activities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bearm.unknownsanta.Model.Participant;
import com.bearm.unknownsanta.ViewModels.ParticipantViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParticipantShuffleActivity extends AppCompatActivity {
    List<Participant> participants;
    List<Participant> originalList;
    ParticipantViewModel participantViewModel;
    Context context;


    public ParticipantShuffleActivity(ParticipantViewModel participantViewModel, Context context) {
        this.participantViewModel = participantViewModel;
        this.context = context;
    }

    public void shuffleList() {
        int loops = 0;
        Log.e("ORIGINAL_LIST", "List before shuffle: " + participants.toString());
        Collections.shuffle(participants);

        for (int i = 0; i < participants.size(); i++) {
            if (originalList.get(i).getId() == participants.get(i).getId()) {
                Collections.shuffle(participants);
                i = 0;
                loops++;
            }
        }
        Log.e("FINAL_LIST", "List after shuffle: " + participants.toString());
        Log.e("LOOPS_NEEDED=", String.valueOf(loops));

    }

    public void assignGivers() {
        for (int i = 0; i < participants.size(); i++) {
            originalList.get(i).setIdReceiver(participants.get(i).getId());
            Log.e("Participant", String.valueOf(originalList.get(i).getId()));
            Log.e("gets this receiver", String.valueOf(participants.get(i).getId()));
        }
        participantViewModel.update(originalList);

        Toast.makeText(context, "Yay! The association was completed successfully!", Toast.LENGTH_LONG).show();

    }


    public void printShuffledList() {
        for (int i = 0; i <= participants.size(); i++) {
            Log.e("FINAL_LIST", String.valueOf(participants.get(i).getId()));
            Log.e("FINAL_LIST", String.valueOf(participants.get(i).getName()));
        }
    }

    public void setParticipants(List<Participant> participantsList) {
        participants = participantsList;
        originalList = new ArrayList<>(participantsList);
    }
}
