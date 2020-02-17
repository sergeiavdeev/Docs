package com.avdeev.docs.ui.listAdapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.avdeev.docs.R;
import com.avdeev.docs.core.Action;
import com.avdeev.docs.core.User;
import java.util.ArrayList;

public class ActionListAdapter extends BaseAdapter<Action> {

    public ActionListAdapter(Context context, ArrayList<Action> list) {
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

    @Override
    protected boolean findText(Action object, CharSequence text) {
        return true;
    }

    protected class ActionHolder extends BaseHolder {

        private TextView date;
        private TextView person;
        private TextView description;

        public ActionHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            person = itemView.findViewById(R.id.person);
            description = itemView.findViewById(R.id.description);
        }

        @Override
        void bind(Action action) {

            person.setText(action.getPerson());
            date.setText(User.dateFromLong(action.getDate()));
            description.setText(action.getDescription());
        }
    }
}
