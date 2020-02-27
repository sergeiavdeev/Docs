package com.avdeev.docs.ui.listAdapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.avdeev.docs.R;
import com.avdeev.docs.core.BaseDocument;
import com.avdeev.docs.core.Task;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class TaskListAdapter extends BaseAdapter<Task> {

    public TaskListAdapter(@NotNull Context context, ArrayList<Task> tasks) {
        super(context, tasks);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.task_list_row;
    }

    @Override
    protected Task copyObject(Task task) {
        return new Task(task);
    }

    @Override
    protected BaseHolder createHolder(View view) {
        return new TaskHolder(view);
    }

    @Override
    protected boolean findText(Task task, CharSequence text) {

        boolean result = false;

        if (task.getTitle().toUpperCase().contains(text) ||
            task.getAuthor().toUpperCase().contains(text) ||
            task.getNumber().toUpperCase().contains(text)
            ) {
            result = true;
        }
        return result;
    }

    protected class TaskHolder extends BaseHolder {

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

        @Override
        void bind(@NotNull Task task) {

            title.setText(task.getTitle());

            long lDate = task.getDate();
            String sNumber = task.getNumber();

            if (lDate > 0 && sNumber.length() > 0) {

                date.setText("№" + sNumber + " от " + BaseDocument.dateFromLong(task.getDate()));
                date.setVisibility(View.VISIBLE);
            } else {
                date.setText("");
                date.setVisibility(View.GONE);
            }

            author.setText(task.getAuthor());

            long dateDue = task.getDate_due();

            if (dateDue > 0) {
                date_due.setText(BaseDocument.dateFromLong(task.getDate_due()));
            } else {
                date_due.setText("");
            }
        }
    }
}
