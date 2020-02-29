package com.bearm.unknownsanta;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.bearm.unknownsanta.Activities.EmailCreator;
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
import java.util.List;

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

    RecyclerView recyclerView;
    ParticipantAdapter mParticipantAdapter;
    List<Participant> participantList;
    MutableLiveData<List<Participant>> liveDataParticipantList;

    ParticipantViewModel participantViewModel;
    EventViewModel eventViewModel;
    Observer<List<Participant>> participantObserver;

    SharedPreferences currentEventData;

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


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(checkAssignments()){
                sendEmails();
                //}
            }
        });

        //SharedPreferences init
        currentEventData = this.getSharedPreferences("my_us_event", Context.MODE_PRIVATE);

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
        participantViewModel.setFilter(getCurrentEventId());
        eventViewModel = new ViewModelProvider(this, myViewModelProviderFactory).get(EventViewModel.class);

        participantShuffleActivity = new ParticipantShuffleActivity(participantViewModel, getApplicationContext());

        //Checks selected event info
        loadEventInfo();
    }

    private void sendEmails() {
        String emailMessage;
        String receiver;
        String recipient;
        Participant currentParticipant;

        EmailCreator emailCreator = new EmailCreator(getCurrentEvent(), participantList);

        for (int i = 0; i < participantList.size(); i++) {
            currentParticipant = participantList.get(i);
            recipient = currentParticipant.getEmail();
            emailMessage = "";
            try {
                receiver = participantList.get(currentParticipant.getIdReceiver() - 1).getName();

                emailCreator.createEmailBody(receiver);
                emailMessage = emailCreator.getEmailBody();
                Log.e("MESSAGE", emailMessage);
            } catch (NullPointerException npe) {
                Toast.makeText(getApplicationContext(), "1Sorry, there are still participants with no assignation.", Toast.LENGTH_LONG).show();
            } catch (ArrayIndexOutOfBoundsException aioob) {
                //TODO change toast for alert dialog
                Toast.makeText(getApplicationContext(), "2Sorry, there are still participants with no assignation. Check that all participants have the green mark next to their names, and press on 'Assign Santas' in the menu below if anyone doesn't.", Toast.LENGTH_LONG).show();
            }

            if (!emailMessage.isEmpty()) {
                final String finalEmailMessage = emailMessage;
                final String finalRecipient = recipient;
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            GMailSender mailSender = new GMailSender("unknownsantaapp@gmail.com", "Android123.-");
                            mailSender.sendMail("Secret Santa Subject", finalEmailMessage, "unknownsantaapp@gmail.com", finalRecipient);
                            //Toast.makeText(getApplicationContext(), "An email has been sent to all participants.", Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            Log.e("SendMail", e.getMessage(), e);
                        }
                    }

                }).start();

            }
        }
    }

    private Event getCurrentEvent() {
        String name = currentEventData.getString("eventName", null);
        String place = currentEventData.getString("eventPlace", null);
        String date = currentEventData.getString("eventDate", null);
        String expense = currentEventData.getString("eventExpense", null);
        return new Event(name, place, date, expense, );
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
                        Integer.parseInt(getCurrentEventId()));

                participantViewModel.insert(newParticipant);

                Toast.makeText(getApplicationContext(), "Yay! A new participant joined the event!", Toast.LENGTH_LONG).show();
            }
        }

        //SELECT EVENT AND SHOW ITS INFO IN HOME SCREEN
        if (requestCode == REQUEST_CODE_SELECTEVENT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                loadEventInfo();
                participantViewModel.setFilter(getCurrentEventId());

            }
        }
    }

    //Adds info about the selected event to Home screen
    public void loadEventInfo() {
        //Checks if there is an event selected (0 = no event)
        if (Integer.parseInt(getCurrentEventId()) > 0) {
            tvEventName.setText(currentEventData.getString("eventName", null));
            tvEventPlace.setText(currentEventData.getString("eventPlace", null));
            tvEventDate.setText(currentEventData.getString("eventDate", null));
            tvEventExpense.setText(currentEventData.getString("eventExpense", null));

            if (currentEventData.getString("eventEmailSent", null).equals("false")) {
                ivEmail.setVisibility(View.GONE);
            } else {
                ivEmail.setVisibility(View.VISIBLE);
            }


            //Hide and display elements in the layout when an event is selected
            lyNoEvent.setVisibility(View.GONE);
            lyEventInfo.setVisibility(View.VISIBLE);
            lyParticipantInfo.setVisibility(View.VISIBLE);
            tvEventTitle.setVisibility(View.GONE);
        } else {
            //Hide and display elements in the layout when no event is selected
            lyNoEvent.setVisibility(View.VISIBLE);
            lyEventInfo.setVisibility(View.GONE);
            lyParticipantInfo.setVisibility(View.INVISIBLE);
            tvEventTitle.setVisibility(View.VISIBLE);

        }

    }

    //Reads eventId of selected event from SharedPreferences
    public String getCurrentEventId() {
        return currentEventData.getString("eventId", "0");
    }

    //Deletes info about selected event from SharedPreferences so there is no event marked as selected
    public void closeCurrentEvent() {
        currentEventData.edit().clear().apply();
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
            if (getCurrentEventId() != null) {
                eventViewModel.deleteEvent(Integer.parseInt(getCurrentEventId()));
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
            participantShuffleActivity.setParticipants(participantList);
            participantShuffleActivity.shuffleList();
            participantShuffleActivity.assignGivers();
            return true;
        }

        //TODO Displays info about the app
        if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
