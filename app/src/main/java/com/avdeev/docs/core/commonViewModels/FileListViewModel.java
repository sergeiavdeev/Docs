package com.avdeev.docs.core.commonViewModels;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.network.pojo.File;
import com.avdeev.docs.core.interfaces.ItemClickListener;
import com.avdeev.docs.ui.listAdapters.FileListAdapter;

import java.util.ArrayList;

public class FileListViewModel extends DocAppModel {

    private MutableLiveData<FileListAdapter> fileListAdapter;

    public FileListViewModel(Application app) {
        super(app);

        fileListAdapter = new MutableLiveData<>();
    }

    public LiveData<FileListAdapter> getFileListAdapter() {
        return fileListAdapter;
    }

    public void init(ArrayList<File> files, ItemClickListener itemClickListener) {

        updateFiles(files);
        FileListAdapter adapter = new FileListAdapter(getApplication().getApplicationContext(), files);
        adapter.setOnItemClickListener(itemClickListener);
        fileListAdapter.setValue(adapter);
    }

    public void downloadFile(final File file) {

        file.setWait(true);
        final FileListAdapter adapter = fileListAdapter.getValue();
        adapter.notifyDataSetChanged();

        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {

                String fileName = "";
                try {
                    fileName = appUser.getFile(file);
                    file.setDownloaded(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return fileName;
            }

            @Override
            protected void onPostExecute(Object fileName) {

                file.setWait(false);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    public void updateFiles(ArrayList<File> files) {

        Context context = getApplication().getApplicationContext();

        for (int i = 0; i < files.size(); i++) {

            File file = files.get(i);
            String fileName = file.getId() + "." + file.getType();

            java.io.File jFile = new java.io.File(context.getFilesDir(), fileName);

            file.setDownloaded(jFile.exists());
        }
    }
}
