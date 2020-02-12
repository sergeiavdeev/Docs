package com.avdeev.docs.ui.task;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import androidx.annotation.NonNull;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.avdeev.docs.R;
import com.avdeev.docs.core.DocFragment;
import com.avdeev.docs.core.Task;
import com.avdeev.docs.ui.ext.TaskListAdapter;
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

        final RecyclerView listView = root.findViewById(R.id.task_list);
        final SwipeRefreshLayout refreshLayout = root.findViewById(R.id.refresh);

        //listView.getItemAnimator().set
        listView.setLayoutManager(new LinearLayoutManager(getContext()));

        TaskListAdapter adapter = new TaskListAdapter(getContext(), new ArrayList<Task>());
        listView.setAdapter(adapter);
        adapter.setOnItemClickListener(createClickListener());

        taskViewModel.isAuth().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean auth) {

                if (!auth) {

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        taskViewModel.getTaskList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Task>>() {
            @Override
            public void onChanged(ArrayList<Task> tasks) {

                TaskListAdapter adapter = new TaskListAdapter(getContext(), tasks);
                adapter.setOnItemClickListener(createClickListener());
                listView.setAdapter(adapter);
            }
        });

        taskViewModel.isWaiting().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean wait) {

                refreshLayout.setRefreshing(wait);
            }
        });

        taskViewModel.getSearchText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String searchText) {

                refreshLayout.setRefreshing(true);
                TaskListAdapter adapter = (TaskListAdapter) listView.getAdapter();
                adapter.getFilter().filter(searchText);
                refreshLayout.setRefreshing(false);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                taskViewModel.updateList();
            }
        });

        taskViewModel.getList();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        taskViewModel.getList();
    }

    @Override
    public void onSearch(String searchText) {

        taskViewModel.search(searchText);
    }


    @NotNull
    @Contract(value = " -> new", pure = true)
    private ItemClickListener createClickListener() {

        return new ItemClickListener() {
            @Override
            public void onItemClick(Object task) {

                Intent intent = new Intent(getActivity(), TaskActivity.class);
                intent.putExtra("task", (Task)task);
                intent.putExtra("type", "inbox");
                intent.putExtra("caption", "Входящие");
                startActivity(intent);

            }
        };
    }
}