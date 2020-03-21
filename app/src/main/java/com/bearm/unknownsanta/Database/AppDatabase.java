package com.bearm.unknownsanta.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.bearm.unknownsanta.DAO.EventDao;
import com.bearm.unknownsanta.DAO.ParticipantDao;
import com.bearm.unknownsanta.Model.Event;
import com.bearm.unknownsanta.Model.Participant;

@Database(entities = {Event.class, Participant.class}, version = 7, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract EventDao eventDao();

    public abstract ParticipantDao participantDao();

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class,
                        "unknown_santa_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                            }
                        })
                        .addMigrations(MIGRATION_6_7)
                        .build();
            }

        }

        return instance;
    }

    static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE event "
                    + " ADD COLUMN isAssignationDone INTEGER DEFAULT 0 NOT NULL");
        }
    };



}
