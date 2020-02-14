package com.avdeev.docs.ui.docIn;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.avdeev.docs.R;
import com.avdeev.docs.core.DocFragment;
import com.avdeev.docs.core.Document;
import com.avdeev.docs.core.interfaces.ItemClickListener;
import com.avdeev.docs.ui.docDetail.DocDetailActivity;
import com.avdeev.docs.ui.listAdapters.DocListAdapter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DocInFragment extends DocFragment {

    private DocInViewModel docInViewModel;
    private DocListAdapter listAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        Application app = getActivity().getApplication();

        docInViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(app).create(DocInViewModel.class);

        final View root = inflater.inflate(R.layout.common_list_layout, container, false);

        final RecyclerView listView = root.findViewById(R.id.view_list);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        //final ProgressBar progressBar = root.findViewById(R.id.progress_bar);
        final SwipeRefreshLayout refreshLayout = root.findViewById(R.id.refresh);

        docInViewModel.getDocList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Document>>() {
            @Override
            public void onChanged(ArrayList<Document> documents) {

                listAdapter = new DocListAdapter(getContext(), new ArrayList<Object>(documents));
                listAdapter.setOnItemClickListener(createClickListener());
                listView.setAdapter(listAdapter);
            }
        });

        docInViewModel.isWaiting().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean wait) {

                refreshLayout.setRefreshing(wait);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                docInViewModel.updateList();
            }
        });

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Document doc = (Document) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), DocDetailActivity.class);
                intent.putExtra("id", doc);
                intent.putExtra("type", "inbox");
                intent.putExtra("caption", "Входящие");
                startActivity(intent);
            }
        });*/

        docInViewModel.getList();

        return root;
    }

    @Override
    public void onSearch(String searchText) {

        if (listAdapter != null) {
            listAdapter.getFilter().filter(searchText);
        }
    }

    @NotNull
    @Contract(value = " -> new", pure = true)
    private ItemClickListener createClickListener() {

        return new ItemClickListener() {
            @Override
            public void onItemClick(Object object) {

                Intent intent = new Intent(getActivity(), DocDetailActivity.class);
                intent.putExtra("id", (Document)object);
                intent.putExtra("type", "inbox");
                intent.putExtra("caption", "Входящие");
                startActivity(intent);

            }
        };
    }
}