package com.bearm.unknownsanta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bearm.unknownsanta.Model.Event;
import com.bearm.unknownsanta.Model.Participant;

public class CreateEvent extends AppCompatActivity {

    EditText eventName;
    EditText eventPlace;
    EditText eventDate;
    EditText eventLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        eventName = findViewById(R.id.edit_event_name);
        eventPlace = findViewById(R.id.edit_event_place);
        eventDate = findViewById(R.id.edit_event_date);
        eventLimit = findViewById(R.id.edit_event_money);

        Button saveBtn = findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent();
            }
        });

        //Cancel button will close ativity and return to main
        Button cancelBtn = findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void createEvent() {
        String name = String.valueOf(eventName.getText());
        String place = String.valueOf(eventPlace.getText());
        String date = String.valueOf(eventDate.getText());
        String expense = String.valueOf(eventLimit.getText());

        Log.i("DATA", "Name: " + name + ", Place: " + place + ", Date: " + date + ", Limit: " + expense);

        if ((name.equals("")) || (place.equals("")) || (date.equals("")) || (expense.equals(""))) {
            Toast.makeText(getApplicationContext(), "There are some empty fields.", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), "Name: " + name + ", Place: " + place + ", Date: " + date + ", Expense limit: " + expense, Toast.LENGTH_LONG).show();

            Intent output = new Intent();
            output.putExtra("name", name);
            output.putExtra("place", place);
            output.putExtra("date", date);
            output.putExtra("expense", expense);
            setResult(RESULT_OK, output);
            finish();
        }

    }
}
