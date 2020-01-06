package com.bearm.unknownsanta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

public class AddParticipants extends AppCompatActivity {

    private TextInputEditText etEmail;
    private TextInputEditText etName;
    int avatarId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_participants);

        ImageView ivAvatar = findViewById(R.id.iv_rndm_avatar);
        avatarId = getRandomAvatar();
        ivAvatar.setImageResource(avatarId);

        etEmail = findViewById(R.id.edit_email);
        etName = findViewById(R.id.edit_name);

        Button saveBtn = findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createParticipant();
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

    //Set a random avatar everytime the activity is opened
    private int getRandomAvatar() {

        Random random = new Random();
        avatarId = random.nextInt(5);

        switch (avatarId) {
            case 1:
                avatarId = R.drawable.ic_deer;
                break;
            case 2:
                avatarId = R.drawable.ic_elf;
                break;
            case 3:
                avatarId = R.drawable.ic_gingerbread_man;
                break;
            case 4:
                avatarId = R.drawable.ic_snowman;
                break;
            default:
                avatarId = R.drawable.ic_angel;
        }

        return avatarId;
    }

    //Sends the name and email of the participant created to MainActivity
    public void createParticipant() {
        String name = String.valueOf(etName.getText());
        String email = String.valueOf(etEmail.getText());

        if ((name.equals("")) || (email.equals(""))) {
            Toast.makeText(getApplicationContext(), "There are some empty fields.", Toast.LENGTH_LONG).show();

        } else {

            Toast.makeText(getApplicationContext(), "Name: " + name + ", Email: " + email + ", Avatar:v"+ avatarId,  Toast.LENGTH_LONG).show();

            Intent output = new Intent();
            output.putExtra("name", name);
            output.putExtra("email", email);
            output.putExtra("avatar", String.valueOf(avatarId));
            setResult(RESULT_OK, output);
            finish();
        }
    }
}
