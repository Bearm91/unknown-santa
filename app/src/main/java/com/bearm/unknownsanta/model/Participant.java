package com.bearm.unknownsanta.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "participant",
        foreignKeys = @ForeignKey(entity = Event.class,
                parentColumns = "id",
                childColumns = "eventId",
                onDelete = CASCADE),
        indices = {@Index("eventId"),
                @Index(value = {"eventId", "email"})})

public class Participant {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo
    @NonNull
    public String name;

    @ColumnInfo
    @NonNull
    public String email;


    @ColumnInfo
    public String avatarName;

    @ColumnInfo
    int eventId;

    @ColumnInfo
    int idReceiver;

    @Ignore
    public Participant(String name, String email, String avatarName, int eventId) {
        this.name = name;
        this.email = email;
        this.avatarName = avatarName;
        this.eventId = eventId;
    }


    public Participant() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(int idReceiver) {
        this.idReceiver = idReceiver;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", avatarName='" + avatarName + '\'' +
                ", eventId=" + eventId +
                ", idReceiver=" + idReceiver +
                '}';
    }
}

