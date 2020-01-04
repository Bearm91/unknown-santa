package com.bearm.unknownsanta.Model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "event")
public class Event {

    @PrimaryKey (autoGenerate = true)
    public int id;
    public String name;
    public String place;
    public String date;
    public String expense;

    @Ignore
    public Event(String name, String place, String date, String expense) {
        this.name = name;
        this.place = place;
        this.date = date;
        this.expense = expense;
    }

    public Event() {

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

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }
}
