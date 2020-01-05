package com.bearm.unknownsanta.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.bearm.unknownsanta.Model.Event;

import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * FROM event")
    LiveData<List<Event>> getEventList();

    @Query("SELECT * FROM event WHERE name LIKE :eventName LIMIT 1")
    LiveData<Event> findByName(String eventName);

    @Insert
    void insertEvent(Event event);

    @Query("DELETE FROM event WHERE id = :id")
    void delete(int id);

    @Query("DELETE FROM event")
    void deleteAll();
}
