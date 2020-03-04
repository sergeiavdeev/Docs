package com.avdeev.docs.core.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.avdeev.docs.core.database.dao.ApiPath;
import com.avdeev.docs.core.database.dao.DocInbox;
import com.avdeev.docs.core.database.dao.DocInner;
import com.avdeev.docs.core.database.dao.DocOutbox;
import com.avdeev.docs.core.database.dao.Tasks;
import com.avdeev.docs.core.database.dao.Users;
import com.avdeev.docs.core.database.entity.ApiPathHistory;
import com.avdeev.docs.core.database.entity.DocumentInbox;
import com.avdeev.docs.core.database.entity.DocumentInner;
import com.avdeev.docs.core.database.entity.DocumentOutbox;
import com.avdeev.docs.core.database.entity.Task;
import com.avdeev.docs.core.database.entity.File;
import com.avdeev.docs.core.database.entity.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {
                User.class,
                ApiPathHistory.class,
                DocumentInbox.class,
                DocumentOutbox.class,
                DocumentInner.class,
                Task.class,
                File.class
            },
        version = 1, exportSchema = false)
public abstract class DocDatabase extends RoomDatabase {
    public abstract Users user();
    public abstract ApiPath apiPath();
    public abstract DocInbox inbox();
    public abstract DocOutbox outbox();
    public abstract DocInner inner();
    public abstract Tasks task();

    private static final int NUMBER_OF_THREADS = 4;
    private static volatile DocDatabase instance;

    public static final ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static DocDatabase getInstance(Context context) {

        if (instance == null) {
            synchronized (DocDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, DocDatabase.class, "docs").build();
                }
            }
        }
        return instance;
    }

    public static DocDatabase getInstance() {
        return instance;
    }
}
