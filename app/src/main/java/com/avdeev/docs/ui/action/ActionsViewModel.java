package com.avdeev.docs.ui.action;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avdeev.docs.core.Action;
import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.Document;

import java.util.ArrayList;

public class ActionsViewModel extends DocAppModel {

    private MutableLiveData<ArrayList<Action>> actions;

    public ActionsViewModel(Application app) {
        super(app);

        actions = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Action>> getActions() {

        return actions;
    }

    public void updateActions(final String type, final String id, final String request) {

        wait.setValue(true);

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                return user.getActions(type, id, request);
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);

                wait.setValue(false);
                actions.setValue((ArrayList<Action>)result);
            }

        }.execute();
    }
}
