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
    protected AppUser appUser;
    protected int waitCount;

    public DocAppModel(Application app) {
        super(app);

        appUser = ((AppDoc)app).getAppUser();
        auth = new MutableLiveData<>(appUser.isAuth());
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
            wait.setValue(waitCount > 0);
        }
    }

    protected Context getContext() {
        return getApplication().getBaseContext();
    }

    protected abstract class BaseAsyncTask<T> extends AsyncTask {

        protected abstract T process();

        protected abstract void onPostProcess(T object, Context context);

        @Override
        protected T doInBackground(Object[] objects) {
            return process();
        }

        @Override
        protected void onPreExecute() {
            setWait(true);
        }

        @Override
        protected void onPostExecute(Object object) {
            super.onPostExecute(object);
            onPostProcess((T)object, getApplication().getBaseContext());
            setWait(false);
        }
    }
}
