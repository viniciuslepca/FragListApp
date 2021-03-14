package com.cs250.joanne.myfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MarkListCompletedFrag extends DialogFragment {

    private MainActivity myact;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        myact = (MainActivity) getActivity();

        final String dueDate = getArguments().getString("dueDate");
        String category = getArguments().getString("category");
        String name = getArguments().getString("name");
        final int POSITION = getArguments().getInt("position");
        final ArrayList<Task> TASKS = myact.tasks;

        // Use the Builder class for convenient dialog constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.mark_completed_layout, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                .setPositiveButton("MARK COMPLETED", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Implement Mark Completed button
                        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                        Date date = new Date();
                        String completedDate = formatter.format(date).toString();
                        Task task = TASKS.get(POSITION);
                        task.setCompletedDate(completedDate);
                        TASKS.remove(POSITION);
                        TASKS.add(task);
                        Context context = myact.getApplicationContext();
                        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                        ArrayList<Task> updatedTasks = myact.tasks;
                        SharedPreferences.Editor peditor = myPrefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(updatedTasks);
                        peditor.putString("tasks", json);
                        peditor.apply();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MarkListCompletedFrag.this.getDialog().cancel();
                            }
                        });

        TextView nameTextView = view.findViewById(R.id.what_to_do);
        nameTextView.setText(name);
        TextView dueDateTextView = view.findViewById(R.id.pop_up_due_date_done);
        dueDateTextView.setText(dueDate);
        TextView categoryTextView = view.findViewById(R.id.pop_up_category_done);
        categoryTextView.setText(category);

        return builder.create();
    }
}
