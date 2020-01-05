package com.bearm.unknownsanta;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bearm.unknownsanta.Adapters.EventAdapter;
import com.bearm.unknownsanta.Model.Event;
import com.bearm.unknownsanta.Model.EventViewModel;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EventAdapter mEventAdapter;
    List<Event> eventList;

    EventViewModel eventViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_event);

        recyclerView = findViewById(R.id.rv_events_list);
        eventList = new ArrayList<>();
        mEventAdapter = new EventAdapter(eventList, getApplicationContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(mEventAdapter);

        ViewModelProvider.AndroidViewModelFactory myViewModelProviderFactory = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        eventViewModel = new ViewModelProvider(this, myViewModelProviderFactory).get(EventViewModel.class);
        eventViewModel.getEventList().observe(this, new Observer<List<Event>>() {

            @Override
            public void onChanged(List<Event> events) {
                mEventAdapter.setEvents(events);
            }
        });


        //Cancel button will close activity and return to main
        Button backBtn = findViewById(R.id.btn_select_event);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent output = new Intent();
                output.putExtra("load", "LOAD");
                setResult(RESULT_OK, output);
                finish();
            }
        });
    }

}
