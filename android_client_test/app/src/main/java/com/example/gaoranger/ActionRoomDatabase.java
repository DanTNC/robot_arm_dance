package com.example.gaoranger;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Action.class}, version = 1, exportSchema = false)
public abstract class ActionRoomDatabase extends RoomDatabase{
    public abstract ActionDao actionDao();
    private static ActionRoomDatabase INSTANCE;
    public static ActionRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ActionRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ActionRoomDatabase.class, "action_database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();

                }
            }
        }
        return INSTANCE;
    }
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    // Populate the database with the initial data set
    // only if the database has no entries.
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ActionDao mDao;

        PopulateDbAsync(ActionRoomDatabase db) {
            mDao = db.actionDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // If we have no words, then create the initial list of words

            return null;
        }
    }
    public static void deleteInstance(){
        INSTANCE=null;
    }
}
