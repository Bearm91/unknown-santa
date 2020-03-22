package com.bearm.unknownsanta;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bearm.unknownsanta.Activities.AddParticipantActivity;
import com.bearm.unknownsanta.Activities.CreateEventActivity;
import com.bearm.unknownsanta.Helpers.SharedPreferencesHelper;
import com.bearm.unknownsanta.eMailSender.EmailCreator;
import com.bearm.unknownsanta.Activities.EventsActivity;
import com.bearm.unknownsanta.Activities.ParticipantShuffleActivity;
import com.bearm.unknownsanta.Adapters.ParticipantAdapter;
import com.bearm.unknownsanta.Model.Event;
import com.bearm.unknownsanta.ViewModels.EventViewModel;
import com.bearm.unknownsanta.Model.Participant;
import com.bearm.unknownsanta.ViewModels.ParticipantViewModel;
import com.bearm.unknownsanta.eMailSender.GMailSender;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CREATEVENT = 1;
    private static final int REQUEST_CODE_ADDPARTICIPANT = 2;
    private static final int REQUEST_CODE_SELECTEVENT = 3;

    Button btnNewEvent;
    Button btnSelectEvent;
    ImageView btnAddParticipant;
    LinearLayout lyNoEvent;
    LinearLayout lyEventInfo;
    LinearLayout lyParticipantInfo;
    TextView tvEventName;
    TextView tvEventPlace;
    TextView tvEventDate;
    TextView tvEventExpense;
    TextView tvEventTitle;
    ImageView ivEmail;
    FloatingActionButton fabsendEmail;

    RecyclerView recyclerView;
    ParticipantAdapter mParticipantAdapter;
    List<Participant> participantList;
    MutableLiveData<List<Participant>> liveDataParticipantList;

    ParticipantViewModel participantViewModel;
    EventViewModel eventViewModel;
    Observer<List<Participant>> participantObserver;
    SharedPreferencesHelper sharedPreferencesHelper;
    ParticipantShuffleActivity participantShuffleActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lyParticipantInfo = findViewById(R.id.layout_participant_data);
        lyEventInfo = findViewById(R.id.layout_event_data);
        lyNoEvent = findViewById(R.id.layout_no_event);

        tvEventTitle = findViewById(R.id.tv_event_title);
        tvEventName = findViewById(R.id.tv_event_name);
        tvEventPlace = findViewById(R.id.tv_event_place);
        tvEventDate = findViewById(R.id.tv_event_date);
        tvEventExpense = findViewById(R.id.tv_event_money);
        ivEmail = findViewById(R.id.iv_email);

        btnNewEvent = findViewById(R.id.btn_new_event);
        //Opens CreateEventActivity activity
        btnNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("New event", "CREATE");
                Intent eventForm;
                eventForm = new Intent(v.getContext(), CreateEventActivity.class);
                startActivityForResult(eventForm, REQUEST_CODE_CREATEVENT);
            }
        });

        btnSelectEvent = findViewById(R.id.btn_select_event);
        //Opens EventsActivity activity
        btnSelectEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectEvent(v);
            }
        });

        btnAddParticipant = findViewById(R.id.btn_add);
        //Opens AddParticipantActivity activity
        btnAddParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("New Participant", "ADD");
                Intent participantForm = new Intent(v.getContext(), AddParticipantActivity.class);
                startActivityForResult(participantForm, REQUEST_CODE_ADDPARTICIPANT);
            }
        });

        fabsendEmail = findViewById(R.id.fab);
        fabsendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(sharedPreferencesHelper.getCurrentEventId()) > 0) {
                    if (!sharedPreferencesHelper.getCurrentEvent().isEmailSent()) {
                        if (participantList.size() > 2) {
                            if (sharedPreferencesHelper.getCurrentEventAssignationStatus()) {
                                sendEmails();
                            } else {
                                Toast.makeText(getApplicationContext(), "The assignation has not been made yet.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "There are not enough participants in this event.", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "The emails for this event have already been sent.", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });

        //SharedPreferences Helper init
        sharedPreferencesHelper = new SharedPreferencesHelper(this);

        //ParticipantAdapter init
        participantList = new ArrayList<>();
        liveDataParticipantList = new MutableLiveData<>();

        recyclerView = findViewById(R.id.rv_participants_list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //ViewModels init
        ViewModelProvider.AndroidViewModelFactory myViewModelProviderFactory = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        participantViewModel = new ViewModelProvider(this, myViewModelProviderFactory).get(ParticipantViewModel.class);

        mParticipantAdapter = new ParticipantAdapter(participantList, participantViewModel);
        recyclerView.setAdapter(mParticipantAdapter);

        participantObserver = new Observer<List<Participant>>() {
            @Override
            public void onChanged(List<Participant> participants) {
                if (participants != null) {
                    Log.e("Select participant size", String.valueOf(participants.size()));
                    mParticipantAdapter.setParticipants(participants);
                    participantList = participants;
                }
            }
        };

        participantViewModel.getParticipantList().observe(this, participantObserver);
        participantViewModel.setFilter(sharedPreferencesHelper.getCurrentEventId());
        eventViewModel = new ViewModelProvider(this, myViewModelProviderFactory).get(EventViewModel.class);

        participantShuffleActivity = new ParticipantShuffleActivity(participantViewModel, getApplicationContext());

        //Checks selected event info
        loadEventInfo();
    }

    public HashMap<String, String> getParticipantReceiverMap(){
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
                e.printStackTrace();
            }
        }
        return participantMap;
    }

    private HashMap<String, String> getParticipantEmailMessageMap(HashMap<String, String> participantMap){
        final HashMap<String, String> emailData = new HashMap<>();
        String emailMessage;

        for (Map.Entry<String, String> entry : participantMap.entrySet()) {
            String participantEmail = entry.getKey();
            String presentReceiverName = entry.getValue();
            Log.e("PARTICIPANT_MAP", "key= " + participantEmail + "; value= " + presentReceiverName);

            try {
                EmailCreator emailCreator = new EmailCreator(sharedPreferencesHelper.getCurrentEvent(), participantList);
                emailCreator.createEmailBody(presentReceiverName);
                emailMessage = emailCreator.getEmailBody();
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

    private void sendEmails() {

        HashMap<String, String> emailData = new HashMap<>();

        //Match Participant email with Receiver Name
        HashMap<String, String> participantMap = getParticipantReceiverMap();

        //Match Participant email with email message
        if (participantMap.size() > 0) {
           emailData = getParticipantEmailMessageMap(participantMap);
        }

        if (!emailData.isEmpty()) {
            new sendEmailsAsyncTask(emailData).execute();
         }
    }

    public class sendEmailsAsyncTask extends AsyncTask<HashMap<String, String>, String, Boolean>{

        HashMap<String, String> information;

        public sendEmailsAsyncTask(HashMap<String, String> emailData) {
            this.information = emailData;
        }

        // Before the tasks execution
        protected void onPreExecute(){
            //mTextView.setText(mTextView.getText() + "\nStarting task....");
        }

        @Override
        protected Boolean doInBackground(HashMap<String, String>... hashMaps) {
            boolean isSent;
            try {
                GMailSender mailSender = new GMailSender(getString(R.string.account_email), getString(R.string.account_password));
                //mailSender.sendMail(emailData);
                isSent = true;
            } catch (Exception e) {
                isSent = false;
                Log.e("SendMail", e.getMessage(), e);


            }
            return isSent;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            sharedPreferencesHelper.updateCurrentEventEmailStatus(result);
            updateEmailStatus(result, true);
            updateDBEmailStatus(result);
            Log.e("SendMail", String.valueOf(result));

        }
    }

    private void updateEmailStatus(boolean success, boolean inform) {
        if (success) {
            ivEmail.setVisibility(View.VISIBLE);
            if(inform){
                Toast.makeText(getApplicationContext(), "An email has been sent to all participants.", Toast.LENGTH_LONG).show();
            }
        } else {
            ivEmail.setVisibility(View.INVISIBLE);
            if(inform){
                Toast.makeText(getApplicationContext(), "The email could not be sent. Please, try again", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void openSelectEvent(View v) {
        Log.e("Select event", "SELECT");
        Intent eventSelection;
        if (v != null) {
            eventSelection = new Intent(v.getContext(), EventsActivity.class);
        } else {
            eventSelection = new Intent(getApplicationContext(), EventsActivity.class);
        }
        startActivityForResult(eventSelection, REQUEST_CODE_SELECTEVENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        super.onActivityResult(requestCode, resultCode, data);

        //CREATE EVENT IN DATABASE
        if (requestCode == REQUEST_CODE_CREATEVENT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Event newEvent = new Event(data.getStringExtra("name"),
                        data.getStringExtra("place"),
                        data.getStringExtra("date"),
                        data.getStringExtra("expense"),
                        false,
                        false);

                eventViewModel.insert(newEvent);

                Toast.makeText(getApplicationContext(), data.getStringExtra("name") + " was created correctly.", Toast.LENGTH_LONG).show();
            }
        }

        //ADD PARTICIPANT TO DATABASE
        if (requestCode == REQUEST_CODE_ADDPARTICIPANT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Participant newParticipant = new Participant(
                        data.getStringExtra("name"),
                        data.getStringExtra("email"),
                        data.getStringExtra("avatar"),
                        Integer.parseInt(sharedPreferencesHelper.getCurrentEventId()));

                participantViewModel.insert(newParticipant);
                sharedPreferencesHelper.updateCurrentEventAssignationStatus(false);
                Toast.makeText(getApplicationContext(), "Yay! A new participant joined the event!", Toast.LENGTH_LONG).show();
            }
        }

        //SELECT EVENT AND SHOW ITS INFO IN HOME SCREEN
        if (requestCode == REQUEST_CODE_SELECTEVENT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                loadEventInfo();
                participantViewModel.setFilter(sharedPreferencesHelper.getCurrentEventId());

            }
        }
    }

    //Adds info about the selected event to Home screen
    public void loadEventInfo() {
        //Checks if there is an event selected (0 = no event)
        if (Integer.parseInt(sharedPreferencesHelper.getCurrentEventId()) > 0) {
            tvEventName.setText(sharedPreferencesHelper.getCurrentEventName());
            tvEventPlace.setText(sharedPreferencesHelper.getCurrentEventPlace());
            tvEventDate.setText(sharedPreferencesHelper.getCurrentEventDate());
            tvEventExpense.setText(sharedPreferencesHelper.getCurrentEventExpense());

            //Hide and display elements in the layout when an event is selected
            lyNoEvent.setVisibility(View.GONE);
            lyEventInfo.setVisibility(View.VISIBLE);
            lyParticipantInfo.setVisibility(View.VISIBLE);
            tvEventTitle.setVisibility(View.GONE);
            fabsendEmail.setVisibility(View.VISIBLE);

            updateEmailStatus(sharedPreferencesHelper.getCurrentEventEmailStatus(), false);
        } else {
            //Hide and display elements in the layout when no event is selected
            lyNoEvent.setVisibility(View.VISIBLE);
            lyEventInfo.setVisibility(View.GONE);
            lyParticipantInfo.setVisibility(View.INVISIBLE);
            tvEventTitle.setVisibility(View.VISIBLE);
            fabsendEmail.setVisibility(View.INVISIBLE);
        }

    }

    //Deletes info about selected event from SharedPreferences so there is no event marked as selected
    public void closeCurrentEvent() {
        sharedPreferencesHelper.clearCurrentEvent();
        participantList.clear();
        mParticipantAdapter.setParticipants(participantList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Deletes selected event from database
        if (id == R.id.action_delete_event) {
            if (sharedPreferencesHelper.getCurrentEventId() != null) {
                eventViewModel.deleteEvent(Integer.parseInt(sharedPreferencesHelper.getCurrentEventId()));
                closeCurrentEvent();
                loadEventInfo();
            }
            return true;
        }

        //Closes selected event
        if (id == R.id.action_close_event) {
            closeCurrentEvent();
            loadEventInfo();
            return true;
        }

        //Assigns secret santas to the participants of the event
        if (id == R.id.action_shuffle_participants) {
            if (participantList.size() > 2) {
                participantShuffleActivity.setParticipants(participantList);
                participantShuffleActivity.shuffleList();
                participantShuffleActivity.assignGivers();
                sharedPreferencesHelper.updateCurrentEventAssignationStatus(true);
                updateDBAssignationStatus(true);
            } else {
                Toast.makeText(getApplicationContext(), "You need more than 2 participants.", Toast.LENGTH_LONG).show();

            }
            return true;
        }

        //TODO Displays info about the app
        if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Updates Event object in database with Email status (sent or not sent)
    public void updateDBEmailStatus(boolean isSent){
        Event currentE = sharedPreferencesHelper.getCurrentEvent();
        ViewModelProvider.AndroidViewModelFactory myViewModelProviderFactory = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        eventViewModel = new ViewModelProvider(this, myViewModelProviderFactory).get(EventViewModel.class);
        currentE.isEmailSent(isSent);
        eventViewModel.update(currentE);
    }

    //Updates Event object in database with Assignation status (done or not)
    public void updateDBAssignationStatus(boolean isDone){
        Event currentE = sharedPreferencesHelper.getCurrentEvent();
        ViewModelProvider.AndroidViewModelFactory myViewModelProviderFactory = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        eventViewModel = new ViewModelProvider(this, myViewModelProviderFactory).get(EventViewModel.class);
        currentE.setAssignationDone(isDone);
        eventViewModel.update(currentE);
    }
}
