package com.avdeev.docs.core.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.avdeev.docs.core.database.entity.User;

@Dao
public interface Users {

    @Query("SELECT * FROM user LIMIT 1")
    User getOne();

    @Query("DELETE FROM user")
    void clear();

    @Insert
    void add(User user);
}
