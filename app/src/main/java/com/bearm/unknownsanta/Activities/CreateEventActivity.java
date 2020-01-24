package com.bearm.unknownsanta.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bearm.unknownsanta.R;

import java.util.Calendar;
import java.util.Random;


public class CreateEventActivity extends AppCompatActivity{

    EditText eventName;
    EditText eventPlace;
    EditText eventDate;
    EditText eventLimit;
    ImageView ivIcon;

    int avatarId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        eventName = findViewById(R.id.edit_event_name);
        eventPlace = findViewById(R.id.edit_event_place);
        eventDate = findViewById(R.id.edit_event_date);
        eventLimit = findViewById(R.id.edit_event_money);
        ivIcon = findViewById(R.id.event_icon);

        avatarId = getRandomAvatar();
        ivIcon.setImageResource(avatarId);

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

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String day;
                if(dayOfMonth < 10){
                    day = "0" + dayOfMonth;
                } else {
                    day = String.valueOf(dayOfMonth);
                }
                eventDate.setText(day +"/"+ month+1 +"/"+ year);
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
        String expense = String.valueOf(eventLimit.getText());

        if ((name.equals("")) || (place.equals("")) || (date.equals("")) || (expense.equals(""))) {
            Toast.makeText(getApplicationContext(), getText(R.string.event_creation_error), Toast.LENGTH_LONG).show();

        } else {
            Intent output = new Intent();
            output.putExtra("name", name);
            output.putExtra("place", place);
            output.putExtra("date", date);
            output.putExtra("expense", expense);
            setResult(RESULT_OK, output);
            finish();
        }

    }

    //Sets a random avatar every time the activity is opened
    private int getRandomAvatar() {

        Random random = new Random();
        avatarId = random.nextInt(4);

        switch (avatarId) {
            case 1:
                avatarId = R.drawable.ic_snow;
                break;
            case 2:
                avatarId = R.drawable.ic_star;
                break;
            case 3:
                avatarId = R.drawable.ic_wreath;
                break;
            default:
                avatarId = R.drawable.ic_muffin;
        }

        return avatarId;
    }
}
