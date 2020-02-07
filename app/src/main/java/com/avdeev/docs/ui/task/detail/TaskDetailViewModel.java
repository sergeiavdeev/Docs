package com.avdeev.docs.ui.task.detail;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.Task;

public class TaskDetailViewModel extends DocAppModel {

    private MutableLiveData<Task> mTask;

    public TaskDetailViewModel(Application app) {
        super(app);

        mTask = new MutableLiveData<>();
    }

    public LiveData<Task> getTask() {
        return mTask;
    }

    public void setTask(Task task) {
        mTask.setValue(task);
    }
}
