package com.bearm.unknownsanta.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.bearm.unknownsanta.BuildConfig;
import com.bearm.unknownsanta.eMailSender.GMailSender;

import java.util.HashMap;

public class SendEmailAsyncTask extends AsyncTask<HashMap<String, String>, String, Boolean> {

    HashMap<String, String> information;
    Context context;

    public SendEmailAsyncTask(HashMap<String, String> emailData, Context context) {
        this.information = emailData;
        this.context = context;
    }

    // Before the tasks execution
    protected void onPreExecute() {
        //mTextView.setText(mTextView.getText() + "\nStarting task....");
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
