package com.avdeev.docs.ui.task.detail;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.File;
import com.avdeev.docs.core.Task;

public class TaskDetailViewModel extends DocAppModel {

    private MutableLiveData<Task> mTask;
    private MutableLiveData<Boolean> mFilesVisible;
    private MutableLiveData<String> mFileName;

    public TaskDetailViewModel(Application app) {
        super(app);

        mTask = new MutableLiveData<>();
        mFilesVisible = new MutableLiveData<>();
        mFileName = new MutableLiveData<>();
        mFilesVisible.setValue(false);
        mFileName.setValue("");
    }

    public LiveData<Task> getTask() {
        return mTask;
    }

    public LiveData<String> getFileName() {
        return mFileName;
    }

    public void setTask(Task task) {
        mTask.setValue(task);
    }

    public LiveData<Boolean>getFilesVisible() {
        return mFilesVisible;
    }

    public void changeFileVisible() {

        boolean visible = mFilesVisible.getValue();
        mFilesVisible.setValue(!visible);
    }

    public void LoadFile(final File file) {

        wait.setValue(true);
        file.setWait(true);

        Task task = mTask.getValue();
        mTask.setValue(task);

        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {

                String fileName = "";
                try {
                    fileName = user.getFile(file);
                    file.setDownload(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return fileName;
            }

            @Override
            protected void onPostExecute(Object fileName) {

                wait.setValue(false);
                file.setWait(false);
                Task task = mTask.getValue();
                mTask.setValue(task);
                mFileName.setValue((String)fileName);
            }
        }.execute();
    }
}
