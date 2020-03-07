package com.avdeev.docs.ui.start;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.avdeev.docs.MainActivity;
import com.avdeev.docs.R;
import com.avdeev.docs.core.AppUser;
import com.avdeev.docs.ui.settings.SettingsActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_start);

        StartViewModel startViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(StartViewModel.class);

        startViewModel.isComplete().observe(this, (Boolean complete) -> {
            if (complete) {

                Intent intent;
                if (AppUser.getApiUrl().length() == 0) {
                    intent = new Intent(this, SettingsActivity.class);
                } else {
                    intent = new Intent(this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        });
        startViewModel.init();
    }
}
