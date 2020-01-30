package com.avdeev.docs;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private SearchView searchView;
    private SearchView.OnQueryTextListener searchListener;
    private MenuItem searchMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_task, R.id.navigation_doc_in, R.id.navigation_doc_out, R.id.navigation_doc_inner)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.user_menu, menu);

        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView)searchMenuItem.getActionView();

        if (searchListener != null) {
            searchView.setOnQueryTextListener(searchListener);
        }

        return true;
    }

    public void onClickExit(MenuItem item) {
        Toast.makeText(this, "Выход", Toast.LENGTH_LONG).show();
    }

    public void setOnQuerySearchListener(SearchView.OnQueryTextListener listener) {

        searchListener = listener;

        if (searchView != null) {
            searchView.setOnQueryTextListener(searchListener);

            searchView.clearFocus();
            searchView.setQuery("", false);
            searchView.setIconified(true);
            searchMenuItem.collapseActionView();
        }
    }
}
