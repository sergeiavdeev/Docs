package com.avdeev.docs.ui.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.avdeev.docs.MainActivity;
import com.avdeev.docs.R;
import com.avdeev.docs.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsViewModel settingsViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(SettingsViewModel.class);

        ActivitySettingsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        binding.setSettingsViewModel(settingsViewModel);
        binding.setLifecycleOwner(this);

        settingsViewModel.isComplete().observe(this, (Boolean complete) -> {
            if (complete) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
