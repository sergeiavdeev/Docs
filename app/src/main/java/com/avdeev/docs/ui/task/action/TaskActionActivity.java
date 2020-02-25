package com.avdeev.docs.ui.task.action;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.avdeev.docs.R;
import com.avdeev.docs.core.Task;
import com.avdeev.docs.core.User;

import org.jetbrains.annotations.NotNull;

import com.avdeev.docs.databinding.ActivityTaskActionBinding;

public class TaskActionActivity extends AppCompatActivity {

    private TaskActionViewModel taskActionViewModel;
    private String action;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_task_action);

        Task task = (Task)getIntent().getExtras().getSerializable("task");
        action = getIntent().getStringExtra("action");

        initActionBar(task);

        taskActionViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication()).create(TaskActionViewModel.class)
                .setTask(task);


        ActivityTaskActionBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_task_action);
        binding.setTaskActionViewModel(taskActionViewModel);
        binding.setLifecycleOwner(this);

        taskActionViewModel.getActionComplete().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean complete) {

                if (complete) {
                    finish();
                }
            }
        });
    }

    private void initActionBar(Task task) {

        //Ознакомление Утвеждение Рассмотрение Исполнение
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

        //Toast.makeText(this, "Отправили согласование", Toast.LENGTH_LONG).show();
        taskActionViewModel.postTaskAction(action);
    }
}
