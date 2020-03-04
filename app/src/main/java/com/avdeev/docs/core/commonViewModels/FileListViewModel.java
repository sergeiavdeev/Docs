package com.avdeev.docs.core.commonViewModels;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.database.entity.File;
import com.avdeev.docs.core.network.NetworkService;
import com.avdeev.docs.core.network.pojo.AppFile;
import com.avdeev.docs.core.interfaces.ItemClickListener;
import com.avdeev.docs.ui.listAdapters.FileListAdapter;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileListViewModel extends DocAppModel {

    private MutableLiveData<FileListAdapter> fileListAdapter;

    public FileListViewModel(Application app) {
        super(app);

        fileListAdapter = new MutableLiveData<>();
    }

    public LiveData<FileListAdapter> getFileListAdapter() {
        return fileListAdapter;
    }

    public void init(List<AppFile> files, ItemClickListener itemClickListener) {

        updateFiles(files);
        FileListAdapter adapter = new FileListAdapter(getApplication().getApplicationContext(), files);
        adapter.setOnItemClickListener(itemClickListener);
        fileListAdapter.setValue(adapter);
    }

    public void downloadFile(final AppFile appFile) {

        appFile.setDownloading(true);
        final FileListAdapter adapter = fileListAdapter.getValue();
        adapter.notifyDataSetChanged();
        Context context = getContext();
        String fileName = appFile.getId() + "." + appFile.getType();

        NetworkService.getInstance().getApi()
                .getFile(appFile.getId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        appFile.setDownloading(false);

                        writeFileToDisk(fileName, response.body());
                        appFile.setExist(true);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        appFile.setDownloading(false);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void writeFileToDisk(String fileName, ResponseBody body) {

        Context context = getContext();

        try {
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                inputStream = body.byteStream();
                outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);
                }

                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateFiles(List<AppFile> files) {

        Context context = getApplication().getApplicationContext();

        for (int i = 0; i < files.size(); i++) {

            AppFile file = files.get(i);
            String fileName = file.getId() + "." + file.getType();

            java.io.File jFile = new java.io.File(context.getFilesDir(), fileName);

            file.setExist(jFile.exists());
        }
    }
}
