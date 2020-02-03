package com.avdeev.docs.ui.docDetail;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.format.DateFormat;
import android.view.MenuItem;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avdeev.docs.R;
import com.avdeev.docs.core.Document;
import com.avdeev.docs.ui.ext.FileListAdapter;

import java.util.Calendar;
import java.util.Locale;

public class DocDetailActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_doc_detail);

        final Document doc = (Document)getIntent().getExtras().getSerializable("id");

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(doc.getUpdated_at() * 1000L);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getIntent().getStringExtra("caption"));
        actionBar.setSubtitle(doc.getType() + " №" + doc.getNumber() + " от " + DateFormat.format("dd.MM.yyyy", calendar));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        DocDetailViewModel model = ViewModelProviders.of(this).get(DocDetailViewModel.class);
        model.setDocument(doc);

        final TextView title = findViewById(R.id.title);
        final TextView status = findViewById(R.id.status);
        final TextView description = findViewById(R.id.description);
        final TextView author = findViewById(R.id.author);
        final TextView recipient = findViewById(R.id.recipient);
        final TextView signer = findViewById(R.id.signer);
        final RecyclerView fileList = findViewById(R.id.list_file);

        fileList.setLayoutManager(new LinearLayoutManager(this));

        model.getDocument().observe(this, new Observer<Document>() {
            @Override
            public void onChanged(Document document) {

                title.setText(document.getTitle());
                status.setText(document.getStatus());
                description.setText(document.getDescription());
                author.setText(document.getAuthor());
                recipient.setText(document.getRecipient());
                signer.setText(document.getSigner());
                fileList.setAdapter(new FileListAdapter(getBaseContext(), document.getFiles()));
            }
        });

        model.updateDocument(getIntent().getStringExtra("type"));
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
