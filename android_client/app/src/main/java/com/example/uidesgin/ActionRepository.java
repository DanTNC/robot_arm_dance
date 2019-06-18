package com.example.uidesgin;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import java.util.List;
public class ActionRepository {
    private ActionDao mActionDao;
    private LiveData<List<Action>> mAllActions;

    ActionRepository(Application application) {
        ActionRoomDatabase db = ActionRoomDatabase.getDatabase(application);
        mActionDao = db.actionDao();
        mAllActions = mActionDao.getAllActions();
    }

    LiveData<List<Action>> getAllActions() {
        return mAllActions;
    }

    public void insert(Action action) {
        new insertAsyncTask(mActionDao).execute(action);
    }

    public void update(Action action) {
        new updateAsyncTask(mActionDao).execute(action);
    }

    public void deleteAll() {
        new deleteAllWordsAsyncTask(mActionDao).execute();
    }

    // Need to run off main thread
    public void deleteWord(Action action) {
        new deleteWordAsyncTask(mActionDao).execute(action);
    }

    // Static inner classes below here to run database interactions
    // in the background.

    /**
     * Insert a word into the database.
     */
    private static class insertAsyncTask extends AsyncTask<Action, Void, Void> {

        private ActionDao mAsyncTaskDao;

        insertAsyncTask(ActionDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Action... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Action, Void, Void> {

        private ActionDao mAsyncTaskDao;

        updateAsyncTask(ActionDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Action... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    /**
     * Delete all words from the database (does not delete the table)
     */
    private static class deleteAllWordsAsyncTask extends AsyncTask<Void, Void, Void> {
        private ActionDao mAsyncTaskDao;

        deleteAllWordsAsyncTask(ActionDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    /**
     *  Delete a single word from the database.
     */
    private static class deleteWordAsyncTask extends AsyncTask<Action, Void, Void> {
        private ActionDao mAsyncTaskDao;

        deleteWordAsyncTask(ActionDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Action... params) {
            mAsyncTaskDao.deleteAction(params[0]);
            return null;
        }
    }
}
