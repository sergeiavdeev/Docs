package com.avdeev.docs.ui.listAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avdeev.docs.core.interfaces.ItemClickListener;

import java.util.ArrayList;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter <BaseAdapter.BaseHolder> implements Filterable {

    private ArrayList<T> adapterList;
    private ArrayList<T> initList;
    private LayoutInflater inflater;
    private ItemClickListener itemClickListener;
    private BaseAdapter.BaseFilter filter;

    protected abstract BaseHolder createHolder(View view);
    protected abstract int getLayoutId();
    protected abstract T copyObject(T object);


    public BaseAdapter(Context context, ArrayList<T> list) {

        this.adapterList = new ArrayList<T>(list);
        this.initList = new ArrayList<T>(list);
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
    public void onBindViewHolder(@NonNull BaseAdapter.BaseHolder holder, int position) {

        T object = adapterList.get(position);
        holder.bind(object);
    }

    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    @Override
    public Filter getFilter() {

        if (filter == null) {
            filter = new BaseAdapter.BaseFilter();
        }

        return filter;
    }

    protected boolean findText(T object, CharSequence text) {
        return true;
    }

    protected abstract class BaseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        abstract void bind(T object);

        public BaseHolder(View view) {
            super(view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (itemClickListener != null) {
                T object = adapterList.get(getAdapterPosition());
                itemClickListener.onItemClick(object);
            }
        }
    }

    private class BaseFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            results.count = initList.size();
            results.values = initList;

            if (constraint != null && constraint.length() > 0) {

                ArrayList<T> filterObjects = new ArrayList<T>();

                constraint = constraint.toString().toUpperCase();

                for (int i = 0; i < initList.size(); i++) {

                    T object = initList.get(i);
                    if (findText(object, constraint)) {

                        filterObjects.add(copyObject(object));
                    }
                }

                results.count = filterObjects.size();
                results.values = filterObjects;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            adapterList = (ArrayList<T>)results.values;
            notifyDataSetChanged();
        }
    }
}
