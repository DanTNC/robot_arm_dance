package com.example.uidesign;

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

    @ColumnInfo(name = "description")
    private String mDescription;

    @ColumnInfo(name = "action")
    private String mAction;

    public Action(@NonNull String name, @NonNull String description, @NonNull String action)
    {this.mName = name;this.mDescription = description; this.mAction = action;}

    public void setId(int id) {
        this.id = id;
    }
    public int getId(){return this.id;}
    public void setName(String name){this.mName = name;}
    public String getName(){return this.mName;}
    public void setAction(String action) {
        this.mAction = action;
    }
    public String getAction() {return this.mAction;}
    public void setDescription(String description){this.mDescription = description;}
    public String getDescription(){return this.mDescription;}
}
