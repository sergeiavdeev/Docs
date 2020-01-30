package com.avdeev.docs.ui.task;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avdeev.docs.core.DocAppModel;

public class TaskViewModel extends DocAppModel {

    private MutableLiveData<String> mText;

    public TaskViewModel(Application app) {

        super(app);

        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}