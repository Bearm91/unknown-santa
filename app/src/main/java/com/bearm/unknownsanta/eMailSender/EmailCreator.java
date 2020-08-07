package com.bearm.unknownsanta.eMailSender;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.bearm.unknownsanta.R;
import com.bearm.unknownsanta.model.Event;
import com.bearm.unknownsanta.model.Participant;

import java.util.List;

public class EmailCreator extends AppCompatActivity {

    String emailBody;
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
        emailBody = emailBody.concat(getReceiverInfo(receiver));
        Log.i("EMAIL_BODY", emailBody);
    }

    public String getReceiverInfo(String receiver){
        return getString(R.string.email_body_receiver, receiver);
    }

    public String getEventInfo(){
        return getString(R.string.email_body_event_name,
                myEvent.getName(),
                myEvent.getDate(),
                myEvent.getPlace(),
                myEvent.getExpense());
    }

}
