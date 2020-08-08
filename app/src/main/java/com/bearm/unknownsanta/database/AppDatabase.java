package com.bearm.unknownsanta.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.bearm.unknownsanta.DAO.EventDao;
import com.bearm.unknownsanta.DAO.ParticipantDao;
import com.bearm.unknownsanta.R;
import com.bearm.unknownsanta.model.Event;
import com.bearm.unknownsanta.model.Participant;

import java.util.concurrent.Executors;
@Database(entities = {Event.class, Participant.class}, version = 8, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract EventDao eventDao();

    public abstract ParticipantDao participantDao();

    private static AppDatabase instance;

    public static AppDatabase getInstance(final Context context) {
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
                                Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        populateEventsTable(context);
                                        populateParticipantsTable(context);
                                    }
                                });
                            }
                        })
                        .addMigrations(MIGRATION_6_7)
                        .addMigrations(MIGRATION_7_8)
                        .build();
            }

        }

        return instance;
    }

    private static void populateEventsTable(Context context) {
        Event myEvent = new Event();
        myEvent.setName(context.getString(R.string.event_name_example));
        myEvent.setDate(context.getString(R.string.event_date_example));
        myEvent.setPlace(context.getString(R.string.event_place_example));
        myEvent.setExpense(context.getString(R.string.event_expense_example));
        myEvent.setIconName("ic_christmas_tree");
        myEvent.setAssignationDone(true);
        myEvent.isEmailSent(true);

        getInstance(context).eventDao().insert(myEvent);
    }

    private static void populateParticipantsTable(Context context) {
        Participant myParticipant1 = new Participant(
                context.getString(R.string.participant1_name_example),
                context.getString(R.string.participant1_email_example),
                "ic_elf",
                1 );
        Participant myParticipant2= new Participant(
                context.getString(R.string.participant2_name_example), 
                context.getString(R.string.participant2_email_example), 
                "ic_deer", 
                1 );
        Participant myParticipant3 = new Participant(
                context.getString(R.string.participant3_name_example), 
                context.getString(R.string.participant3_email_example), 
                "ic_angel", 
                1 );
        Participant myParticipant4 = new Participant(
                context.getString(R.string.participant4_name_example),
                context.getString(R.string.participant4_email_example),
                "ic_gift", 
                1);
        myParticipant1.setIdReceiver(2);
        myParticipant2.setIdReceiver(3);
        myParticipant3.setIdReceiver(4);
        myParticipant4.setIdReceiver(1);
        getInstance(context).participantDao().insert(myParticipant1);
        getInstance(context).participantDao().insert(myParticipant2);
        getInstance(context).participantDao().insert(myParticipant3);
        getInstance(context).participantDao().insert(myParticipant4);
    }

    private static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE event "
                    + " ADD COLUMN isAssignationDone INTEGER DEFAULT 0 NOT NULL");
        }
    };

    private static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE event "
                    + " ADD COLUMN iconName TEXT");
        }
    };



}
