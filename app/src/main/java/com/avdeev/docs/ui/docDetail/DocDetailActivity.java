package com.avdeev.docs.ui.docDetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avdeev.docs.R;
import com.avdeev.docs.core.Document;
import com.avdeev.docs.ui.action.ActionsActivity;
import com.avdeev.docs.ui.listAdapters.FileListAdapter;

public class DocDetailActivity extends AppCompatActivity {

    private Document doc;
    private String docType;
    DocDetailViewModel docViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_doc_detail_v2);

        doc = (Document)getIntent().getExtras().getSerializable("id");
        docType = getIntent().getStringExtra("type");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getIntent().getStringExtra("caption"));
        actionBar.setSubtitle(doc.getType() + " №" + doc.getNumber() + " от " + doc.dateFromLong(doc.getUpdated_at()));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        docViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication()).create(DocDetailViewModel.class);
        docViewModel.setDocument(doc);

        final TextView title = findViewById(R.id.title);
        final TextView status = findViewById(R.id.status);
        final TextView description = findViewById(R.id.description);
        final TextView author = findViewById(R.id.author);
        final TextView recipient = findViewById(R.id.recipient);
        final TextView signer = findViewById(R.id.signer);
        final TextView number = findViewById(R.id.number);
        final TextView date = findViewById(R.id.date);
        final RecyclerView fileList = findViewById(R.id.list_file);
        final ImageView fileArrow = findViewById(R.id.image_files);
        final ImageView moreArrow = findViewById(R.id.image_more);
        final LinearLayout moreLayout = findViewById(R.id.more_buttons);

        fileList.setLayoutManager(new LinearLayoutManager(this));

        docViewModel.getDocument().observe(this, new Observer<Document>() {
            @Override
            public void onChanged(Document document) {

                title.setText(document.getTitle());
                status.setText(document.getStatus());
                description.setText(document.getDescription());
                author.setText(document.getAuthor());
                recipient.setText(document.getRecipient());
                signer.setText(document.getSigner());
                number.setText(document.getNumber());
                date.setText(document.dateFromLong(document.getDate()));
                fileList.setAdapter(new FileListAdapter(getBaseContext(), document.getFiles()));
            }
        });

        docViewModel.getFileVisible().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean visible) {

                if (visible) {
                    fileList.setVisibility(View.VISIBLE);
                    fileArrow.setImageResource(R.drawable.ic_collapse_up_black_24dp);
                } else {
                    fileList.setVisibility(View.GONE);
                    fileArrow.setImageResource(R.drawable.ic_collapse_down_black_24dp);
                }
            }
        });

        docViewModel.getMoreVisible().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean visible) {

                if (visible) {
                    moreLayout.setVisibility(View.VISIBLE);
                    moreArrow.setImageResource(R.drawable.ic_collapse_up_black_24dp);
                } else {
                    moreLayout.setVisibility(View.GONE);
                    moreArrow.setImageResource(R.drawable.ic_collapse_down_black_24dp);
                }
            }
        });

        docViewModel.updateDocument(docType);
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

    public void onHistoryClick(View view) {

        Intent intent = new Intent(this, ActionsActivity.class);
        intent.putExtra("id", doc.getId());
        intent.putExtra("type", docType);
        intent.putExtra("caption", "История");
        intent.putExtra("request", "history");
        startActivity(intent);
    }

    public void onVisaClick(View view) {

        Intent intent = new Intent(this, ActionsActivity.class);
        intent.putExtra("id", doc.getId());
        intent.putExtra("type", docType);
        intent.putExtra("caption", "Визы");
        intent.putExtra("request", "visas");
        startActivity(intent);
    }

    public void onResolutionClick(View view) {

        Intent intent = new Intent(this, ActionsActivity.class);
        intent.putExtra("id", doc.getId());
        intent.putExtra("type", docType);
        intent.putExtra("caption", "Резолюции");
        intent.putExtra("request", "resolutions");
        startActivity(intent);
    }

    public void onMailerClick(View view) {

        Intent intent = new Intent(this, ActionsActivity.class);
        intent.putExtra("id", doc.getId());
        intent.putExtra("type", docType);
        intent.putExtra("caption", "Рассылка");
        intent.putExtra("request", "mailing");
        startActivity(intent);
    }

    public void onFilesClick(View view) {

        docViewModel.changeFileVisible();
    }

    public void onMoreClick(View view) {

        docViewModel.changeMoreVisible();
    }
}
