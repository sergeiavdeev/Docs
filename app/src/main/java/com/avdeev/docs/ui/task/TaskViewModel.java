package com.avdeev.docs.ui.task;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.avdeev.docs.R;
import com.avdeev.docs.core.AppUser;
import com.avdeev.docs.core.network.pojo.BaseDocument;
import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.database.DocDatabase;
import com.avdeev.docs.core.database.dao.Tasks;
import com.avdeev.docs.core.database.entity.Task;
import com.avdeev.docs.core.database.entity.TaskWithFiles;
import com.avdeev.docs.core.network.NetworkService;
import com.avdeev.docs.core.network.pojo.TasksResponse;
import com.avdeev.docs.ui.listAdapters.BaseAdapter;
import com.avdeev.docs.ui.listAdapters.BasePagedAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskViewModel extends DocAppModel {

    private Tasks tasksDao;
    public LiveData<PagedList<Task>> taskList;
    TaskAdapter taskAdapter;

    public TaskViewModel(Application app) {
        super(app);

        this.tasksDao = DocDatabase.getInstance().task();
        taskList = new LivePagedListBuilder<>(tasksDao.taskByDate(""), 50).build();
        taskAdapter = new TaskAdapter(app.getBaseContext());
    }

    public TaskAdapter getTaskAdapter() {
        return taskAdapter;
    }

    public void search(LifecycleOwner lifecycleOwner, String search) {
        taskList.removeObservers(lifecycleOwner);
        taskList = new LivePagedListBuilder<>(tasksDao.taskByDate(search), 50).build();
    }

    public void updateTasksFromNetwork() {
        setWait(true);

        NetworkService.getInstance(AppUser.getApiUrl())
                .setAuthKey(AppUser.getKey())
                .setPasswordHash(AppUser.getHash())
                .getApi()
                .getTasks()
                .enqueue(new Callback<TasksResponse>() {
                    @Override
                    public void onResponse(Call<TasksResponse> call, Response<TasksResponse> response) {

                        List<com.avdeev.docs.core.network.pojo.Task> tasks = response.body().tasks;

                        if (tasks != null) {

                            for (com.avdeev.docs.core.network.pojo.Task task:tasks) {

                                TaskWithFiles taskWithFiles = TaskWithFiles.create(task);

                                DocDatabase db = DocDatabase.getInstance();
                                DocDatabase.executor.execute(() -> {
                                    db.task().add(taskWithFiles);
                                });
                            }
                        }
                        setWait(false);
                    }

                    @Override
                    public void onFailure(Call<TasksResponse> call, Throwable t) {
                        setWait(false);
                    }
                });
    }

    public class TaskAdapter extends BasePagedAdapter<Task> {

        @Override
        protected BaseHolder createHolder(View view) {
            return new TaskViewHolder(view);
        }

        @Override
        protected int getLayoutId() {
            return R.layout.task_list_row;
        }

        protected TaskAdapter(Context context) {
            super(context, DIFF);
        }

        protected class TaskViewHolder extends BaseHolder {

            private TextView title;
            private TextView author;
            private TextView date;
            private TextView date_due;

            public TaskViewHolder(View view) {
                super(view);

                title = itemView.findViewById(R.id.title);
                author = itemView.findViewById(R.id.author);
                date = itemView.findViewById(R.id.date);
                date_due = itemView.findViewById(R.id.date_due);
            }

            @Override
            protected void bind(Task task) {
                title.setText(task.title);

                long lDate = task.date;
                String sNumber = task.number;

                if (lDate > 0 && sNumber.length() > 0) {

                    date.setText("№" + sNumber + " от " + BaseDocument.dateFromLong(task.date));
                    date.setVisibility(View.VISIBLE);
                } else {
                    date.setText("");
                    date.setVisibility(View.GONE);
                }

                author.setText(task.author);

                long dateDue = task.dateDue;

                if (dateDue > 0) {
                    date_due.setText(BaseDocument.dateFromLong(task.dateDue));
                } else {
                    date_due.setText("");
                }
            }
        }
    }

    private static DiffUtil.ItemCallback<Task> DIFF = new DiffUtil.ItemCallback<Task>() {

        @Override
        public boolean areItemsTheSame(Task oldTask, Task newTask) {
            return areContentsTheSame(oldTask, newTask);
        }

        @Override
        public boolean areContentsTheSame(Task oldTask, Task newTask) {
            return areContentsTheSame(oldTask, newTask);
        }
    };
}