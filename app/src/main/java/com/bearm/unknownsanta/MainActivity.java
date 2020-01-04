package com.bearm.unknownsanta;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.bearm.unknownsanta.Adapters.MyAdapter;
import com.bearm.unknownsanta.Database.DatabaseClient;
import com.bearm.unknownsanta.Model.Event;
import com.bearm.unknownsanta.Model.Participant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CREATEVENT = 1;
    Button btnNewEvent;
    ImageView btnAddParticipant;
    LinearLayout lyNoEvent;
    LinearLayout lyEventInfo;
    TextView tvEventName;
    TextView tvEventPlace;
    TextView tvEventDate;
    TextView tvEventExpense;

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    ArrayList<Participant> arrayParticipant;
    List<Event> eventList;

    Context context;
    EventViewModel eventViewModel;

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

        btnAddParticipant = findViewById(R.id.btn_add);
        btnAddParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("New Participant", "ADD");
                Intent form = new Intent(v.getContext(), AddParticipants.class);
                startActivity(form);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Enviando emails", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        ViewModelProvider.AndroidViewModelFactory myViewModelProviderFactory = new ViewModelProvider.AndroidViewModelFactory(getApplication());
            }
        });
        eventViewModel = new ViewModelProvider(this, myViewModelProviderFactory).get(EventViewModel.class);
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent eventData) {
        // Check which request it is that we're responding to
        super.onActivityResult(requestCode, resultCode, eventData);
        //CREATE EVENT
        if (requestCode == REQUEST_CODE_CREATEVENT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Event newEvent =    new Event (eventData.getStringExtra("name"),
                                                    eventData.getStringExtra("place"),
                                                    eventData.getStringExtra("date"),
                                                    eventData.getStringExtra("expense"));

                eventViewModel.insert(newEvent);

    }

    public void loadEventInfo(List<Event> eventList) {
        if (!eventList.isEmpty()) {
            tvEventName.setText(eventList.get(0).getName());
            tvEventPlace.setText(eventList.get(0).getPlace());
            tvEventDate.setText(eventList.get(0).getDate());
            tvEventExpense.setText(eventList.get(0).getExpense());

            lyNoEvent.setVisibility(View.GONE);
            lyEventInfo.setVisibility(View.VISIBLE);
        } else {
            lyNoEvent.setVisibility(View.VISIBLE);
            lyEventInfo.setVisibility(View.GONE);
        }

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
        if (id == R.id.action_delete) {
            deleteEvents();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    void deleteEvents() {
        class DeleteEvents extends AsyncTask<Void, Void, List<Event>> {

            @Override
            protected List<Event> doInBackground(Void... voids) {
                DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
}
