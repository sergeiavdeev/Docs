package com.avdeev.docs.core.database;

import androidx.room.Database;
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
import com.avdeev.docs.core.database.entity.TaskFile;
import com.avdeev.docs.core.database.entity.User;

@Database(
        entities = {
                User.class,
                ApiPathHistory.class,
                DocumentInbox.class,
                DocumentOutbox.class,
                DocumentInner.class,
                Task.class,
                TaskFile.class
            },
        version = 1, exportSchema = false)
public abstract class DocDatabase extends RoomDatabase {
    public abstract Users user();
    public abstract ApiPath apiPath();
    public abstract DocInbox inbox();
    public abstract DocOutbox outbox();
    public abstract DocInner inner();
    public abstract Tasks task();
}
