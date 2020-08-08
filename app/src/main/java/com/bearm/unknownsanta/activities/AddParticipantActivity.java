package com.bearm.unknownsanta.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bearm.unknownsanta.R;
import com.bearm.unknownsanta.databinding.ActivityAddParticipantsBinding;

import java.util.Random;

public class AddParticipantActivity extends AppCompatActivity {

    //ViewBinding
    ActivityAddParticipantsBinding addParticipantsBinding;

    String avatarId;
    static final String EMAIL_REGEX = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addParticipantsBinding = ActivityAddParticipantsBinding.inflate(getLayoutInflater());
        setContentView(addParticipantsBinding.getRoot());

        setParticipantIcon();

        addParticipantsBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createParticipant();
            }
        });

        //Cancel button will close ativity and return to main
        addParticipantsBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //Sets a random avatar every time the activity is opened
    private String getRandomAvatar() {
        Random random = new Random();
        int rndavatarId = random.nextInt(7);
        switch (rndavatarId) {
            case 1:
                avatarId = "ic_deer";
                break;
            case 2:
                avatarId = "ic_elf";
                break;
            case 3:
                avatarId = "ic_gingerbread_man";
                break;
            case 4:
                avatarId = "ic_snowman";
                break;
            case 5:
                avatarId = "ic_gift";
                break;
            case 6:
                avatarId = "ic_milk";
                break;
            default:
                avatarId = "ic_angel";
        }
        return avatarId;
    }

    //Sets the random icon for the new event
    private void setParticipantIcon() {
        avatarId = getRandomAvatar();

        int resourceIdImage = this.getResources().getIdentifier(avatarId, "drawable",
                this.getPackageName());
        addParticipantsBinding.ivRndmAvatar.setImageResource(resourceIdImage);
        Log.e("EVENT_ICON", avatarId);

    }

    //Sends the name and email of the participant to MainActivity
    public void createParticipant() {
        String name = String.valueOf(addParticipantsBinding.editName.getText());
        String email = String.valueOf(addParticipantsBinding.editEmail.getText());

        boolean ok = false;
        if (name.equals("")) {
            addParticipantsBinding.tiName.setError(getString(R.string.error_empty_name));
        } else {
            addParticipantsBinding.tiName.setError(null);
        }

        if (emailIsValid(email)) {
            ok = true;
            addParticipantsBinding.tiEmail.setError(null);
        } else {
            addParticipantsBinding.tiEmail.setError(getString(R.string.error_format_email));
        }
        if (email.equals("")) {
            addParticipantsBinding.tiEmail.setError(getString(R.string.error_emtpy_email));
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

    //Checks if the email format is valid
    public boolean emailIsValid(String email) {
        return email.matches(EMAIL_REGEX);
    }
}
