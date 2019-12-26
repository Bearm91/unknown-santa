package com.bearm.unknownsanta.DAO;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.bearm.unknownsanta.Model.Participant;

import java.util.List;

public interface ParticipantDao {
    @Query("SELECT * FROM participant")
    List<Participant> getAll();

    @Query("SELECT * FROM participant where eventId = :eventID")
    List<Participant> findAllByEventId(int eventID);

    @Query("SELECT * FROM participant WHERE name LIKE :participantName LIMIT 1")
    Participant findByName(String participantName);

    @Query("SELECT * FROM participant WHERE email LIKE :participantEmail LIMIT 1")
    Participant findByEmail(String participantEmail);

    @Insert
    void insertParticipant(Participant participant);

    @Delete
    void delete(Participant participant);
}
