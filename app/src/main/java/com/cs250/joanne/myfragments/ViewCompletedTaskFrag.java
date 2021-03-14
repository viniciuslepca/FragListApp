package com.cs250.joanne.myfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class ViewCompletedTaskFrag extends DialogFragment {
    private MainActivity myact;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        myact = (MainActivity) getActivity();

        String dueDate = getArguments().getString("dueDate");
        String doneDate = getArguments().getString("doneDate");
        String category = getArguments().getString("category");
        String name = getArguments().getString("name");
        final int POSITION = getArguments().getInt("position");
        final ArrayList<Task> TASKS = myact.tasks;

        // Use the Builder class for convenient dialog constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.view_completed_tasks_layout, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                .setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ViewCompletedTaskFrag.this.getDialog().cancel();
                    }
                });

        TextView nameTextView = view.findViewById(R.id.what_to_do);
        nameTextView.setText(name);
        TextView dueDateTextView = view.findViewById(R.id.pop_up_due_date_done);
        dueDateTextView.setText(dueDate);
        TextView categoryTextView = view.findViewById(R.id.pop_up_category_done);
        categoryTextView.setText(category);
        TextView doneDateTextView = view.findViewById(R.id.pop_up_done_date);
        doneDateTextView.setText(doneDate);

        return builder.create();
    }
}
