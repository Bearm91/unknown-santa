package com.bearm.unknownsanta.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.bearm.unknownsanta.DAO.EventDao;
import com.bearm.unknownsanta.DAO.ParticipantDao;
import com.bearm.unknownsanta.Model.Event;
import com.bearm.unknownsanta.Model.Participant;

@Database(entities = {Event.class, Participant.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EventDao userDao();

    public abstract ParticipantDao participantDao();
}
