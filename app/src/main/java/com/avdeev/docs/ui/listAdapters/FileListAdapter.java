package com.avdeev.docs.ui.listAdapters;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.avdeev.docs.R;
import com.avdeev.docs.core.database.entity.File;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FileListAdapter extends BaseAdapter<File> {

    public FileListAdapter(@NotNull Context context, List<File> files) {
        super(context, files);
    }

    @Override
    protected BaseHolder createHolder(View view) {
        return new FileHolder(view);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.file_list_row;
    }

    @Override
    protected File copyObject(File file) {
        return File.create(file);
    }

    protected class FileHolder extends BaseHolder {

        private ImageView fileIcon;
        private TextView fileName;
        private TextView fileSize;
        private ImageView downloadIcon;

        public FileHolder(View itemView) {
            super(itemView);

            fileIcon = itemView.findViewById(R.id.file_type_icon);
            fileName = itemView.findViewById(R.id.text_file_name);
            fileSize = itemView.findViewById(R.id.text_file_size);

            downloadIcon = itemView.findViewById(R.id.file_download_icon);

            itemView.setOnClickListener(this);
        }

        @Override
        void bind(@NotNull File file) {

            fileName.setText(file.name);
            fileSize.setText(Long.toString(file.size) + " KB");
            fileIcon.setImageResource(getFileIcon(file.type));

            if (file.isExist()) {
                downloadIcon.setImageResource(R.drawable.ic_file_view_black_24dp);
            } else {
                downloadIcon.setImageResource(R.drawable.a_ic_file_download);
                if (file.isDownloading()) {
                    Drawable drawer = downloadIcon.getDrawable();
                    ((Animatable)drawer).start();
                } else {
                    Drawable drawer = downloadIcon.getDrawable();
                    ((Animatable)drawer).stop();
                }
            }
        }

        private int getFileIcon(@NotNull String type) {

            int result = R.mipmap.ic_txt;

            if (type.equals("xls") || type.equals("xlsx")) {
                result = R.mipmap.ic_xls;
            } else if (type.equals("doc") || type.equals("docx")) {
                result = R.mipmap.ic_doc;
            } else if (type.equals("pdf")) {
                result = R.mipmap.ic_pdf;
            } else if (type.equals("jpg")) {
                result = R.mipmap.ic_jpg;
            } else if (type.equals("png")) {
                result = R.mipmap.ic_png;
            } else if (type.equals("csv")) {
                result = R.mipmap.ic_csv;
            } else if (type.equals("png")) {
                result = R.mipmap.ic_png;
            } else if (type.equals("html") || type.equals("htm")) {
                result = R.mipmap.ic_html;
            }

            return result;
        }
    }
}