package com.avdeev.docs.core.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String hash;
    @ColumnInfo(name="api_url")
    public String apiUrl;
    public String key;

    public User(String hash, String apiUrl) {
        this.hash = hash;
        this.apiUrl = apiUrl;
    }
}
