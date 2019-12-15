package com.bearm.unknownsanta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

public class AddParticipants extends AppCompatActivity {

    TextInputEditText et_email;
    TextInputEditText et_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_participants);

        ImageView avatar = findViewById(R.id.iv_avatar);
        avatar.setImageResource(getRandomAvatar());

        et_email = findViewById(R.id.edit_email);
        et_name = findViewById(R.id.edit_name);

        Button saveBtn = findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveParticipant();
            }
        });

        //Cancel button will close ativity and return to previous
        Button cancelBtn = findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //Set a random avatar everytime the activity is opened
    private int getRandomAvatar() {

        Random random = new Random();
        int avatar;

        switch (random.nextInt(5)){
            case 1:
                avatar = R.drawable.ic_deer;
                break;
            case 2:
                avatar = R.drawable.ic_elf;
                break;
            case 3:
                avatar = R.drawable.ic_gingerbread_man;
                break;
            case 4:
                avatar = R.drawable.ic_snowman;
                break;
            default:
                avatar = R.drawable.ic_angel;
        }

        return avatar;
    }

    //Saves the name and email of every participan created
    //TODO Save data on database
    public void saveParticipant(){
        String name = String.valueOf(et_name.getText());
        String email = String.valueOf(et_email.getText());

        Toast.makeText(getApplicationContext(), "Name: " + name + ", Email: " + email, Toast.LENGTH_LONG).show();
    }
}
