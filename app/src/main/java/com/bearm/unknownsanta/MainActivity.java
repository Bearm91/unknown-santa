package com.bearm.unknownsanta;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.bearm.unknownsanta.Activities.EmailCreatorActivity;
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

    private static final int REQUEST_CODE_ADDPARTICIPANT = 2;

    ImageView btnAddParticipant;
    LinearLayout lyEventInfo;
    LinearLayout lyParticipantInfo;
    TextView tvEventName;
    TextView tvEventPlace;
    TextView tvEventDate;
    TextView tvEventExpense;
    ImageView ivEmail;
    ImageView ivEventIcon;

    FloatingActionButton fabEditEvent;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        lyParticipantInfo = findViewById(R.id.layout_participant_data);
        lyEventInfo = findViewById(R.id.layout_event_data);

        tvEventName = findViewById(R.id.tv_event_name);
        tvEventPlace = findViewById(R.id.tv_event_place);
        tvEventDate = findViewById(R.id.tv_event_date);
        tvEventExpense = findViewById(R.id.tv_event_money);
        ivEmail = findViewById(R.id.iv_email);
        ivEventIcon = findViewById(R.id.iv_event_icon);

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

        /*fabEditEvent = findViewById(R.id.fab);
        fabEditEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }

        });*/

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

        mParticipantAdapter = new ParticipantAdapter(participantList, participantViewModel,this);
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
        participantViewModel.setFilter(SharedPreferencesHelper.getCurrentEventId());
        eventViewModel = new ViewModelProvider(this, myViewModelProviderFactory).get(EventViewModel.class);

        participantShuffleActivity = new ParticipantShuffleActivity(participantViewModel, getApplicationContext());


        //Checks selected event info
        loadEventInfo();
    }


    private void sendEmails() {
        EmailCreatorActivity ecreator = new EmailCreatorActivity(SharedPreferencesHelper.getCurrentEvent(), participantList);
        ecreator.setParticipantViewModel(participantViewModel);

        HashMap<String, String> emailData = new HashMap<>();
        emailData = ecreator.createEmailContent();


        if (!emailData.isEmpty()) {
            new sendEmailsAsyncTask(emailData).execute();
        }
    }

    public class sendEmailsAsyncTask extends AsyncTask<HashMap<String, String>, String, Boolean> {

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
            SharedPreferencesHelper.updateCurrentEventEmailStatus(result);
            updateEmailStatus(result, true);
            updateDBEmailStatus(result);
            Log.e("SendMail", String.valueOf(result));

        }
    }

    private void updateEmailStatus(boolean success, boolean inform) {
        if (success) {
            ivEmail.setVisibility(View.VISIBLE);
            SharedPreferencesHelper.updateCurrentEventEmailStatus(true);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        super.onActivityResult(requestCode, resultCode, data);

        //ADD PARTICIPANT TO DATABASE
        if (requestCode == REQUEST_CODE_ADDPARTICIPANT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Participant newParticipant = new Participant(
                        data.getStringExtra("name"),
                        data.getStringExtra("email"),
                        data.getStringExtra("avatar"),
                        Integer.parseInt(SharedPreferencesHelper.getCurrentEventId()));

                participantViewModel.insert(newParticipant);
                SharedPreferencesHelper.updateCurrentEventAssignationStatus(false);
                Toast.makeText(getApplicationContext(), "Yay! A new participant joined the event!", Toast.LENGTH_LONG).show();
            }
        }

    }

    //Adds info about the selected event to Home screen
    public void loadEventInfo() {
        //Checks if there is an event selected (0 = no event)
        if (Integer.parseInt(SharedPreferencesHelper.getCurrentEventId()) > 0) {
            tvEventName.setText(SharedPreferencesHelper.getCurrentEventName());
            tvEventPlace.setText(SharedPreferencesHelper.getCurrentEventPlace());
            tvEventDate.setText(SharedPreferencesHelper.getCurrentEventDate());
            tvEventExpense.setText(SharedPreferencesHelper.getCurrentEventExpense());
            updateEmailStatus(SharedPreferencesHelper.getCurrentEventEmailStatus(), false);

            String iconName = SharedPreferencesHelper.getCurrentEventIconName();
            Log.e("ICON_NAME", iconName);
            if (!iconName.equals("")) {
                int resourceIdImage = this.getResources().getIdentifier(iconName, "drawable",
                        this.getPackageName());
                ivEventIcon.setImageResource(resourceIdImage);
            } else {
                ivEventIcon.setImageResource(R.drawable.ic_christmas_tree);
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Assigns secret santas to the participants of the event
        if (id == R.id.action_shuffle_participants) {
            shuffleParticipants();
            return true;
        }
    private void shuffleParticipants() {
        if (participantList.size() > 2) {
            participantShuffleActivity.setParticipants(participantList);
            participantShuffleActivity.shuffleList();
            participantShuffleActivity.assignGivers();
            SharedPreferencesHelper.updateCurrentEventAssignationStatus(true);
            updateDBAssignationStatus(true);
            if (SharedPreferencesHelper.getCurrentEventEmailStatus()) {
                SharedPreferencesHelper.updateCurrentEventEmailStatus(false);
                updateDBEmailStatus(false);
            }
        } else {
            Toast.makeText(getApplicationContext(), "You need more than 2 participants.", Toast.LENGTH_LONG).show();
        }
    }

    //Updates Event object in database with Email status (sent or not sent)
    public void updateDBEmailStatus(boolean isSent){
        Event currentE = SharedPreferencesHelper.getCurrentEvent();
        ViewModelProvider.AndroidViewModelFactory myViewModelProviderFactory = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        eventViewModel = new ViewModelProvider(this, myViewModelProviderFactory).get(EventViewModel.class);
        currentE.isEmailSent(isSent);
        eventViewModel.update(currentE);
    }

    //Updates Event object in database with Assignation status (done or not)
    public void updateDBAssignationStatus(boolean isDone){
        Event currentE = SharedPreferencesHelper.getCurrentEvent();
        ViewModelProvider.AndroidViewModelFactory myViewModelProviderFactory = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        eventViewModel = new ViewModelProvider(this, myViewModelProviderFactory).get(EventViewModel.class);
        currentE.setAssignationDone(isDone);
        eventViewModel.update(currentE);
    }
}
