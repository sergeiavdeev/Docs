package com.avdeev.docs.ui.listAdapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.avdeev.docs.R;
import com.avdeev.docs.core.database.entity.Task;
import com.avdeev.docs.core.database.entity.TaskWithFiles;
import com.avdeev.docs.core.network.pojo.BaseDocument;

public class TaskListAdapter extends BasePagedAdapter<TaskWithFiles> {

    @Override
    protected BaseHolder createHolder(View view) {
        return new TaskViewHolder(view);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.task_list_row;
    }

    public TaskListAdapter(Context context) {
        super(context, TaskWithFiles.DIFF_UTIL);
    }

    protected class TaskViewHolder extends BaseHolder {

        private TextView title;
        private TextView author;
        private TextView date;
        private TextView date_due;

        public TaskViewHolder(View view) {
            super(view);

            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            date = itemView.findViewById(R.id.date);
            date_due = itemView.findViewById(R.id.date_due);
        }

        @Override
        protected void bind(TaskWithFiles taskWithFiles) {

            Task task = taskWithFiles.task;

            title.setText(task.title);

            long lDate = task.date;
            String sNumber = task.number;

            if (lDate > 0 && sNumber.length() > 0) {

                date.setText("№" + sNumber + " от " + BaseDocument.dateFromLong(task.date));
                date.setVisibility(View.VISIBLE);
            } else {
                date.setText("");
                date.setVisibility(View.GONE);
            }

            author.setText(task.author);

            long dateDue = task.date_due;

            if (dateDue > 0) {
                date_due.setText(BaseDocument.dateFromLong(task.date_due));
            } else {
                date_due.setText("");
            }
        }
    }
}
