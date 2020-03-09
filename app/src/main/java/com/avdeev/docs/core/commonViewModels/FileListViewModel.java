package com.avdeev.docs.core.commonViewModels;

import android.app.Application;
import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avdeev.docs.R;
import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.database.entity.File;
import com.avdeev.docs.core.database.entity.Task;
import com.avdeev.docs.core.network.NetworkService;
import com.avdeev.docs.core.interfaces.ItemClickListener;
import com.avdeev.docs.ui.listAdapters.FileListAdapter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileListViewModel extends DocAppModel {

    private MutableLiveData<FileListAdapter> fileListAdapter;
    private MutableLiveData<String> filesTitle;
    private MutableLiveData<Integer> filesCount;

    public FileListViewModel(Application app) {
        super(app);

        fileListAdapter = new MutableLiveData<>();
        filesTitle = new MutableLiveData<>("");
        filesCount = new MutableLiveData<>(0);
    }

    public LiveData<FileListAdapter> getFileListAdapter() {
        return fileListAdapter;
    }

    public LiveData<String> getFilesTitle() {
        return filesTitle;
    }

    public LiveData<Integer> getFilesCount() {
        return filesCount;
    }

    public void init(List<File> files, ItemClickListener itemClickListener) {

        updateFiles(files);
        FileListAdapter adapter = new FileListAdapter(getApplication().getApplicationContext(), files);
        adapter.setOnItemClickListener(itemClickListener);
        filesTitle.setValue(getFilesTitle(files));
        filesCount.setValue(files.size());
        fileListAdapter.setValue(adapter);
    }

    public void downloadFile(final File appFile) {

        appFile.setDownloading(true);
        final FileListAdapter adapter = fileListAdapter.getValue();
        adapter.notifyDataSetChanged();
        Context context = getContext();
        String fileName = appFile.id + "." + appFile.type;

        NetworkService.getInstance().getApi()
                .getFile(appFile.id)
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

    public void updateFiles(List<File> files) {

        Context context = getApplication().getApplicationContext();

        for (int i = 0; i < files.size(); i++) {

            File file = files.get(i);
            String fileName = file.id + "." + file.type;

            java.io.File jFile = new java.io.File(context.getFilesDir(), fileName);

            file.setExist(jFile.exists());
        }
    }

    private String getFilesTitle(List<File> files) {

        String title = getContext().getString(R.string.title_files) + " (" + files.size()  + ")";
        return title;
    }
}
