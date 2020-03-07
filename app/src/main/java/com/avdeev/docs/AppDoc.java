package com.avdeev.docs;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.avdeev.docs.core.AppUser;
import com.avdeev.docs.core.database.DocDatabase;
import com.avdeev.docs.core.database.entity.User;
import com.avdeev.docs.core.network.NetworkService;

public class AppDoc extends Application {

    public AppDoc() {

        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DocDatabase.getInstance(getBaseContext());
    }
}
