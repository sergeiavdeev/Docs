package com.avdeev.docs;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.avdeev.docs.core.AppUser;
import com.avdeev.docs.core.database.DocDatabase;
import com.avdeev.docs.core.database.entity.User;
import com.avdeev.docs.core.network.NetworkService;

public class AppDoc extends Application {

    private Context context;
    private AppUser appUser;

    public AppDoc() {

        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getBaseContext();

        DocDatabase.getInstance(context);

        appUser = new AppUser(context);
    }

    public AppUser getAppUser() {
        return appUser;
    }

}
