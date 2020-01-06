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

    @Query("SELECT * FROM participant")
    LiveData<List<Participant>> getAll();

    @Query("SELECT * FROM participant where eventId = :eventId")
    LiveData<List<Participant>> findByEventId(int eventId);

    @Query("SELECT * FROM participant WHERE name LIKE :participantName LIMIT 1")
    LiveData<Participant> findByName(String participantName);

    @Query("SELECT * FROM participant WHERE email LIKE :participantEmail LIMIT 1")
    LiveData<Participant> findByEmail(String participantEmail);

    @Insert
    void insertParticipant(Participant participant);

    @Delete
    void delete(Participant participant);
}
