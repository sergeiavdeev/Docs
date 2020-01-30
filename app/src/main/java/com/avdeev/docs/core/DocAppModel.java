package com.avdeev.docs.core;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avdeev.docs.AppDoc;

public class DocAppModel extends AndroidViewModel {

    protected MutableLiveData<Boolean> isAuth;
    protected MutableLiveData<Boolean> wait;

    protected User user;


    public DocAppModel(Application app) {
        super(app);

        user = ((AppDoc)app).getUser();
        isAuth = new MutableLiveData<>();
        wait = new MutableLiveData<>();
        wait.setValue(false);
        isAuth.setValue(user.isAuth());
    }

    public LiveData<Boolean>isAuth() {

        isAuth.setValue(user.isAuth());

        return isAuth;
    }

    public LiveData<Boolean>isWaiting() {

        return wait;
    }
}
