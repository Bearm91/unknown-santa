package com.bearm.unknownsanta.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.bearm.unknownsanta.R;
import com.bearm.unknownsanta.databinding.ActivityCreateEventBinding;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class CreateEventActivity extends AppCompatActivity {

    String eventIcon;

    ActivityCreateEventBinding createEventBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createEventBinding = ActivityCreateEventBinding.inflate(getLayoutInflater());
        setContentView(createEventBinding.getRoot());

        setRandomEventIcon();

        createEventBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent();
            }
        });

        //Cancel button will close activity and return to main
        createEventBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        createEventBinding.editEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

    }
    //Sets the random icon for the new event
    private void setRandomEventIcon() {
        eventIcon = getRandomIcon();
        Log.e("EVENT_ICON", eventIcon);
        int resourceIdImage = this.getResources().getIdentifier(eventIcon, "drawable",
                this.getPackageName());
        createEventBinding.eventIcon.setImageResource(resourceIdImage);
    }

    //Opens a date picker dialog to select the date of the new event
    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.myAlertDialogs, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String day;
                if (dayOfMonth < 10) {
                    day = "0" + dayOfMonth;
                } else {
                    day = String.valueOf(dayOfMonth);
                }
                month = month + 1;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date date = null;
                try {
                    date = sdf.parse(day + "/" + month + "/" + year);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                createEventBinding.editEventDate.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(date));
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.colorPrimary));
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.colorPrimary));
    }

    //Checks form data and sends it to EventsActivity to be saved in the database
    private void createEvent() {
        String name = String.valueOf(createEventBinding.editEventName.getText());
        String place = String.valueOf(createEventBinding.editEventPlace.getText());
        String date = String.valueOf(createEventBinding.editEventDate.getText());
        String expense = String.valueOf(createEventBinding.editEventExpense.getText());

        boolean ok = false;
        if (name.isEmpty()) {
            createEventBinding.tiEventName.setError(getString(R.string.error_empty_name));
        } else {
            createEventBinding.tiEventName.setError(null);
        }

        if (date.isEmpty()) {
            createEventBinding.tiEventDate.setError(getString(R.string.error_empty_date));
        } else {
            createEventBinding.tiEventDate.setError(null);
        }

        if (expense.isEmpty()) {
            createEventBinding.tiEventExpense.setError(getString(R.string.error_empty_expense));
        } else {
            createEventBinding.tiEventExpense.setError(null);
        }

        if (place.isEmpty()) {
            place = getString(R.string.no_place_selected);
        }
        if ((!name.isEmpty()) && (!date.isEmpty()) && (!expense.isEmpty())) {
            ok = true;
        }

        if (ok) {
            Intent output = new Intent();
            output.putExtra("name", name);
            output.putExtra("place", place);
            output.putExtra("date", date);
            output.putExtra("expense", expense);
            output.putExtra("icon", eventIcon);
            setResult(RESULT_OK, output);
            finish();
        }
    }

    //Sets a random avatar for the event every time the activity is opened
    private String getRandomIcon() {
        Random random = new Random();
        int rndm = random.nextInt(6);
        switch (rndm) {
            case 1:
                eventIcon = "ic_snow";
                break;
            case 2:
                eventIcon = "ic_star";
                break;
            case 3:
                eventIcon = "ic_wreath";
                break;
            case 4:
                eventIcon = "ic_candy";
                break;
            case 5:
                eventIcon = "ic_cabin";
                break;
            default:
                eventIcon = "ic_christmas_tree";
        }
        return eventIcon;
    }
}
