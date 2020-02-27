package com.avdeev.docs.ui.task;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.Task;
import com.avdeev.docs.ui.listAdapters.TaskListAdapter;

import java.util.ArrayList;

public class TaskViewModel extends DocAppModel {

    private MutableLiveData<TaskListAdapter> taskListAdapter;
    private MutableLiveData<String> searchText;

    public TaskViewModel(Application app) {

        super(app);

        taskListAdapter = new MutableLiveData<>();
        taskListAdapter.setValue(new TaskListAdapter(getContext(), new ArrayList<>()));
    }

    public LiveData<TaskListAdapter> getTaskListAdapter() {
        return taskListAdapter;
    }


    public void getList() {

        setWait(true);

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                ArrayList<Task> taskList;

                try {
                    taskList = appUser.getTaskList();
                    if (taskList.size() == 0) {
                        appUser.updateTaskList();
                        taskList = appUser.getTaskList();
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
                taskListAdapter.setValue(new TaskListAdapter(getContext(), new ArrayList<>((ArrayList<Task>)result)));
                setWait(false);
            }

        }.execute();
    }

    public void updateList() {

        setWait(true);

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                ArrayList<Task> taskList;

                appUser.updateTaskList();

                try {
                    taskList = appUser.getTaskList();
                } catch (Exception e) {
                    e.printStackTrace();
                    taskList = new ArrayList<>();
                }

                return taskList;
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);
                taskListAdapter.setValue(new TaskListAdapter(getContext(), new ArrayList<>((ArrayList<Task>)result)));
                setWait(false);
            }

        }.execute();
    }

    public void search(String searchText) {

        setWait(true);
        TaskListAdapter adapter = taskListAdapter.getValue();
        adapter.getFilter().filter(searchText);
        setWait(false);
    }
}