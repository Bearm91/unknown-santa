package com.bearm.unknownsanta.activities;


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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bearm.unknownsanta.adapters.EventAdapter;
import com.bearm.unknownsanta.model.Event;
import com.bearm.unknownsanta.R;
import com.bearm.unknownsanta.viewModels.EventViewModel;
import com.bearm.unknownsanta.databinding.ActivityEventsBinding;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {

    EventAdapter mEventAdapter;
    List<Event> eventList;
    private static final int REQUEST_CODE_CREATEVENT = 1;

    //ViewModel
    EventViewModel eventViewModel;

    //ViewBinding
    ActivityEventsBinding eventsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventsBinding = ActivityEventsBinding.inflate(getLayoutInflater());
        setContentView(eventsBinding.getRoot());
        setSupportActionBar(eventsBinding.toolbar);

        eventsBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("New event", "CREATE");
                Intent eventForm;
                eventForm = new Intent(v.getContext(), CreateEventActivity.class);
                startActivityForResult(eventForm, REQUEST_CODE_CREATEVENT);
            }
        });

        //ViewModel
        ViewModelProvider.AndroidViewModelFactory myViewModelProviderFactory = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        eventViewModel = new ViewModelProvider(this, myViewModelProviderFactory).get(EventViewModel.class);
        eventViewModel.getEventList().observe(this, new Observer<List<Event>>() {

            @Override
            public void onChanged(List<Event> events) {
                //Changes layout to show a message when there are no events to list
                if (events.isEmpty()) {
                    //eventsBinding.noDataMessage.setVisibility(View.VISIBLE);
                    eventsBinding.rvEventsList.setVisibility(View.GONE);
                } else {
                    //eventsBinding.noDataMessage.setVisibility(View.GONE);
                    eventsBinding.rvEventsList.setVisibility(View.VISIBLE);

                    mEventAdapter.setEvents(events);
                }
            }
        });

        eventList = new ArrayList<>();
        mEventAdapter = new EventAdapter(eventList, eventViewModel, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        eventsBinding.rvEventsList.setLayoutManager(layoutManager);

        eventsBinding.rvEventsList.setAdapter(mEventAdapter);

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

        //TODO: Search among events
        /*if (id == R.id.action_search) {
            return true;
        }*/

        if (id == R.id.action_delete_all) {
            showDeleteConfirmationDialog();
            return true;
        }

        //TODO Display the instructions
        if (id == R.id.action_tutorial) {
            showTutorialScreen();
            return true;
        }
        if (id == R.id.action_about) {
            showAboutInfo();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_confirmation_title)
                .setMessage(R.string.dialog_delete_events_message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eventViewModel.deleteAllEvents();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    private void showTutorialScreen() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tutorial_title)
                .setMessage(getString(R.string.dialog_under_development_message))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    //Shows a dialog with info about the app
    private void showAboutInfo() {
        String aboutMessageVersion = getString(R.string.about_current_version);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.about_title)
                .setMessage(getString(R.string.abbout_info, aboutMessageVersion))
                .setIcon(R.mipmap.ic_unknown_launcher)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //CREATE EVENT IN DATABASE
        if (requestCode == REQUEST_CODE_CREATEVENT) {
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
