package com.avdeev.docs;

import android.app.Application;
import android.content.Context;

import com.avdeev.docs.core.User;

public class AppDoc extends Application {

    private Context context;
    private User user;

    public AppDoc() {

        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getBaseContext();
        user = new User(context);
    }

    public User getUser() {

        return user;
    }

}
