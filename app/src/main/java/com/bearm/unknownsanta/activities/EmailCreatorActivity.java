package com.bearm.unknownsanta.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bearm.unknownsanta.R;
import com.bearm.unknownsanta.model.Event;
import com.bearm.unknownsanta.model.Participant;
import com.bearm.unknownsanta.viewModels.ParticipantViewModel;

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
    Context context;

    public EmailCreatorActivity(Context context, Event event, List<Participant> participants) {
        this.myParticipants = participants;
        this.myEvent = event;
        this.context = context;
    }


    public String getEmailBody() {
        if (!emailBody.isEmpty()) {
            return emailBody;
        } else {
            return null;
        }
    }

    //Creates the body of the email with the information about the participants and the event
    public void createEmailBody(String receiver) {
        emailBody = "";
        emailBody = getEventInfo();
        emailBody = emailBody.concat(context.getString(R.string.email_body_receiver, receiver));
        //emailBody = emailBody.concat(context.getString(R.string.email_footer));
    }

    //Adds the information of the event to the body of the email
    public String getEventInfo() {
        try {
            eventInfo = context.getString(R.string.email_body_event_name, myEvent.getName(), myEvent.getDate(), myEvent.getExpense());
        } catch(Exception npe){
            Log.e("ERROR", npe.getMessage());
        }
        //Log.e("EMAILBODY", eventInfo);
        //TODO return styled message
        //Spanned eventInfoStyled = Html.fromHtml(eventInfo);
        return eventInfo;
    }

    //Links email body with receiver
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
                Toast.makeText(getApplicationContext(), "Sorry, there are still participants with no assignation.", Toast.LENGTH_LONG).show();
            } catch (ArrayIndexOutOfBoundsException aioob) {
                //TODO change toast for alert dialog
                Toast.makeText(getApplicationContext(), "Sorry, there are still participants with no assignation. Check that all participants have the green mark next to their names, and press on 'Assign Santas' in the menu below if anyone doesn't.", Toast.LENGTH_LONG).show();
            }
        }

        return emailData;
    }
    //Opens a dialog with information and instructions for the error that occurs when there are participants with no assignation.
    private void showError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getApplicationContext());
        builder.setTitle(R.string.error_email_title)
                .setMessage(R.string.error_email_message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

}
