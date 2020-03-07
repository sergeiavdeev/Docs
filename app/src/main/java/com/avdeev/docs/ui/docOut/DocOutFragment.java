package com.avdeev.docs.ui.docOut;

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
import com.avdeev.docs.core.database.entity.DocumentOutbox;
import com.avdeev.docs.core.network.pojo.Document;
import com.avdeev.docs.core.interfaces.ItemClickListener;
import com.avdeev.docs.ui.docDetail.DocDetailActivity;
import com.avdeev.docs.ui.listAdapters.DocOutboxListAdapter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class DocOutFragment extends DocFragment {

    private DocOutViewModel docOutViewModel;
    private DocOutboxListAdapter listAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        Application app = getActivity().getApplication();

        docOutViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(app).create(DocOutViewModel.class);
        final View root = inflater.inflate(R.layout.common_list_layout, container, false);

        final RecyclerView listView = root.findViewById(R.id.view_list);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        //final ProgressBar progressBar = root.findViewById(R.id.progress_bar);
        final SwipeRefreshLayout refreshLayout = root.findViewById(R.id.refresh);

        listAdapter = new DocOutboxListAdapter(getContext());
        listAdapter.setOnItemClickListener(createClickListener());
        listView.setAdapter(listAdapter);

        docOutViewModel.docList.observe(getViewLifecycleOwner(), (paggedList) -> {
            listAdapter.submitList(paggedList);
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

                docOutViewModel.updateFromNetwork();
           }
        });

        docOutViewModel.updateFromNetwork();

        return root;
    }

    @Override
    public void onSearch(String searchText) {
        docOutViewModel.search(getViewLifecycleOwner(), searchText);
        docOutViewModel.docList.observe(getViewLifecycleOwner(), (pagedList) -> {
            listAdapter.submitList(pagedList);
        });
    }

    @NotNull
    @Contract(value = " -> new", pure = true)
    private ItemClickListener createClickListener() {

        return new ItemClickListener<DocumentOutbox>() {
            @Override
            public void onItemClick(DocumentOutbox documentOutbox) {

                Intent intent = new Intent(getActivity(), DocDetailActivity.class);
                intent.putExtra("document", Document.create(documentOutbox));
                intent.putExtra("type", "outbox");
                intent.putExtra("caption", "Исходящие");
                startActivity(intent);

            }
        };
    }
}