package com.avdeev.docs.ui.start;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.avdeev.docs.MainActivity;
import com.avdeev.docs.R;

public class StartActivity extends AppCompatActivity {

    private StartViewModel startViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        startViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(StartViewModel.class);

        startViewModel.isComplete().observe(this, (Boolean complete) -> {
            if (complete) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        startViewModel.init();
    }
}
