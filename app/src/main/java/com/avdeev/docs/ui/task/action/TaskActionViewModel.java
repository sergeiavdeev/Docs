package com.avdeev.docs.ui.task.action;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avdeev.docs.core.DocAppModel;

public class TaskActionViewModel extends DocAppModel {

    MutableLiveData<Boolean> actionComplete;

    public TaskActionViewModel(Application app) {
        super(app);
        actionComplete = new MutableLiveData<>();
        actionComplete.setValue(false);
    }

    public LiveData<Boolean> getActionComplete() {
        return actionComplete;
    }

    public void postTaskAction() {

        actionComplete.setValue(true);
    }
}
