package com.avdeev.docs.ui.listAdapters;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avdeev.docs.R;
import com.avdeev.docs.core.File;
import com.avdeev.docs.core.interfaces.ItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FileListAdapter extends RecyclerView.Adapter <FileListAdapter.FileHolder> {

    private ArrayList<File> files;
    private LayoutInflater inflater;

    private ItemClickListener itemClickListener;

    public FileListAdapter(@NotNull Context context, ArrayList<File> files) {

        this.files = files;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public FileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.file_list_row, parent, false);
        return new FileHolder(view);

    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    @Override
    public void onBindViewHolder(@NonNull FileHolder holder, int position) {

        File file = files.get(position);
        holder.bind(file);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {

        this.itemClickListener = itemClickListener;
    }

    protected class FileHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

        private void bind(@NotNull File file) {

            fileName.setText(file.getName());
            fileSize.setText(Long.toString(file.getSize()) + " KB");

            fileIcon.setImageResource(getFileIcon(file.getType()));

            if (file.isDownloaded()) {

                downloadIcon.setImageResource(R.drawable.ic_file_view_black_24dp);
            } else {

                downloadIcon.setImageResource(R.drawable.a_ic_file_download);

                if (file.isWait()) {

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

            if (type.equals("xls") || type.equals("xlsx") || type.equals("csv")) {
                result = R.mipmap.ic_xls;
            } else if (type.equals("doc") || type.equals("docx")) {
                result = R.mipmap.ic_doc;
            } else if (type.equals("pdf")) {
                result = R.mipmap.ic_pdf;
            }

            return result;
        }

        @Override
        public void onClick(View view) {

            if (itemClickListener != null) {

                File file = files.get(getAdapterPosition());
                itemClickListener.onItemClick(file);
            }
        }
    }
}