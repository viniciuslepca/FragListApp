package com.cs250.joanne.myfragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class StatsFrag extends Fragment {

    private MainActivity myact;
    private SharedPreferences myPrefs;
    private Gson gson;
    private View view;
    protected ArrayList<Task> tasks;
    protected ArrayList<Task> todo;
    protected ArrayList<Task> completed;

    Context cntx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.stats_frag, container, false);

        myact = (MainActivity) getActivity();
        myact.getSupportActionBar().setTitle("Statistics");
        cntx = myact.getApplicationContext();
        view = myview;

        return myview;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Get tasks
        todo = new ArrayList<>();
        completed = new ArrayList<>();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(cntx);
//        totalNumTasks = myPrefs.getInt("totalNumTasks", 0);
        gson = new Gson();
        String json = myPrefs.getString("tasks", null);
        Type type = new TypeToken<List<Task>>() {
        }.getType();
        if (json == null) {
            tasks = new ArrayList<>();
        } else {
            tasks = gson.fromJson(json, type);
        }

        // Calculate statistics
        int doneByDeadline = 0;
        int doneAfterDue = 0;
        int pastDue = 0;
        int toBeDone = 0;
        int totalTasks = 0;
        for (Task t : tasks) {
            totalTasks++;
            Date dueDate = t.getDueDateFormatted();
            Date completedDate = t.getCompletedDateFormatted();
            if (completedDate == null) {
                Date today = new Date();
                if (today.after(dueDate)) {
                    pastDue++;
                } else {
                    toBeDone++;
                }
            } else {
                if (completedDate.after(dueDate)) {
                    doneAfterDue++;
                } else {
                    doneByDeadline++;
                }
            }

        }

        // Populate statistics
        TextView doneByDeadlineNum = (TextView) view.findViewById(R.id.doneByDeadlineNum);
        doneByDeadlineNum.setText(String.valueOf(doneByDeadline));

        TextView doneAfterDueNum = (TextView) view.findViewById(R.id.doneAfterDueNum);
        doneAfterDueNum.setText(String.valueOf(doneAfterDue));

        TextView pastDueNum = (TextView) view.findViewById(R.id.pastDueNum);
        pastDueNum.setText(String.valueOf(pastDue));

        TextView toBeDoneNum = (TextView) view.findViewById(R.id.todoNum);
        toBeDoneNum.setText(String.valueOf(toBeDone));

        TextView totalTasksNum = (TextView) view.findViewById(R.id.totalTasksNum);
        totalTasksNum.setText(String.valueOf(totalTasks));
    }
}
