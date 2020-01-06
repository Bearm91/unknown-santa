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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bearm.unknownsanta.Adapters.ParticipantAdapter;
import com.bearm.unknownsanta.Model.Event;
import com.bearm.unknownsanta.Model.EventViewModel;
import com.bearm.unknownsanta.Model.Participant;
import com.bearm.unknownsanta.Model.ParticipantViewModel;
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
    LinearLayout lyPartciipantInfo;
    TextView tvEventName;
    TextView tvEventPlace;
    TextView tvEventDate;
    TextView tvEventExpense;

    RecyclerView recyclerView;
    ParticipantAdapter mParticipantAdapter;
    List<Participant> participantList;

    ParticipantViewModel participantViewModel;
    EventViewModel eventViewModel;


    SharedPreferences currentEventData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lyEventInfo = findViewById(R.id.layout_event_data);
        lyNoEvent = findViewById(R.id.layout_no_event);
        tvEventName = findViewById(R.id.tv_event_name);
        tvEventPlace = findViewById(R.id.tv_event_place);
        tvEventDate = findViewById(R.id.tv_event_date);
        tvEventExpense = findViewById(R.id.tv_event_money);
        btnNewEvent = findViewById(R.id.btn_new_event);
        btnNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("New event", "CREATE");
                Intent eventForm;
                eventForm = new Intent(v.getContext(), CreateEvent.class);
                startActivityForResult(eventForm, REQUEST_CODE_CREATEVENT);
            }
        });

        btnSelectEvent = findViewById(R.id.btn_select_event);
        btnSelectEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectEvent(v);
            }
        });

        lyPartciipantInfo = findViewById(R.id.layout_participant_data);

        btnAddParticipant = findViewById(R.id.btn_add);
        btnAddParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("New Participant", "ADD");
                Intent participantForm = new Intent(v.getContext(), AddParticipants.class);
                startActivityForResult(participantForm, REQUEST_CODE_ADDPARTICIPANT);
            }
        });


        //TODO set action
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Will be added at the end
            }
        });



        currentEventData = this.getSharedPreferences("my_us_event", Context.MODE_PRIVATE);

        loadEventInfo();

        participantList = new ArrayList<>();
        mParticipantAdapter = new ParticipantAdapter(participantList, getApplication());

        recyclerView = findViewById(R.id.rv_participants_list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(mParticipantAdapter);

        ViewModelProvider.AndroidViewModelFactory myViewModelProviderFactory = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        participantViewModel = new ViewModelProvider(this, myViewModelProviderFactory).get(ParticipantViewModel.class);
        participantViewModel.getParticipantList(Integer.parseInt(getCurrentEventId())).observe(this, new Observer<List<Participant>>() {

            @Override
            public void onChanged(List<Participant> participants) {
                mParticipantAdapter.setParticipants(participants);
            }
        });

        eventViewModel = new ViewModelProvider(this, myViewModelProviderFactory).get(EventViewModel.class);

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
        //CREATE EVENT
        if (requestCode == REQUEST_CODE_CREATEVENT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Event newEvent = new Event(data.getStringExtra("name"),
                        data.getStringExtra("place"),
                        data.getStringExtra("date"),
                        data.getStringExtra("expense"));

                eventViewModel.insert(newEvent);

            }
        }

        //ADD PARTICIPANT
        if (requestCode == REQUEST_CODE_ADDPARTICIPANT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Participant newParticipant = new Participant(data.getStringExtra("name"),
                        data.getStringExtra("email"),
                        data.getStringExtra("avatar"),
                        Integer.parseInt(getCurrentEventId()));

                participantViewModel.insert(newParticipant);
            }
        }

        //SELECT EVENT
        if (requestCode == REQUEST_CODE_SELECTEVENT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                loadEventInfo();
            }
        }
    }

    public void loadEventInfo() {

        if (Integer.parseInt(getCurrentEventId()) > 0) {
            tvEventName.setText(currentEventData.getString("eventName", null));
            tvEventPlace.setText(currentEventData.getString("eventPlace", null));
            tvEventDate.setText(currentEventData.getString("eventDate", null));
            tvEventExpense.setText(currentEventData.getString("eventExpense", null));

            lyNoEvent.setVisibility(View.GONE);
            lyEventInfo.setVisibility(View.VISIBLE);
            lyPartciipantInfo.setVisibility(View.VISIBLE);
        } else {
            lyNoEvent.setVisibility(View.VISIBLE);
            lyEventInfo.setVisibility(View.GONE);
            lyPartciipantInfo.setVisibility(View.INVISIBLE);
        }

    }

    public String getCurrentEventId() {
        return currentEventData.getString("eventId", "0");
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_event) {
            if (getCurrentEventId() != null) {
                eventViewModel.deleteEvent(Integer.parseInt(getCurrentEventId()));
                closeCurrentEvent();
                loadEventInfo();
            }
            return true;

        }

        if (id == R.id.action_change_event) {
            openSelectEvent(null);
            return true;
        }

        if (id == R.id.action_close_event) {
            closeCurrentEvent();
            loadEventInfo();
            return true;
        }

        if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void closeCurrentEvent() {
        currentEventData.edit().clear().apply();
    }

}
