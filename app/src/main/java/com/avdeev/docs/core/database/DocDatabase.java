package com.avdeev.docs.core.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.avdeev.docs.core.database.dao.ApiPath;
import com.avdeev.docs.core.database.dao.Users;
import com.avdeev.docs.core.database.entity.ApiPathHistory;
import com.avdeev.docs.core.database.entity.User;

@Database(entities = {User.class, ApiPathHistory.class}, version = 1)
public abstract class DocDatabase extends RoomDatabase {
    public abstract Users users();
    public abstract ApiPath apiPath();
}
