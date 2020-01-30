package com.avdeev.docs.ui.docDetail;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.avdeev.docs.R;

public class DocDetailActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_doc_detail);

        Bundle args = getIntent().getExtras();
        String id = args.get("id").toString();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
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
