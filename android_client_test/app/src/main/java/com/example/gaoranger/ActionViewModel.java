package com.example.gaoranger;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class ActionViewModel extends AndroidViewModel{
    private ActionRepository mRepository;

    private LiveData<List<Action>> mAllActions;

    public ActionViewModel(Application application) {
        super(application);
        mRepository = new ActionRepository(application);
        mAllActions = mRepository.getAllActions();
    }

    LiveData<List<Action>> getAllActions() {
        return mAllActions;
    }

    public void insert(Action action) {
        mRepository.insert(action);
    }

    public void update(Action action) {
        mRepository.update(action);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteAction(Action action) {
        mRepository.deleteWord(action);
    }
}
