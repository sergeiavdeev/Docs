package com.avdeev.docs.ui.task;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.avdeev.docs.R;
import com.avdeev.docs.core.DocFragment;
import com.avdeev.docs.core.Task;
import com.avdeev.docs.ui.listAdapters.TaskListAdapter;
import com.avdeev.docs.ui.login.LoginActivity;
import com.avdeev.docs.ui.task.detail.TaskActivity;
import com.avdeev.docs.core.interfaces.ItemClickListener;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class TaskFragment extends DocFragment {

    private TaskViewModel taskViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Application app = getActivity().getApplication();

        taskViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(app).create(TaskViewModel.class);
        View root = inflater.inflate(R.layout.fragment_task, container, false);

        final RecyclerView listView = root.findViewById(R.id.view_list);
        final SwipeRefreshLayout refreshLayout = root.findViewById(R.id.refresh);

        listView.setLayoutManager(new LinearLayoutManager(getContext()));

        TaskListAdapter adapter = new TaskListAdapter(getContext(), new ArrayList<Task>());
        listView.setAdapter(adapter);
        adapter.setOnItemClickListener(createClickListener());

        taskViewModel.isAuth().observe(getViewLifecycleOwner(), (Boolean auth) -> {
            if (!auth) {
                startLoginActivity();
                getActivity().finish();
            }
        });

        taskViewModel.getTaskListAdapter().observe(getViewLifecycleOwner(), (TaskListAdapter taskListAdapter)-> {
            taskListAdapter.setOnItemClickListener(createClickListener());
            listView.setAdapter(taskListAdapter);
        });

        taskViewModel.isWaiting().observe(getViewLifecycleOwner(), (Boolean wait) -> {
            refreshLayout.setRefreshing(wait);
        });

        refreshLayout.setOnRefreshListener(()->{
            taskViewModel.updateList();
        });

        taskViewModel.getList();
        return root;
    }

    @Override
    public void onSearch(String searchText) {
        taskViewModel.search(searchText);
    }

    @NotNull
    @Contract(value = " -> new", pure = true)
    private ItemClickListener createClickListener() {

        return (Object task) -> {
            startTaskActivity((Task)task);
        };
    }

    private void startLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    private void startTaskActivity(Task task) {
        Intent intent = new Intent(getActivity(), TaskActivity.class);
        intent.putExtra("task", task);
        startActivity(intent);
    }
}