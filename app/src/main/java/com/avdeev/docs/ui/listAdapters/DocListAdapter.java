package com.avdeev.docs.ui.listAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.avdeev.docs.R;
import com.avdeev.docs.core.Document;

import java.util.ArrayList;

public class DocListAdapter extends ArrayAdapter<Document> implements Filterable {

    private final Context context;
    private ArrayList<Document> documents;
    private DocFilter filter;
    private ArrayList<Document> filterList;

    private LayoutInflater inflater;

    @Nullable
    @Override
    public Document getItem(int position) {

        return documents.get(position);
    }

    @Override
    public int getCount() {
        return documents.size();
    }

    public DocListAdapter(Context context, ArrayList<Document> values) {
        super(context, R.layout.doc_list_row, values);

        this.context = context;
        this.documents = values;
        filterList = values;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.doc_list_row, parent, false);

            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.nameView.setText(documents.get(position).getTitle());
        viewHolder.authorView.setText(documents.get(position).getAuthor());

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {

        if (filter == null) {
            filter = new DocFilter();
        }

        return filter;
    }

    private class ViewHolder {
        final TextView nameView;
        final TextView authorView;
        ViewHolder(View view){
            nameView = view.findViewById(R.id.list_row_name);
            authorView = view.findViewById(R.id.list_row_author);
        }
    }

    class DocFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence search) {

            FilterResults results = new FilterResults();

            if (search != null && search.length() > 0) {

                search = search.toString().toUpperCase();

                ArrayList<Document> filters = new ArrayList<Document>();

                for (int i = 0; i < filterList.size(); i++) {

                    if (filterList.get(i).getTitle().toUpperCase().contains(search) || filterList.get(i).getAuthor().toUpperCase().contains(search)) {
                        filters.add(new Document(filterList.get(i)));
                    }
                }

                results.count = filters.size();
                results.values = filters;

            } else {

                results.count = filterList.size();
                results.values = filterList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            documents = (ArrayList<Document>)filterResults.values;
            notifyDataSetChanged();
        }
    }
}



