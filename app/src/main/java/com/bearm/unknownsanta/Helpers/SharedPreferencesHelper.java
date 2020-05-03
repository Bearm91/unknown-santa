package com.bearm.unknownsanta.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.bearm.unknownsanta.Model.Event;

public class SharedPreferencesHelper {

    private static SharedPreferences currentEventData;
    private static SharedPreferences selectedEventData;

    public SharedPreferencesHelper(Context context) {
        currentEventData = context.getSharedPreferences("my_us_event", Context.MODE_PRIVATE);
        selectedEventData = context.getSharedPreferences("my_us_event", Context.MODE_PRIVATE);
    }

    public static Event getCurrentEvent() {
        String id = currentEventData.getString("eventId", "0");
        String name = currentEventData.getString("eventName", null);
        String place = currentEventData.getString("eventPlace", null);
        String date = currentEventData.getString("eventDate", null);
        String expense = currentEventData.getString("eventExpense", null);
        boolean isDone = currentEventData.getBoolean("eventIsAssignationDone", false);
        boolean isSent = currentEventData.getBoolean("eventIsEmailSent", false);
        return new Event(Integer.parseInt(id), name, place, date, expense, isDone, isSent);
    }

    public static void setSelectedEventAsCurrent(String id, String name, String place, String date, String expense, boolean assigned, boolean sent){
        SharedPreferences.Editor editor = selectedEventData.edit();
        editor.putString("eventId", id);
        editor.putString("eventName", name);
        editor.putString("eventPlace", place);
        editor.putString("eventDate", date);
        editor.putString("eventExpense",expense);
        editor.putBoolean("eventIsAssignationDone", assigned);
        editor.putBoolean("eventIsEmailSent", sent);

        editor.apply();
    }

    //Reads eventId of selected event from SharedPreferences
    public static String getCurrentEventId() {
        return currentEventData.getString("eventId", "0");
    }

    //Reads eventName of selected event from SharedPreferences
    public static String getCurrentEventName(){
        return currentEventData.getString("eventName", null);
    }

    public static String getCurrentEventPlace(){
        return currentEventData.getString("eventPlace", null);
    }

    public static String getCurrentEventDate(){
        return currentEventData.getString("eventDate", null);
    }

    public static String getCurrentEventExpense(){
        return currentEventData.getString("eventExpense", null);
    }

    public static boolean getCurrentEventAssignationStatus(){
        return currentEventData.getBoolean("eventIsAssignationDone", false);
    }

    public static boolean getCurrentEventEmailStatus(){
        return currentEventData.getBoolean("eventIsEmailSent", false);
    }

    public static void updateCurrentEventAssignationStatus(boolean isAssigned){
        currentEventData.edit().putBoolean("eventIsAssignationDone", isAssigned).apply();
    }

    public static void updateCurrentEventEmailStatus (boolean isSent){

    }

    public static void clearCurrentEvent() {
        currentEventData.edit().clear().apply();
    }
}
