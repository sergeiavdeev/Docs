package com.avdeev.docs.core.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ApiPathHistory {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "api_path")
    public String apiPath;

    public ApiPathHistory(String apiPath) {
        this.apiPath = apiPath;
    }
}
