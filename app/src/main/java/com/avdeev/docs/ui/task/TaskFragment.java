package com.avdeev.docs.ui.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
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

import java.util.ArrayList;

public class TaskFragment extends DocFragment {

    private TaskViewModel taskViewModel;

    private ItemClickListener itemClickListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        taskViewModel =
                ViewModelProviders.of(this).get(TaskViewModel.class);
        View root = inflater.inflate(R.layout.fragment_task, container, false);

        final RecyclerView listView = root.findViewById(R.id.task_list);
        final SwipeRefreshLayout refreshLayout = root.findViewById(R.id.refresh);

        itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(Object task) {

                Intent intent = new Intent(getActivity(), TaskActivity.class);
                intent.putExtra("task", (Task)task);
                intent.putExtra("type", "inbox");
                intent.putExtra("caption", "Входящие");
                startActivity(intent);

            }
        };
        //listView.getItemAnimator().set
        listView.setLayoutManager(new LinearLayoutManager(getContext()));

        TaskListAdapter adapter = new TaskListAdapter(getContext(), new ArrayList<Task>());
        adapter.setOnItemClickListener(itemClickListener);
        listView.setAdapter(adapter);

        taskViewModel.isAuth().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean auth) {

                if (!auth) {

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        taskViewModel.getTaskList().observe(this, new Observer<ArrayList<Task>>() {
            @Override
            public void onChanged(ArrayList<Task> tasks) {

                TaskListAdapter adapter = new TaskListAdapter(getContext(), tasks);
                adapter.setOnItemClickListener(itemClickListener);
                listView.setAdapter(adapter);
            }
        });

        taskViewModel.isWaiting().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean wait) {

                refreshLayout.setRefreshing(wait);
            }
        });

        taskViewModel.getSearchText().observe(this, new Observer<String>() {
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
    public void onSearch(String searchText) {

        taskViewModel.search(searchText);
    }
}