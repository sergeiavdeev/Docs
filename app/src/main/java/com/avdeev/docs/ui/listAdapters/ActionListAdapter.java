package com.avdeev.docs.ui.listAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avdeev.docs.R;
import com.avdeev.docs.core.Action;
import com.avdeev.docs.core.User;

import java.util.ArrayList;

public class ActionListAdapter extends RecyclerView.Adapter<ActionListAdapter.ActionHolder> {

    private ArrayList<Action> actions;
    private LayoutInflater inflater;
    //private ActionListAdapter.ActionHolder actionHolder;

    public ActionListAdapter(Context context, ArrayList<Action> actions) {

        this.actions = actions;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ActionListAdapter.ActionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.action_list_row, parent, false);
        return new ActionListAdapter.ActionHolder(view);

    }

    @Override
    public int getItemCount() {
        return actions.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ActionListAdapter.ActionHolder holder, int position) {

        Action action = actions.get(position);
        holder.bind(action);
    }

    protected class ActionHolder extends RecyclerView.ViewHolder {

        private TextView date;
        private TextView person;
        private TextView description;

        public ActionHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            person = itemView.findViewById(R.id.person);
            description = itemView.findViewById(R.id.description);
        }

        private void bind(Action action) {

            person.setText(action.getPerson());
            date.setText(User.dateFromLong(action.getDate()));
            description.setText(action.getDescription());
        }
    }
}
