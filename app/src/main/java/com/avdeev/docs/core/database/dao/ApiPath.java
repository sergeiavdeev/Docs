package com.avdeev.docs.core.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.avdeev.docs.core.database.entity.ApiPathHistory;

import java.util.List;

@Dao
public interface ApiPath {
    @Query("SELECT * FROM ApiPathHistory")
    List<ApiPathHistory> getAll();

    @Insert
    void add(ApiPathHistory history);

    @Query("DELETE FROM ApiPathHistory")
    void clear();
}
