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

    public TaskAdapter(Context ctx, int res, List<Task> tasks)
    {
        super(ctx, res, tasks);
        resource = res;
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

        TextView whatView = (TextView) itemView.findViewById(R.id.detail);

        whatView.setText(task.getName());


        return itemView;
    }

}
