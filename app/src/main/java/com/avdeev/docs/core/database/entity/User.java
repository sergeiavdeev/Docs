package com.avdeev.docs.core.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String token;
    @ColumnInfo(name="api_path")
    public String apiPath;

    public User(String token, String apiPath) {
        this.token = token;
        this.apiPath = apiPath;
    }
}
