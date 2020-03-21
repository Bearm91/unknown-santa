package com.bearm.unknownsanta.Activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bearm.unknownsanta.Adapters.EventAdapter;
import com.bearm.unknownsanta.Model.Event;
import com.bearm.unknownsanta.R;
import com.bearm.unknownsanta.SharedPreferencesHelper;
import com.bearm.unknownsanta.ViewModels.EventViewModel;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EventAdapter mEventAdapter;
    List<Event> eventList;

    EventViewModel eventViewModel;
    SharedPreferences eventData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_event);

        final TextView tvNoData = findViewById(R.id.no_data_message);

        //Sends a signal to MainActivity to change displayed info about selected event
        final Button selectBtn = findViewById(R.id.btn_select_event);
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
        });

        eventData = this.getSharedPreferences("my_us_event", Context.MODE_PRIVATE);


        recyclerView = findViewById(R.id.rv_events_list);
        eventList = new ArrayList<>();
        mEventAdapter = new EventAdapter(eventList, getApplicationContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(mEventAdapter);

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
                    selectBtn.setVisibility(View.GONE);
                    cancelBtn.setVisibility(View.VISIBLE);

                } else {
                    tvNoData.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    selectBtn.setVisibility(View.VISIBLE);
                    cancelBtn.setVisibility(View.VISIBLE);

                    mEventAdapter.setEvents(events);
                }
            }
        });
    }

    //Saves selected event into SharedPreferences1
    private void saveSelectedEvent() {
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(this);
        sharedPreferencesHelper.setSelectedEventAsCurrent();
    }
}
