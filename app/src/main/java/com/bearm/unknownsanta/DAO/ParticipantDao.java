package com.bearm.unknownsanta.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.bearm.unknownsanta.model.Participant;

import java.util.List;

@Dao
public interface ParticipantDao {

    @Query("SELECT * FROM participant WHERE eventId = :eventId")
    LiveData<List<Participant>> findByEventId(int eventId);

    @Insert
    void insert(Participant participant);

    @Delete
    void delete(Participant participant);

    @Update
    void update(List<Participant> participantList);

    @Query("SELECT name FROM participant WHERE id = :currentPReceiverId")
    String findNameById(int currentPReceiverId);
}
