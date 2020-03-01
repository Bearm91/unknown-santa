package com.bearm.unknownsanta.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.bearm.unknownsanta.Model.Event;

import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * FROM event")
    LiveData<List<Event>> findAll();

    @Insert
    void insert(Event event);

    @Query("DELETE FROM event WHERE id = :id")
    void delete(int id);

    @Update
    void update(Event event);
}
