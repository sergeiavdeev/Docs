package com.avdeev.docs.ui.listAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.avdeev.docs.core.database.entity.Task;
import com.avdeev.docs.core.interfaces.ItemClickListener;

import java.util.ArrayList;

public abstract class BasePagedAdapter<T> extends PagedListAdapter<T, BasePagedAdapter.BaseHolder> {

    private LayoutInflater inflater;
    private ItemClickListener itemClickListener;

    protected abstract BaseHolder createHolder(View view);
    protected abstract int getLayoutId();

    public BasePagedAdapter(Context context, DiffUtil.ItemCallback<T> callback) {
        super(callback);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setOnItemClickListener(ItemClickListener listener) {

        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layoutId = getLayoutId();

        View view = inflater.inflate(layoutId, parent, false);
        return createHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasePagedAdapter.BaseHolder holder, int position) {

        T task = getItem(position);

        if (task != null) {
            holder.bind(task);
        } else {
            holder.clear();
        }
    }

    protected abstract class BaseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected abstract void bind(T object);

        private void clear() {

        };

        public BaseHolder(View view) {
            super(view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (itemClickListener != null) {
                T object = getItem(getAdapterPosition());
                itemClickListener.onItemClick(object);
            }
        }
    }
}
