package com.bearm.unknownsanta.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.bearm.unknownsanta.Model.Event;

import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * FROM event")
    List<Event> getAll();

    @Query("SELECT * FROM event WHERE name LIKE :eventName LIMIT 1")
    Event findByName(String eventName);

    @Insert
    void insertEvent(Event event);

    @Delete
    void delete(Event event);

    @Query ("DELETE FROM event")
    void deleteAll();
}
