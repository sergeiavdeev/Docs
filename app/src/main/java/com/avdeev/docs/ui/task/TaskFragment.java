package com.avdeev.docs.ui.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.avdeev.docs.R;
import com.avdeev.docs.core.DocFragment;
import com.avdeev.docs.ui.login.LoginActivity;

public class TaskFragment extends DocFragment {

    private TaskViewModel taskViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        taskViewModel =
                ViewModelProviders.of(this).get(TaskViewModel.class);
        View root = inflater.inflate(R.layout.fragment_task, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        taskViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        taskViewModel.isAuth().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean auth) {

                if (!auth) {

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        return root;
    }

    @Override
    public void onSearch(String searchText) {

        Toast.makeText(getActivity(), "Task search: " + searchText, Toast.LENGTH_LONG).show();
    }
}