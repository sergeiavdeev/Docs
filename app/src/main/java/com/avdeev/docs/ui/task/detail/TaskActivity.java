package com.avdeev.docs.ui.task.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
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

        final TextView title = findViewById(R.id.title);
        final TextView type = findViewById(R.id.type);
        final TextView description = findViewById(R.id.description);
        final TextView author = findViewById(R.id.author);
        final TextView executor = findViewById(R.id.executor);
        final TextView date_due = findViewById(R.id.date_due);

        taskViewModel.getTask().observe(this, new Observer<Task>() {
            @Override
            public void onChanged(Task task) {

                title.setText(task.getTitle());
                type.setText(task.getType());
                description.setText(task.getDescription());
                author.setText(task.getAuthor());
                executor.setText(task.getAssignee());
                date_due.setText(User.dateFromLong(task.getDate_due()));
            }
        });

        taskViewModel.setTask(task);
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
