package com.avdeev.docs.ui.ext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avdeev.docs.R;
import com.avdeev.docs.core.File;

import java.util.ArrayList;

public class FileListAdapter extends RecyclerView.Adapter <FileListAdapter.FileHolder> {

    private ArrayList<File> files;
    private LayoutInflater inflater;

    public FileListAdapter(Context context, ArrayList<File> files) {

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

    protected class FileHolder extends RecyclerView.ViewHolder {

        private ImageView fileIcon;
        private TextView fileName;
        private TextView fileSize;

        public FileHolder(View itemView) {
            super(itemView);

            fileIcon = itemView.findViewById(R.id.file_type_icon);
            fileName = itemView.findViewById(R.id.text_file_name);
            fileSize = itemView.findViewById(R.id.text_file_size);
        }

        private void bind(File file) {

            fileName.setText(file.getName());
            fileSize.setText(Long.toString(file.getSize()) + " KB");
        }
    }
}
