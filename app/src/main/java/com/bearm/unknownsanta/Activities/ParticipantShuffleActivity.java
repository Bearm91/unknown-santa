package com.bearm.unknownsanta.Activities;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.bearm.unknownsanta.Model.Participant;

import java.util.Collections;
import java.util.List;

public class ParticipantShuffleActivity extends AppCompatActivity {
    List<Participant> participants;
    List<Participant> originalList;

    public ParticipantShuffleActivity(List<Participant> participantsList) {
        this.participants = participantsList;
        this.originalList = participantsList;
    }

    public void shuffleList(){
        Log.e("ORIGINAL_LIST","List before shuffle: "+participants.toString());
        Collections.shuffle(this.participants);
        Log.e("FINAL_LIST","List after shuffle: "+participants.toString());

    }

    public void assignGivers(){
        boolean ok = false;
        while (!ok) {
            for (int i = 0; i < participants.size(); i++) {
                Log.e("ORIGINAL_LIST", String.valueOf(originalList.get(i).getId()));
                Log.e("FINAL_LIST", String.valueOf(this.participants.get(i).getId()));

                if (originalList.get(i).getId() != participants.get(i).getId()) {
                    originalList.get(i).setIdReceiver(participants.get(i).getId());
                }
            }

            if(originalList.get(participants.size()-1).getIdReceiver() > 0){
                ok = true;
                printShuffledList();
            } else {
                shuffleList();
            }
        }

    }

    public void printShuffledList() {
        for (int i = 0; i <= participants.size(); i++) {
            Log.e("FINAL_LIST", String.valueOf(participants.get(i).getId()));
            Log.e("FINAL_LIST", String.valueOf(participants.get(i).getName()));
        }
    }

    public void setParticipants(List<Participant> participantsList){
        this.participants = participantsList;
        this.originalList = participantsList;
    }
}
