package com.avdeev.docs.core.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.avdeev.docs.core.database.entity.User;

import java.util.UUID;

@Dao
public abstract class Users {

    @Query("SELECT * FROM user LIMIT 1")
    public abstract User getOne();

    @Query("DELETE FROM user")
    public abstract void clear();

    @Insert
    abstract void addUser(User user);

    public void add(User user) {
        clear();
        user.key = UUID.randomUUID().toString();
        addUser(user);
    }
}
