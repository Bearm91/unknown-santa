package com.bearm.unknownsanta.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bearm.unknownsanta.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

public class AddParticipantActivity extends AppCompatActivity {

    private TextInputLayout tiEmail;
    private TextInputLayout tiName;
    private TextInputEditText etEmail;
    private TextInputEditText etName;
    int avatarId;
    static final String EMAIL_REGEX = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_participants);

        ImageView ivAvatar = findViewById(R.id.iv_rndm_avatar);
        avatarId = getRandomAvatar();
        ivAvatar.setImageResource(avatarId);

        etEmail = findViewById(R.id.edit_email);
        etName = findViewById(R.id.edit_name);
        tiEmail = findViewById(R.id.ti_email);
        tiName = findViewById(R.id.ti_name);

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

    //Sets a random avatar every time the activity is opened
    private int getRandomAvatar() {

        Random random = new Random();
        avatarId = random.nextInt(7);

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
            case 5:
                avatarId = R.drawable.ic_gift;
                break;
            case 6:
                avatarId = R.drawable.ic_milk;
                break;
            default:
                avatarId = R.drawable.ic_angel;
        }

        return avatarId;
    }

    //Sends the name and email of the participant to MainActivity
    public void createParticipant() {
        String name = String.valueOf(etName.getText());
        String email = String.valueOf(etEmail.getText());

        boolean ok = false;
        if (name.equals("")) {
            tiName.setError(getString(R.string.error_empty_name));
        } else {
            tiName.setError(null);
        }

        if (emailIsValid(email)) {
            ok = true;
            tiEmail.setError(null);
        } else {
            tiEmail.setError(getString(R.string.error_format_email));
        }
        if (email.equals("")) {
            tiEmail.setError(getString(R.string.error_emtpy_email));
        }
        if (ok) {
            Intent output = new Intent();
            output.putExtra("name", name);
            output.putExtra("email", email);
            output.putExtra("avatar", String.valueOf(avatarId));
            setResult(RESULT_OK, output);
            finish();

        }

    }

    //Checks if the email input is valid
    public boolean emailIsValid(String email) {
        return email.matches(EMAIL_REGEX);
    }
}
