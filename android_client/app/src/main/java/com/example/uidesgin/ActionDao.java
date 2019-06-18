package com.example.uidesgin;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
@Dao
public interface ActionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Action action);

    @Update
    void update(Action action);

    @Query("DELETE FROM action_table")
    void deleteAll();

    @Delete
    void deleteAction(Action action);

    @Query("SELECT * from action_table LIMIT 1")
    Action[] getAnyAction();

    @Query("SELECT * from action_table ORDER BY id ASC")
    LiveData<List<Action>> getAllActions();
}
