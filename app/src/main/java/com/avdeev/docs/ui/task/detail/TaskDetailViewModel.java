package com.avdeev.docs.ui.task.detail;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.database.entity.Task;


public class TaskDetailViewModel extends DocAppModel {

    private MutableLiveData<Task> task;
    private MutableLiveData<Boolean> filesVisible;
    private MutableLiveData<Boolean> fabOpen;

    public TaskDetailViewModel(Application app) {
        super(app);

        task = new MutableLiveData<>();
        filesVisible = new MutableLiveData<>(false);
        fabOpen = new MutableLiveData<>(false);
        filesVisible.setValue(false);
    }

    public LiveData<Task> getTask() {
        return task;
    }


    public void setTask(Task task) {
        this.task.setValue(task);
    }

    public LiveData<Boolean>getFilesVisible() {
        return filesVisible;
    }

    public LiveData<Boolean> isFabOpen() {
        return fabOpen;
    }

    public void changeFileVisible() {

        boolean visible = filesVisible.getValue();
        filesVisible.setValue(!visible);
    }

    public void changeFabOpen() {
        Boolean open = fabOpen.getValue();
        fabOpen.setValue(!open);
    }
}
