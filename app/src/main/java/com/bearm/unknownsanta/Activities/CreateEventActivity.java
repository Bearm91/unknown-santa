package com.bearm.unknownsanta.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bearm.unknownsanta.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Random;


public class CreateEventActivity extends AppCompatActivity{

    EditText eventName;
    EditText eventPlace;
    EditText eventDate;
    EditText eventExpense;
    ImageView ivIcon;

    TextInputLayout tiName;
    TextInputLayout tiDate;
    TextInputLayout tiExpense;

    int eventIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        eventName = findViewById(R.id.edit_event_name);
        eventPlace = findViewById(R.id.edit_event_place);
        eventDate = findViewById(R.id.edit_event_date);
        eventExpense = findViewById(R.id.edit_event_money);

        ivIcon = findViewById(R.id.event_icon);
        tiName = findViewById(R.id.ti_event_name);
        tiDate = findViewById(R.id.ti_event_date);
        tiExpense = findViewById(R.id.ti_event_expense);

        eventIcon = getRandomAvatar();
        ivIcon.setImageResource(eventIcon);

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

        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.myAlertDialogs, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String day;
                if(dayOfMonth < 10){
                    day = "0" + dayOfMonth;
                } else {
                    day = String.valueOf(dayOfMonth);
                }
                month = month + 1;
                eventDate.setText(day +"/"+ month +"/"+ year);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(getColor(R.color.white_color));
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(getColor(R.color.white_color));
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.colorPrimary));
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.colorPrimary));


    }

    private void createEvent() {
        String name = String.valueOf(eventName.getText());
        String place = String.valueOf(eventPlace.getText());
        String date = String.valueOf(eventDate.getText());
        String expense = String.valueOf(eventExpense.getText());

        boolean ok = false;
        if (name.isEmpty()){
            tiName.setError(getString(R.string.error_empty_name));
        } else {
            tiName.setError(null);
        }

        if (date.isEmpty()) {
            tiDate.setError(getString(R.string.error_empty_date));
        } else {
            tiDate.setError(null);
        }

        if (expense.isEmpty()){
            tiExpense.setError(getString(R.string.error_empty_expense));
        } else {
            tiExpense.setError(null);
        }

        if (place.isEmpty()){
            place = "No place selected";
        }
        if ((!name.isEmpty()) && (!date.isEmpty()) && (!expense.isEmpty())) {
            ok = true;
        }
        
        if(ok) {
            Intent output = new Intent();
            output.putExtra("name", name);
            output.putExtra("place", place);
            output.putExtra("date", date);
            output.putExtra("expense", expense);
            output.putExtra("icon", String.valueOf(eventIcon));
            setResult(RESULT_OK, output);
            finish();
        }

    }

    //Sets a random avatar every time the activity is opened
    private int getRandomAvatar() {

        Random random = new Random();
        eventIcon = random.nextInt(6);

        switch (eventIcon) {
            case 1:
                eventIcon = R.drawable.ic_snow;
                break;
            case 2:
                eventIcon = R.drawable.ic_star;
                break;
            case 3:
                eventIcon = R.drawable.ic_wreath;
                break;
            case 4:
                eventIcon = R.drawable.ic_candy;
                break;
            case 5:
                eventIcon = R.drawable.ic_cabin;
            default:
                eventIcon = R.drawable.ic_muffin;
        }

        return eventIcon;
    }
}
