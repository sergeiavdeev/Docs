package com.avdeev.docs.ui.task.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.avdeev.docs.R;
import com.avdeev.docs.core.Task;
import com.avdeev.docs.core.User;
import com.avdeev.docs.ui.ext.TaskListAdapter;

public class TaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task);

        Task task = (Task)getIntent().getExtras().getSerializable("task");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Задача");
        actionBar.setSubtitle(task.getType() + " №" + task.getNumber() + " от " + User.dateFromLong(task.getDate()));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        TaskDetailViewModel taskViewModel = ViewModelProviders.of(this).get(TaskDetailViewModel.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
