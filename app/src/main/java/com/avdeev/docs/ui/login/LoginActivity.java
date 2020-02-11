package com.avdeev.docs.ui.login;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.avdeev.docs.R;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private EditText textLogin;
    private EditText textPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        final ProgressBar progressBar = findViewById(R.id.progress_bar);
        textLogin = findViewById(R.id.edit_login);
        textPassword = findViewById(R.id.edit_password);
        final Button btn = findViewById(R.id.button_login);
        final TextView textError = findViewById(R.id.text_error);

        loginViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(LoginViewModel.class);

        loginViewModel.isAuth().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean auth) {

                if (auth) {
                    finish();
                }

            }
        });

        loginViewModel.isAuthError().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean error) {

                textError.setVisibility(error ? View.VISIBLE : View.INVISIBLE);
            }
        });

        loginViewModel.isWaiting().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean wait) {

                int visible = (wait ? View.VISIBLE : View.INVISIBLE);

                progressBar.setVisibility(visible);
                btn.setClickable(!wait);
                textLogin.setEnabled(!wait);
                textPassword.setEnabled(!wait);
            }
        });

        //btn.setClickable(false);
        //textLogin.setEnabled(false);
        //textPassword.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.login_menu, menu);

        return true;
    }

    public void onClickSettings(MenuItem item) {

        Toast.makeText(this, "Натройки", Toast.LENGTH_LONG).show();
    }

    public void onClickAuth(View view) {
        //Toast.makeText(this, "Вход", Toast.LENGTH_LONG).show();
        loginViewModel.auth(textLogin.getText().toString(), textPassword.getText().toString());
    }
}
