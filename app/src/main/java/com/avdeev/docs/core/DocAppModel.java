package com.avdeev.docs.core;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avdeev.docs.AppDoc;

public class DocAppModel extends AndroidViewModel {

    protected MutableLiveData<Boolean> auth;
    protected MutableLiveData<Boolean> wait;
    protected MutableLiveData<Boolean> error;
    protected MutableLiveData<Boolean> complete;
    protected MutableLiveData<String> errorMessage;
    protected int waitCount;

    public DocAppModel(Application app) {
        super(app);

        auth = new MutableLiveData<>(AppUser.isAuth());
        wait = new MutableLiveData<>(false);
        error = new MutableLiveData<>(false);
        errorMessage = new MutableLiveData<>("");
        complete = new MutableLiveData<>(false);
        waitCount = 0;
    }

    public LiveData<Boolean>isAuth() {

        auth.setValue(AppUser.isAuth());
        return auth;
    }

    public LiveData<Boolean>isWaiting() {
        return wait;
    }

    public LiveData<String> getErrorMessage () {
        return errorMessage;
    }

    public LiveData<Boolean> isComplete() {
        return complete;
    }

    public LiveData<Boolean> isError() {
        return error;
    }

    protected void setWait(boolean isWait) {

        synchronized (this) {
            if (isWait) {
                waitCount ++;
            } else {
                if (waitCount > 0) {
                    waitCount --;
                }
            }
            wait.postValue(waitCount > 0);
        }
    }

    protected Context getContext() {
        return getApplication().getBaseContext();
    }
}
