package com.avdeev.docs.ui.docOut;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.avdeev.docs.R;
import com.avdeev.docs.core.DocFragment;
import com.avdeev.docs.core.Document;
import com.avdeev.docs.ui.docDetail.DocDetailActivity;
import com.avdeev.docs.ui.listAdapters.DocListAdapter;

import java.util.ArrayList;

public class DocOutFragment extends DocFragment {

    private DocOutViewModel docOutViewModel;
    private DocListAdapter listAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        Application app = getActivity().getApplication();

        docOutViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(app).create(DocOutViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_doc_out, container, false);

        final ListView listView = root.findViewById(R.id.doc_list);
        //final ProgressBar progressBar = root.findViewById(R.id.progress_bar);
        final SwipeRefreshLayout refreshLayout = root.findViewById(R.id.refresh);

        docOutViewModel.getDocList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Document>>() {
            @Override
            public void onChanged(ArrayList<Document> documents) {

                listAdapter = new DocListAdapter(getContext(), documents);
                listView.setAdapter(listAdapter);
            }
        });

        docOutViewModel.isWaiting().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean wait) {

                refreshLayout.setRefreshing(wait);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                docOutViewModel.updateList();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Document doc = (Document) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), DocDetailActivity.class);
                intent.putExtra("id", doc);
                intent.putExtra("type", "outbox");
                intent.putExtra("caption", "Исходящие");
                startActivity(intent);
            }
        });

        docOutViewModel.getList();

        return root;
    }

    @Override
    public void onSearch(String searchText) {

        if (listAdapter != null) {
            listAdapter.getFilter().filter(searchText);
        }
    }
}