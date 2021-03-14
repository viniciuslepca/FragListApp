package com.cs250.joanne.myfragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by joanne.
 */
public class TaskAdapter extends ArrayAdapter<Task> {

    int resource;
    String type;

    public TaskAdapter(Context ctx, int res, List<Task> tasks, String type) {
        super(ctx, res, tasks);
        resource = res;
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout itemView;
        Task task = getItem(position);

        if (convertView == null) {
            itemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, itemView, true);
        } else {
            itemView = (LinearLayout) convertView;
        }

        TextView taskName = (TextView) itemView.findViewById(R.id.task_title);
        taskName.setText(task.getName());

        TextView taskDate = (TextView) itemView.findViewById(R.id.task_date);
        if (type.equals("todo")) {
            taskDate.setText(task.getDueDate());
        } else if (type.equals("completed")) {
            taskDate.setText(task.getCompletedDate());
        }

        TextView taskCategory = (TextView) itemView.findViewById(R.id.task_category);
        taskCategory.setText(task.getCategory());

        return itemView;
    }

}
