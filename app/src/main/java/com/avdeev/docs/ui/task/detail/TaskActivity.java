package com.avdeev.docs.ui.task.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avdeev.docs.R;
import com.avdeev.docs.core.Task;
import com.avdeev.docs.core.User;
import com.avdeev.docs.ui.action.ActionsActivity;
import com.avdeev.docs.ui.ext.FileListAdapter;

public class TaskActivity extends AppCompatActivity {

    private TaskDetailViewModel taskViewModel;

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

        taskViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication()).create(TaskDetailViewModel.class);

        final TextView title = findViewById(R.id.title);
        final TextView type = findViewById(R.id.type);
        final TextView description = findViewById(R.id.description);
        final TextView author = findViewById(R.id.author);
        final TextView executor = findViewById(R.id.executor);
        final TextView date_due = findViewById(R.id.date_due);

        final ImageView fileArrow = findViewById(R.id.image_files);
        final ImageView historyArrow = findViewById(R.id.image_history);

        final RecyclerView fileList = findViewById(R.id.list_file);
        fileList.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        fileList.setAdapter(new FileListAdapter(getBaseContext(), task.getFiles()));

        taskViewModel.getTask().observe(this, new Observer<Task>() {
            @Override
            public void onChanged(Task task) {

                title.setText(task.getTitle());
                type.setText(task.getType());
                description.setText(task.getDescription());
                author.setText(task.getAuthor());
                executor.setText(task.getAssignee());
                date_due.setText(User.dateFromLong(task.getDate_due()));

                FileListAdapter fileListAdapter = new FileListAdapter(getBaseContext(), task.getFiles());
                fileList.setAdapter(fileListAdapter);
            }
        });

        taskViewModel.getFilesVisible().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean visible) {

                if (visible) {
                    fileList.setVisibility(View.VISIBLE);
                    fileArrow.setImageResource(R.drawable.ic_collapse_up_black_24dp);
                } else {
                    fileList.setVisibility(View.GONE);
                    fileArrow.setImageResource(R.drawable.ic_collapse_down_black_24dp);
                }
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

    public void onFilesClick(View view) {

        taskViewModel.changeFileVisible();
    }

    public void onHistoryClick(View view) {

        Task task = taskViewModel.getTask().getValue();

        Intent intent = new Intent(this, ActionsActivity.class);
        intent.putExtra("id", task.getId());
        intent.putExtra("type", "inbox");
        intent.putExtra("caption", "История");
        intent.putExtra("request", "history");
        intent.putExtra("task", true);
        startActivity(intent);
    }
}
