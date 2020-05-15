package com.bearm.unknownsanta.Activities;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bearm.unknownsanta.Model.Event;
import com.bearm.unknownsanta.Model.Participant;
import com.bearm.unknownsanta.ViewModels.ParticipantViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class EmailCreatorActivity extends AppCompatActivity {

    String emailBody;
    String eventInfo;
    List<Participant> myParticipants;
    Event myEvent;
    ParticipantViewModel participantViewModel;

    public EmailCreatorActivity(Event event, List<Participant> participants) {
        this.myParticipants = participants;
        this.myEvent = event;
    }


    public String getEmailBody() {
        if (!emailBody.isEmpty()) {
            return emailBody;
        } else {
            return null;
        }
    }

    public void createEmailBody(String receiver) {
        emailBody = getEventInfo();
        emailBody = emailBody.concat("Your mission is getting something nice for: " + receiver);
    }


    public String getEventInfo() {
        eventInfo = "You have been invited to participate in a Secret Santa game for " + myEvent.getName() + " event";
        //if (!myEvent.getDate().isEmpty()){
        eventInfo = eventInfo.concat(", which will take place next " + myEvent.getDate());
        //}
        //if (!myEvent.getPlace().isEmpty()) {
        eventInfo = eventInfo.concat(" at " + myEvent.getPlace() + ". ");
        //}
        //if (!myEvent.getExpense().isEmpty()){
        eventInfo = eventInfo.concat("Remember that the expense limit is: " + myEvent.getExpense() + ". ");
        //}

        Log.e("EMAILBODY", eventInfo);


        return eventInfo;
    }

    public HashMap<String, String> createEmailContent() {

        HashMap<String, String> emailData = new HashMap<>();

        //Match Participant email with Receiver Name
        HashMap<String, String> participantMap = getParticipantReceiverMap(myParticipants);

        //Match Participant email with email message
        if (participantMap.size() > 0) {
            emailData = getParticipantEmailMessageMap(participantMap, myParticipants);
        }

        return emailData;
    }

    public void setParticipantViewModel(ParticipantViewModel participantViewModel) {
        this.participantViewModel = participantViewModel;
    }

    public HashMap<String, String> getParticipantReceiverMap(List<Participant> participantList) {
        Participant currentParticipant;
        int currentPReceiverId;
        String currentPReceiverName;
        String currentPEmail;
        HashMap<String, String> participantMap = new HashMap<>();

        for (int i = 0; i < participantList.size(); i++) {
            currentParticipant = participantList.get(i);
            currentPEmail = currentParticipant.getEmail();
            currentPReceiverId = currentParticipant.getIdReceiver();
            try {
                currentPReceiverName = participantViewModel.getReceiverName(currentPReceiverId);
                participantMap.put(currentPEmail, currentPReceiverName);
            } catch (ExecutionException | InterruptedException e) {
                Log.e("EXECUTION_EXCEPTION", e.getMessage());
            }
        }
        return participantMap;
    }

    private HashMap<String, String> getParticipantEmailMessageMap(HashMap<String, String> participantMap, List<Participant> participantList) {
        final HashMap<String, String> emailData = new HashMap<>();
        String emailMessage;

        for (Map.Entry<String, String> entry : participantMap.entrySet()) {
            String participantEmail = entry.getKey();
            String presentReceiverName = entry.getValue();
            Log.e("PARTICIPANT_MAP", "key= " + participantEmail + "; value= " + presentReceiverName);

            try {
                createEmailBody(presentReceiverName);
                emailMessage = getEmailBody();
                Log.e("MESSAGE", emailMessage);

                emailData.put(participantEmail, emailMessage);
            } catch (NullPointerException npe) {
                Toast.makeText(getApplicationContext(), "1Sorry, there are still participants with no assignation.", Toast.LENGTH_LONG).show();
            } catch (ArrayIndexOutOfBoundsException aioob) {
                //TODO change toast for alert dialog
                Toast.makeText(getApplicationContext(), "2Sorry, there are still participants with no assignation. Check that all participants have the green mark next to their names, and press on 'Assign Santas' in the menu below if anyone doesn't.", Toast.LENGTH_LONG).show();
            }
        }
        return emailData;
    }

}
