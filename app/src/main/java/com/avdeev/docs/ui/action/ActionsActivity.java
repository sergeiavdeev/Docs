package com.avdeev.docs.ui.action;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.avdeev.docs.R;
import com.avdeev.docs.core.network.pojo.Action;
import com.avdeev.docs.ui.listAdapters.ActionListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_action);

        String ownerType = getIntent().getStringExtra("ownerType");
        String ownerId = getIntent().getStringExtra("ownerId");
        String actionType = getIntent().getStringExtra("actionType");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getIntent().getStringExtra("caption"));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        ActionsViewModel actionsViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication()).create(ActionsViewModel.class)
                .init(actionType, ownerId, ownerType);

        final RecyclerView actionList = findViewById(R.id.action_list);
        actionList.setLayoutManager(new LinearLayoutManager(this));
        actionList.setAdapter(new ActionListAdapter(getBaseContext(), new ArrayList<Action>()));

        final SwipeRefreshLayout refreshLayout = findViewById(R.id.refresh);

        actionsViewModel.getActions().observe(this, new Observer<List<Action>>() {
            @Override
            public void onChanged(List<Action> actions) {
                actionList.setAdapter(new ActionListAdapter(getBaseContext(), actions));
            }
        });

        actionsViewModel.isWaiting().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean wait) {

                refreshLayout.setRefreshing(wait);
            }
        });

        refreshLayout.setOnRefreshListener(()->{
            actionsViewModel.updateActions();
        });

        actionsViewModel.updateActions();
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
