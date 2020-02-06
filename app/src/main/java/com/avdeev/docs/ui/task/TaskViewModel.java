package com.avdeev.docs.ui.task;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.Document;
import com.avdeev.docs.core.Task;

import java.util.ArrayList;

public class TaskViewModel extends DocAppModel {

    private MutableLiveData<ArrayList<Task>> mTaskList;

    public TaskViewModel(Application app) {

        super(app);

        mTaskList = new MutableLiveData<>();
        mTaskList.setValue(new ArrayList<Task>());
    }

    public LiveData<ArrayList<Task>> getTaskList() {
        return mTaskList;
    }

    public void getList() {

        wait.setValue(true);

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                ArrayList<Task> taskList;

                try {
                    taskList = user.getTaskList();
                    if (taskList.size() == 0) {
                        user.updateTaskList();
                        taskList = user.getTaskList();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    taskList = new ArrayList<>();
                }

                return taskList;
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);

                wait.setValue(false);
                mTaskList.setValue((ArrayList<Task>) result);
            }

        }.execute();
    }

    public void updateList() {

        wait.setValue(true);

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                ArrayList<Task> taskList;

                user.updateTaskList();

                try {
                    taskList = user.getTaskList();
                } catch (Exception e) {
                    e.printStackTrace();
                    taskList = new ArrayList<>();
                }

                return taskList;
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);

                wait.setValue(false);
                mTaskList.setValue((ArrayList<Task>) result);
            }

        }.execute();
    }

}