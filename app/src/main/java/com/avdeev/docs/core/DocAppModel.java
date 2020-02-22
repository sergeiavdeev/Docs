package com.avdeev.docs.core;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avdeev.docs.AppDoc;

import java.lang.reflect.Constructor;

public class DocAppModel extends AndroidViewModel {

    protected MutableLiveData<Boolean> isAuth;
    protected MutableLiveData<Boolean> wait;
    protected User user;
    protected int waitCount;

    public DocAppModel(Application app) {
        super(app);

        user = ((AppDoc)app).getUser();
        isAuth = new MutableLiveData<>();
        wait = new MutableLiveData<>();
        wait.setValue(false);
        isAuth.setValue(user.isAuth());
        waitCount = 0;
    }

    public LiveData<Boolean>isAuth() {

        isAuth.setValue(user.isAuth());
        return isAuth;
    }

    public LiveData<Boolean>isWaiting() {
        return wait;
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
