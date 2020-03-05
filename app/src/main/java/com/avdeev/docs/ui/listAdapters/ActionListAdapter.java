package com.avdeev.docs.ui.listAdapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.avdeev.docs.R;
import com.avdeev.docs.core.network.pojo.Action;
import com.avdeev.docs.core.network.pojo.BaseDocument;

import java.util.ArrayList;
import java.util.List;

public class ActionListAdapter extends BaseAdapter<Action> {

    public ActionListAdapter(Context context, List<Action> list) {
        super(context, list);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.action_list_row;
    }

    @Override
    protected BaseHolder createHolder(View view) {

        return new ActionHolder(view);
    }

    @Override
    protected Action copyObject(Action object) {

        return new Action(object);
    }

    private class ActionHolder extends BaseHolder {

        private TextView date;
        private TextView person;
        private TextView description;

        private ActionHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            person = itemView.findViewById(R.id.person);
            description = itemView.findViewById(R.id.description);
        }

        @Override
        protected void bind(Action action) {

            person.setText(action.getPerson());
            date.setText(BaseDocument.dateFromLong(action.getDate()));
            description.setText(action.getDescription());
        }
    }
}
