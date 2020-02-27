package com.avdeev.docs.ui.login;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avdeev.docs.core.DocAppModel;

public class LoginViewModel extends DocAppModel {

    protected MutableLiveData<Boolean> authError;

    public LoginViewModel(Application app) {
        super(app);
        authError = new MutableLiveData<>();
        authError.setValue(false);
    }

    public LiveData<Boolean>isAuthError() {

        return authError;
    }

    public void auth(String login, String password) {

        final String l = login;
        final String p = password;

        wait.setValue(true);
        authError.setValue(false);

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                return appUser.auth(l, p);
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);

                wait.setValue(false);
                auth.setValue((boolean) result);
                authError.setValue(!(boolean)result);
            }


        }.execute();
    }


}
