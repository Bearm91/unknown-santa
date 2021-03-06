package com.bearm.unknownsanta;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bearm.unknownsanta.activities.AddParticipantActivity;
import com.bearm.unknownsanta.activities.EmailCreatorActivity;
import com.bearm.unknownsanta.asyncTasks.SendEmailAsyncTask;
import com.bearm.unknownsanta.helpers.SharedPreferencesHelper;
import com.bearm.unknownsanta.databinding.ActivityMainBinding;
import com.bearm.unknownsanta.activities.ParticipantShuffleActivity;
import com.bearm.unknownsanta.adapters.ParticipantAdapter;
import com.bearm.unknownsanta.model.Event;
import com.bearm.unknownsanta.viewModels.EventViewModel;
import com.bearm.unknownsanta.model.Participant;
import com.bearm.unknownsanta.viewModels.ParticipantViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADDPARTICIPANT = 2;

    ParticipantAdapter mParticipantAdapter;
    List<Participant> participantList;
    MutableLiveData<List<Participant>> liveDataParticipantList;

    ParticipantViewModel participantViewModel;
    EventViewModel eventViewModel;
    Observer<List<Participant>> participantObserver;
    SharedPreferencesHelper sharedPreferencesHelper;
    ParticipantShuffleActivity participantShuffleActivity;
    ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        setSupportActionBar(mainBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Opens AddParticipantActivity activity
        mainBinding.layoutActivityMain.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("New Participant", "ADD");
                Intent participantForm = new Intent(v.getContext(), AddParticipantActivity.class);
                startActivityForResult(participantForm, REQUEST_CODE_ADDPARTICIPANT);
            }
        });

        FloatingActionButton fabEditEvent = findViewById(R.id.fab);
        fabEditEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Edit event info functionality
                showInfoDialog();
            }

        });

        //SharedPreferences Helper init
        sharedPreferencesHelper = new SharedPreferencesHelper(this);

        //ParticipantAdapter init
        participantList = new ArrayList<>();
        liveDataParticipantList = new MutableLiveData<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mainBinding.layoutActivityMain.rvParticipantsList.setLayoutManager(layoutManager);

        //ViewModels init
        ViewModelProvider.AndroidViewModelFactory myViewModelProviderFactory = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        participantViewModel = new ViewModelProvider(this, myViewModelProviderFactory).get(ParticipantViewModel.class);

        mParticipantAdapter = new ParticipantAdapter(participantList, participantViewModel, this);
        mainBinding.layoutActivityMain.rvParticipantsList.setAdapter(mParticipantAdapter);

        participantObserver = new Observer<List<Participant>>() {
            @Override
            public void onChanged(List<Participant> participants) {
                if (participants != null) {
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

    private void showInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.edit_event_title)
                .setMessage(getString(R.string.dialog_under_development_message))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }


    private void sendEmails() throws ExecutionException, InterruptedException {
        EmailCreatorActivity eCreator = new EmailCreatorActivity(getApplicationContext(), SharedPreferencesHelper.getCurrentEvent(), participantList);
        eCreator.setParticipantViewModel(participantViewModel);

        HashMap<String, String> emailData = eCreator.createEmailContent();

        if (!emailData.isEmpty()) {
            boolean result = new SendEmailAsyncTask(emailData)
                    .execute()
                    .get();
            SharedPreferencesHelper.updateCurrentEventEmailStatus(result);
            updateEmailStatus(result, true);
            updateDBEmailStatus(result);
        }
    }



    private void updateEmailStatus(boolean success, boolean inform) {
        if (success) {
            mainBinding.layoutActivityMain.ivEmail.setVisibility(View.VISIBLE);
            SharedPreferencesHelper.updateCurrentEventEmailStatus(true);
            if (inform) {
                Toast.makeText(getApplicationContext(), R.string.toast_email_sent, Toast.LENGTH_LONG).show();
            }
        } else {
            mainBinding.layoutActivityMain.ivEmail.setVisibility(View.INVISIBLE);
            if (inform) {
                Toast.makeText(getApplicationContext(), R.string.toast_email_sent_error, Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(), R.string.toast_paticipant_created, Toast.LENGTH_LONG).show();
            }
        }

    }

    //Adds info about the selected event to Home screen
    public void loadEventInfo() {
        //TODO get event info from database
        //Event myEvent = eventViewModel.getEvent(SharedPreferencesHelper.getCurrentEventId());
        //Checks if there is an event selected (0 = no event)
        if (Integer.parseInt(SharedPreferencesHelper.getCurrentEventId()) > 0) {
            mainBinding.layoutActivityMain.tvEventName.setText(SharedPreferencesHelper.getCurrentEventName());
            mainBinding.layoutActivityMain.tvEventPlace.setText(SharedPreferencesHelper.getCurrentEventPlace());
            mainBinding.layoutActivityMain.tvEventDate.setText(SharedPreferencesHelper.getCurrentEventDate());
            mainBinding.layoutActivityMain.tvEventExpense.setText(SharedPreferencesHelper.getCurrentEventExpense());
            updateEmailStatus(SharedPreferencesHelper.getCurrentEventEmailStatus(), false);

            String iconName = SharedPreferencesHelper.getCurrentEventIconName();
            Log.e("ICON_NAME", iconName);
            if (!iconName.equals("")) {
                int resourceIdImage = this.getResources().getIdentifier(iconName, "drawable",
                        this.getPackageName());
                mainBinding.layoutActivityMain.ivEventIcon.setImageResource(resourceIdImage);
            } else {
                mainBinding.layoutActivityMain.ivEventIcon.setImageResource(R.drawable.ic_christmas_tree);
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Adds items to the action bar if it is present.
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

        if (id == R.id.action_sendEmail) {
            if(SharedPreferencesHelper.getCurrentEventAssignationStatus()) {
                if (!SharedPreferencesHelper.getCurrentEventEmailStatus()) {
                    showSendEmailConfirmation();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.toast_email_already_sent, Toast.LENGTH_LONG).show();
                }
            } else {
                showSendEmailErrorInformation();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
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
            Toast.makeText(getApplicationContext(), R.string.toast_shuffle_error, Toast.LENGTH_LONG).show();
        }

    }

    //Updates Event object in database with Email status (sent or not sent)
    public void updateDBEmailStatus(boolean isSent) {
        Event currentE = SharedPreferencesHelper.getCurrentEvent();
        ViewModelProvider.AndroidViewModelFactory myViewModelProviderFactory = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        eventViewModel = new ViewModelProvider(this, myViewModelProviderFactory).get(EventViewModel.class);
        currentE.isEmailSent(isSent);
        eventViewModel.update(currentE);
    }

    //Updates Event object in database with Assignation status (done or not)
    public void updateDBAssignationStatus(boolean isDone) {
        Event currentE = SharedPreferencesHelper.getCurrentEvent();
        ViewModelProvider.AndroidViewModelFactory myViewModelProviderFactory = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        eventViewModel = new ViewModelProvider(this, myViewModelProviderFactory).get(EventViewModel.class);
        currentE.setAssignationDone(isDone);
        eventViewModel.update(currentE);
    }

    //Opens a dialog with information to confirm before sending the emails
    private void showSendEmailConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_confirmation_title)
                .setMessage(R.string.dialog_send_email_message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            sendEmails();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    private void showSendEmailErrorInformation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
