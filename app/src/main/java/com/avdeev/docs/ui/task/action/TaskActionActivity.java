package com.avdeev.docs.ui.task.action;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.avdeev.docs.R;
import com.avdeev.docs.core.Task;
import com.avdeev.docs.core.User;

import org.jetbrains.annotations.NotNull;

public class TaskActionActivity extends AppCompatActivity {

    TaskActionViewModel taskActionViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_action);

        taskActionViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication()).create(TaskActionViewModel.class);

        Task task = (Task)getIntent().getExtras().getSerializable("task");
        initActionBar(task);
    }

    private void initActionBar(Task task) {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Утвердить");
        actionBar.setSubtitle(task.getType() + " №" + task.getNumber() + " от " + User.dateFromLong(task.getDate()));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void postActionClick(View view) {
        Toast.makeText(this, "Отправили согласование", Toast.LENGTH_LONG).show();

        taskActionViewModel.postTaskAction();
    }
}
