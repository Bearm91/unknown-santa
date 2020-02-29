package com.bearm.unknownsanta.Model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "event")
public class Event {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String place;
    private String date;
    private String expense;
    private boolean emailSent;

    @Ignore
    public Event(String name, String place, String date, String expense, boolean emailSent) {
        this.name = name;
        this.place = place;
        this.date = date;
        this.expense = expense;
        this.emailSent = emailSent;
    }

    public Event() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public String getDate() {
        return date;
    }

    public String getExpense() {
        return expense;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }


    public boolean isEmailSent() {
        return emailSent;
    }

    public void setEmailSent(boolean emailSent) {
        this.emailSent = emailSent;
    }
}
