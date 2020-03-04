package com.avdeev.docs.ui.task.detail;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avdeev.docs.BuildConfig;
import com.avdeev.docs.R;
import com.avdeev.docs.core.database.entity.Task;
import com.avdeev.docs.core.database.entity.TaskWithFiles;
import com.avdeev.docs.core.network.pojo.AppFile;
import com.avdeev.docs.core.network.pojo.BaseDocument;

import com.avdeev.docs.core.commonViewModels.FileListViewModel;
import com.avdeev.docs.core.interfaces.ItemClickListener;
import com.avdeev.docs.core.network.pojo.TaskActionRequest;
import com.avdeev.docs.databinding.ActivityTaskBinding;
import com.avdeev.docs.ui.action.ActionsActivity;
import com.avdeev.docs.ui.listAdapters.FileListAdapter;
import com.avdeev.docs.ui.task.action.TaskActionActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@BindingMethods({
        @BindingMethod(type = android.widget.ImageView.class,
                attribute = "app:srcCompat",
                method = "setImageDrawable") })

public class TaskActivity extends AppCompatActivity {

    private TaskDetailViewModel taskViewModel;
    private FileListViewModel fileListViewModel;
    private TaskWithFiles taskWithFiles;
    Animation fab_clock, fab_anticlock, fab_open, fab_close;
    FloatingActionButton fab, fab_history, fab_aply, fab_cancel;
    TextView fabTextApply, fabTextCancel, fabTextHistory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getBaseContext();

        taskWithFiles = (TaskWithFiles) getIntent().getExtras().getSerializable("task");

        initActionBar(taskWithFiles.task);

        taskViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication()).create(TaskDetailViewModel.class);
        ActivityTaskBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_task);
        binding.setTaskViewModel(taskViewModel);
        binding.setLifecycleOwner(this);

        fileListViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication()).create(FileListViewModel.class);

        fab = findViewById(R.id.floatingActionButton);
        fab_history = findViewById(R.id.floatingActionHistory);
        fab_aply = findViewById(R.id.floatingActionHistory);
        fab_aply = findViewById(R.id.floatingActionAply);
        fab_cancel = findViewById(R.id.floatingActionCancel);
        fabTextApply = findViewById(R.id.text_apply);
        fabTextCancel = findViewById(R.id.text_cancel);
        fabTextHistory = findViewById(R.id.text_history);

        fab_history.setClickable(false);
        fab_aply.setClickable(false);
        fab_cancel.setClickable(false);

        fab_clock = AnimationUtils.loadAnimation(this, R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(this, R.anim.fab_rotate_anticlock);
        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        final RecyclerView fileList = findViewById(R.id.list_file);

        fileList.setLayoutManager(new LinearLayoutManager(context));

        taskViewModel.isFabOpen().observe(this, (Boolean visible) -> {
            if (visible) {
                openFab();
            } else {
                closeFab();
            }
        });

        fileListViewModel.getFileListAdapter().observe(this, (FileListAdapter fileListAdapter) -> {
            fileList.setAdapter(fileListAdapter);
        });

        taskViewModel.setTask(taskWithFiles.task);
        fileListViewModel.init(AppFile.createList(taskWithFiles.files), createClickListener());
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

    public void onFilesClick(View view) {

        taskViewModel.changeFileVisible();
    }

    public void onHistoryClick(View view) {

        Task task = taskViewModel.getTask().getValue();

        Intent intent = new Intent(this, ActionsActivity.class);
        intent.putExtra("id", task.id);
        intent.putExtra("type", "inbox");
        intent.putExtra("caption", "История");
        intent.putExtra("request", "history");
        intent.putExtra("task", true);
        startActivity(intent);
    }

    @NotNull
    @Contract(value = " -> new", pure = true)
    private ItemClickListener createClickListener() {
        return (Object object) -> {

            AppFile appFile = (AppFile)object;
            if (!appFile.isExitst()) {
                fileListViewModel.downloadFile(appFile);
            } else {
                previewFile(appFile);
            }
        };
    }

    private void previewFile(@NotNull AppFile appFile) {

        String fileName = appFile.getId() + "." + appFile.getType();

        java.io.File oFile = new java.io.File(getApplicationContext().getFilesDir(), fileName);
        oFile.setReadable(true, false);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(getFileUri(oFile));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PackageManager manager = getPackageManager();
        if (intent.resolveActivity(manager) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Просмотр данного типа файлов не поддерживается", Toast.LENGTH_LONG).show();
        }
    }

    private Uri getFileUri(java.io.File file) {

        return FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, file);
    }

    private void initActionBar(Task task) {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Задача");
        actionBar.setSubtitle(task.type + " №" + task.number + " от " + BaseDocument.dateFromLong(task.date));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    public void onActionClick(View view) {
        taskViewModel.changeFabOpen();
    }

    public void onApplyClick(View view) {
        Intent intent = new Intent(this, TaskActionActivity.class);
        intent.putExtra("task", taskWithFiles.task);
        intent.putExtra("action", TaskActionRequest.ACTION_COMMENTS);
        startActivity(intent);
    }

    public void onCancelClick(View view) {
        Intent intent = new Intent(this, TaskActionActivity.class);
        intent.putExtra("task", taskWithFiles.task);
        intent.putExtra("action", TaskActionRequest.ACTION_NO);
        startActivity(intent);
    }

    private void openFab() {
        fab.startAnimation(fab_clock);
        fab_history.startAnimation(fab_open);
        fab_history.setClickable(true);
        fab_aply.startAnimation(fab_open);
        fab_aply.setClickable(true);
        if (taskWithFiles.task.type.equals("Утверждение")) {
            fab_cancel.startAnimation(fab_open);
            fab_cancel.setClickable(true);
        }

        //fabTextHistory.setVisibility(View.VISIBLE);
        //fabTextApply.setVisibility(View.VISIBLE);
        //fabTextCancel.setVisibility(View.VISIBLE);
    }

    private void closeFab() {

        if(fab_history.isClickable()) {
            fab.startAnimation(fab_anticlock);
            fab_history.startAnimation(fab_close);
            fab_history.setClickable(false);
            fab_aply.startAnimation(fab_close);
            fab_aply.setClickable(false);
            if (taskWithFiles.task.type.equals("Утверждение")) {
                fab_cancel.startAnimation(fab_close);
                fab_cancel.setClickable(false);
            }
            //fabTextHistory.setVisibility(View.INVISIBLE);
            //fabTextApply.setVisibility(View.INVISIBLE);
            //fabTextCancel.setVisibility(View.INVISIBLE);
        }
    }
}
