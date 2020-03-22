package com.bearm.unknownsanta;

import android.content.Context;
import android.content.SharedPreferences;

import com.bearm.unknownsanta.Model.Event;

public class SharedPreferencesHelper {

    private Context context;
    static SharedPreferences currentEventData;
    static SharedPreferences selectedEventData;

    public SharedPreferencesHelper(Context context) {
        this.context = context;
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

    public static void setSelectedEventAsCurrent(){
        SharedPreferences.Editor editor = selectedEventData.edit();
        editor.putString("eventId", selectedEventData.getString("eventId2", null));
        editor.putString("eventName", selectedEventData.getString("eventName2", null));
        editor.putString("eventPlace", selectedEventData.getString("eventPlace2", null));
        editor.putString("eventDate", selectedEventData.getString("eventDate2", null));
        editor.putString("eventExpense", selectedEventData.getString("eventExpense2", null));
        editor.putBoolean("eventIsAssignationDone", selectedEventData.getBoolean("eventIsAssignationDone2", false));
        editor.putBoolean("eventIsEmailSent", selectedEventData.getBoolean("eventIsEmailSent2", false));

        editor.apply();
    }

    public static void saveSelectedEvent(String id, String name, String place, String date, String expense, boolean assigned, boolean sent){
        SharedPreferences.Editor editor = selectedEventData.edit();
        editor.putString("eventId2", id);
        editor.putString("eventName2", name);
        editor.putString("eventPlace2", place);
        editor.putString("eventDate2", date);
        editor.putString("eventExpense2",expense);
        editor.putBoolean("eventIsAssignationDone2", assigned);
        editor.putBoolean("eventIsEmailSent2", sent);

        editor.apply();
    }

    //Reads eventId of selected event from SharedPreferences
    public static String getCurrentEventId() {
        return currentEventData.getString("eventId", "0");
    }

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
