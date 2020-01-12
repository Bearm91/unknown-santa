package com.bearm.unknownsanta.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.bearm.unknownsanta.Model.Participant;

import java.util.List;

@Dao
public interface ParticipantDao {

    @Query("SELECT * FROM participant where eventId = :eventId")
    LiveData<List<Participant>> findByEventId(int eventId);

    @Query("SELECT * FROM participant where eventId = :eventId")
    List<Participant> findByEvent(int eventId);

    @Insert
    void insert(Participant participant);

    @Delete
    void delete(Participant participant);
}
