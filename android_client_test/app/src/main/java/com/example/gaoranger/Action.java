package com.example.gaoranger;

import android.support.annotation.NonNull;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "action_table")
public class Action {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "action")
    private String mAction;

    public Action(@NonNull String name, @NonNull String action)
    {this.mName = name;this.mAction = action;}

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name){this.mName=name;}
    public void setAction(String action) {
        this.mAction = action;
    }
    public String getAction() {return this.mAction;}
    public int getId(){return this.id;}
    public String getName(){return this.mName;}
}
