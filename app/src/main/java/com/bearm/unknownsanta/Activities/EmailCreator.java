package com.bearm.unknownsanta.Activities;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bearm.unknownsanta.Model.Event;
import com.bearm.unknownsanta.Model.Participant;

import java.util.ArrayList;
import java.util.List;

public class EmailCreator extends AppCompatActivity {

    String emailBody;
    String eventInfo;
    String recepient;
    String presentReceiver;
    List<Participant> myParticipants;
    Event myEvent;

    public EmailCreator(Event event, List<Participant> participants) {
        this.myParticipants = participants;
        this.myEvent = event;
    }


    public String getEmailBody(){
        if(!emailBody.isEmpty()){
            return emailBody;
        } else {
            return null;
        }
    }

    public void createEmailBody(String receiver){
        emailBody = getEventInfo();
        emailBody = emailBody.concat("Your mission is getting something nice for: " + receiver);
    }


    public String getEventInfo(){
        eventInfo = "You have been invited to participate in a Secret Santa game for "+ myEvent.getName() + " event";
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

}
