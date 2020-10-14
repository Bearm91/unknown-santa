package com.bearm.unknownsanta.asyncTasks;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bearm.unknownsanta.BuildConfig;
import com.bearm.unknownsanta.R;
import com.bearm.unknownsanta.eMailSender.GMailSender;

import java.util.HashMap;

public class SendEmailAsyncTask extends AsyncTask<HashMap<String, String>, String, Boolean> {

    HashMap<String, String> information;
    Context context;
    Activity activity;
    protected TextView emailNumber;

    public SendEmailAsyncTask(HashMap<String, String> emailData, Context context, Activity activity) {
        this.information = emailData;
        this.context = context;
        this.activity = activity;
    }


    // Before the tasks execution
    protected void onPreExecute() {
        Toast.makeText(context, "Sending emails. Please, wait...", Toast.LENGTH_LONG);
    }

    @Override
    protected Boolean doInBackground(HashMap<String, String>... hashMaps) {
        boolean isSent;
        try {
            GMailSender mailSender = new GMailSender(BuildConfig.account_email, BuildConfig.account_password, context);
            mailSender.sendMail(information);
            isSent = true;
        } catch (Exception e) {
            isSent = false;
            Log.e("SendMail", e.getMessage(), e);


        }
        return isSent;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        Log.e("SendMail", String.valueOf(result));
    }
}
