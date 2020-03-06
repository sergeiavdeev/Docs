package com.avdeev.docs.ui.docDetail;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avdeev.docs.BuildConfig;
import com.avdeev.docs.R;
import com.avdeev.docs.core.database.entity.File;
import com.avdeev.docs.core.network.pojo.BaseDocument;
import com.avdeev.docs.core.network.pojo.Document;
import com.avdeev.docs.core.commonViewModels.FileListViewModel;
import com.avdeev.docs.core.interfaces.ItemClickListener;
import com.avdeev.docs.databinding.ActivityDocDetailBinding;
import com.avdeev.docs.ui.action.ActionsActivity;
import com.avdeev.docs.ui.listAdapters.FileListAdapter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@BindingMethods({
        @BindingMethod(type = android.widget.ImageView.class,
                attribute = "app:srcCompat",
                method = "setImageDrawable") })

public class DocDetailActivity extends AppCompatActivity {

    private Document doc;
    private String docType;
    DocDetailViewModel docViewModel;
    FileListViewModel fileListViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        doc = (Document)getIntent().getExtras().getSerializable("document");
        docType = getIntent().getStringExtra("type");

        initActionBar();

        docViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication()).create(DocDetailViewModel.class);
        docViewModel.setDocument(doc);

        ActivityDocDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_doc_detail);
        binding.setDocViewModel(docViewModel);
        binding.setLifecycleOwner(this);

        fileListViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication()).create(FileListViewModel.class);


        final RecyclerView fileList = findViewById(R.id.list_file);
        final ImageView moreArrow = findViewById(R.id.image_more);
        final LinearLayout moreLayout = findViewById(R.id.more_buttons);

        fileList.setLayoutManager(new LinearLayoutManager(this));

        docViewModel.getDocument().observe(this, (Document document) -> {
            fileListViewModel.init(document.files, createClickListener());
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

        fileListViewModel.getFileListAdapter().observe(this, (FileListAdapter fileListAdapter) -> {
            fileList.setAdapter(fileListAdapter);
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

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getIntent().getStringExtra("caption"));
        actionBar.setSubtitle(doc.type + " №" + doc.number + " от " + BaseDocument.dateFromLong(doc.updated_at));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    public void onHistoryClick(View view) {

        Intent intent = new Intent(this, ActionsActivity.class);
        intent.putExtra("ownerId", doc.id);
        intent.putExtra("ownerType", docType);
        intent.putExtra("actionType", "history");
        intent.putExtra("caption", "История");
        startActivity(intent);
    }

    public void onVisaClick(View view) {

        Intent intent = new Intent(this, ActionsActivity.class);
        intent.putExtra("ownerId", doc.id);
        intent.putExtra("ownerType", docType);
        intent.putExtra("actionType", "visas");
        intent.putExtra("caption", "Визы");
        startActivity(intent);
    }

    public void onResolutionClick(View view) {

        Intent intent = new Intent(this, ActionsActivity.class);
        intent.putExtra("ownerId", doc.id);
        intent.putExtra("ownerType", docType);
        intent.putExtra("actionType", "resolutions");
        intent.putExtra("caption", "Резолюции");
        startActivity(intent);
    }

    public void onMailerClick(View view) {

        Intent intent = new Intent(this, ActionsActivity.class);
        intent.putExtra("ownerId", doc.id);
        intent.putExtra("ownerType", docType);
        intent.putExtra("actionType", "mailing");
        intent.putExtra("caption", "Рассылки");
        startActivity(intent);
    }

    public void onFilesClick(View view) {

        docViewModel.changeFileVisible();
    }

    public void onMoreClick(View view) {

        docViewModel.changeMoreVisible();
    }

    @NotNull
    @Contract(value = " -> new", pure = true)
    private ItemClickListener createClickListener() {

        return new ItemClickListener<File>() {
            @Override
            public void onItemClick(File file) {

                if (!file.isExist()) {
                    fileListViewModel.downloadFile(file);
                } else {
                    previewFile(file);
                }
            }
        };
    }

    private void previewFile(File appFile) {

        String fileName = appFile.id + "." + appFile.type;

        java.io.File oFile = new java.io.File(getApplicationContext().getFilesDir(), fileName);
        oFile.setReadable(true, false);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(getFileUri(oFile));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //intent.addFlags(Intent.)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PackageManager manager = getPackageManager();
        if (intent.resolveActivity(manager) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Просмотр данного типа файлов не поддерживается", Toast.LENGTH_LONG).show();
        }
    }

    private Uri getFileUri(java.io.File file) {

        return FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, file);
    }
}
