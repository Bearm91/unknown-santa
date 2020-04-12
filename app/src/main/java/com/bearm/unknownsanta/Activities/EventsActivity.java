package com.bearm.unknownsanta.Activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bearm.unknownsanta.Adapters.EventAdapter;
import com.bearm.unknownsanta.Model.Event;
import com.bearm.unknownsanta.R;
import com.bearm.unknownsanta.Helpers.SharedPreferencesHelper;
import com.bearm.unknownsanta.ViewModels.EventViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EventAdapter mEventAdapter;
    List<Event> eventList;
    FloatingActionButton fabsendEmail;
    private static final int REQUEST_CODE_CREATEVENT = 1;

    EventViewModel eventViewModel;
    SharedPreferences eventData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView tvNoData = findViewById(R.id.no_data_message);
        fabsendEmail = findViewById(R.id.fab);
        fabsendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("New event", "CREATE");
                Intent eventForm;
                eventForm = new Intent(v.getContext(), CreateEventActivity.class);
                startActivityForResult(eventForm, REQUEST_CODE_CREATEVENT);
            }
        });

        //Sends a signal to MainActivity to change displayed info about selected event
        /*final Button selectBtn = findViewById(R.id.btn_select_event);
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventId2 = eventData.getString("eventId2", null);
                if (eventId2 != null) {
                    saveSelectedEvent();
                }
                Intent output = new Intent();
                output.putExtra("load", "LOAD");
                setResult(RESULT_OK, output);
                finish();
            }
        });

        //Closes activity
        final Button cancelBtn = findViewById(R.id.btn_cancel_event);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

        eventData = this.getSharedPreferences("my_us_event", Context.MODE_PRIVATE);

        //ViewModel
        ViewModelProvider.AndroidViewModelFactory myViewModelProviderFactory = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        eventViewModel = new ViewModelProvider(this, myViewModelProviderFactory).get(EventViewModel.class);
        eventViewModel.getEventList().observe(this, new Observer<List<Event>>() {

            @Override
            public void onChanged(List<Event> events) {
                //Changes layout to show a message when there are no events to list
                if (events.isEmpty()) {
                    tvNoData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    //selectBtn.setVisibility(View.GONE);
                    //cancelBtn.setVisibility(View.VISIBLE);

                } else {
                    tvNoData.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    //selectBtn.setVisibility(View.VISIBLE);
                    //cancelBtn.setVisibility(View.VISIBLE);

                    mEventAdapter.setEvents(events);
                }
            }
        });

        recyclerView = findViewById(R.id.rv_events_list);
        eventList = new ArrayList<>();
        mEventAdapter = new EventAdapter(eventList, eventViewModel, getApplicationContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(mEventAdapter);

    }

    //Saves selected event into SharedPreferences1
    private void saveSelectedEvent() {
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(this);
        sharedPreferencesHelper.setSelectedEventAsCurrent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Search among events
        if (id == R.id.action_search) {
            return true;
        }

        //Delete all events
        if (id == R.id.action_delete_all) {
            return true;
        }

        //TODO Displays info about the app
        if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                        false, data.getStringExtra("icon"));

                eventViewModel.insert(newEvent);

                Toast.makeText(getApplicationContext(), data.getStringExtra("name") + " was created correctly.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
