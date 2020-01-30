package com.avdeev.docs.core;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.avdeev.docs.MainActivity;

import java.util.zip.Inflater;

public class DocFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();

        if (activity == null) return;

        activity.setOnQuerySearchListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(getActivity(), query, Toast.LENGTH_LONG).show();
                onSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(getActivity(), newText, Toast.LENGTH_LONG).show();
                onSearch(newText);
                return false;
            }
        });
    }

    public void onSearch(String searchString) {

    }
}
