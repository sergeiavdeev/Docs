package com.avdeev.docs.ui.ext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avdeev.docs.R;
import com.avdeev.docs.core.Action;
import com.avdeev.docs.core.Document;
import com.avdeev.docs.core.Task;
import com.avdeev.docs.core.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TaskListAdapter extends RecyclerView.Adapter <TaskListAdapter.TaskHolder>
implements Filterable {

    private ArrayList<Task> tasks;
    private ArrayList<Task> constTasks;
    private LayoutInflater inflater;

    private ItemClickListener itemClickListener;

    private TaskFilter taskFilter;

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.task_list_row, parent, false);

        return new TaskListAdapter.TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {

        Task task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public TaskListAdapter(@NotNull Context context, ArrayList<Task> tasks) {

        this.tasks = tasks;
        constTasks = new ArrayList<>(tasks);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setOnItemClickListener(final ItemClickListener listener) {

        this.itemClickListener = listener;
    }

    @Override
    public Filter getFilter() {

        if (taskFilter == null) {
            taskFilter = new TaskFilter();
        }

        return taskFilter;
    }

    protected class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView title;
        private TextView author;
        private TextView date;
        private TextView date_due;

        public TaskHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            date = itemView.findViewById(R.id.date);
            date_due = itemView.findViewById(R.id.date_due);

            itemView.setOnClickListener(this);
        }

        private void bind(@NotNull Task task) {

            title.setText(task.getTitle());

            long lDate = task.getDate();
            String sNumber = task.getNumber();

            if (lDate > 0 && sNumber.length() > 0) {

                date.setText("№" + sNumber + " от " + User.dateFromLong(task.getDate()));
                date.setVisibility(View.VISIBLE);
            } else {
                date.setText("");
                date.setVisibility(View.GONE);
            }

            author.setText(task.getAuthor());

            long dateDue = task.getDate_due();

            if (dateDue > 0) {
                date_due.setText(User.dateFromLong(task.getDate_due()));
            } else {
                date_due.setText("");
            }
        }

        @Override
        public void onClick(View view) {

            if (itemClickListener != null) {

                Task task = tasks.get(getAdapterPosition());
                itemClickListener.onItemClick(task);
            }
        }
    }

    public interface ItemClickListener {

        void onItemClick(Task task);
    }

    private class TaskFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence searchText) {

            FilterResults results = new FilterResults();

            results.count = constTasks.size();
            results.values = constTasks;

            if (searchText != null && searchText.length() > 0) {

                ArrayList<Task> filterTasks = new ArrayList<>();

                searchText = searchText.toString().toUpperCase();

                for (int i = 0; i < constTasks.size(); i++) {

                    Task task = constTasks.get(i);
                    if (findText(task, searchText)) {

                        filterTasks.add(new Task(task));
                    }
                }

                results.count = filterTasks.size();
                results.values = filterTasks;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            tasks = (ArrayList<Task>)filterResults.values;
            notifyDataSetChanged();
        }

        private boolean findText(Task task, CharSequence text) {

            boolean result = false;

            if (
                task.getTitle().toUpperCase().contains(text) ||
                task.getAuthor().toUpperCase().contains(text) ||
                task.getNumber().toUpperCase().contains(text)
            ) {

                result = true;
            }

            return result;
        }
    }
}
