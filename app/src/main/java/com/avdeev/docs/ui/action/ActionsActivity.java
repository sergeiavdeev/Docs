package com.avdeev.docs.ui.action;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.avdeev.docs.R;
import com.avdeev.docs.core.Action;
import com.avdeev.docs.core.ActionRequest;
import com.avdeev.docs.ui.ext.ActionListAdapter;

import java.util.ArrayList;

public class ActionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_action);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getIntent().getStringExtra("caption"));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        ActionsViewModel actionsViewModel = ViewModelProviders.of(this).get(ActionsViewModel.class);

        final RecyclerView actionList = findViewById(R.id.action_list);
        actionList.setLayoutManager(new LinearLayoutManager(this));
        actionList.setAdapter(new ActionListAdapter(getBaseContext(), new ArrayList<Action>()));

        final SwipeRefreshLayout refreshLayout = findViewById(R.id.refresh);

        actionsViewModel.getActions().observe(this, new Observer<ArrayList<Action>>() {
            @Override
            public void onChanged(ArrayList<Action> actions) {
                actionList.setAdapter(new ActionListAdapter(getBaseContext(), actions));
            }
        });

        actionsViewModel.isWaiting().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean wait) {

                refreshLayout.setRefreshing(wait);
            }
        });


        ActionRequest request;

        if (getIntent().getBooleanExtra("task", false)) {

            request = new ActionRequest(
                    getIntent().getStringExtra("request"),
                    getIntent().getStringExtra("type"),
                    getIntent().getStringExtra("id"),
                    true);
        } else {

            request = new ActionRequest(getIntent().getStringExtra("request"),
                    getIntent().getStringExtra("type"),
                    getIntent().getStringExtra("id"));
        }

        actionsViewModel.updateActions(request);
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
