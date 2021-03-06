package com.cs250.joanne.myfragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

//    private Integer totalNumTasks;
    private SharedPreferences myPrefs;
    private DatePickerDialog dPicker;
    private int edit;
    private int id;
    private final CharSequence EMPTY_NAME = "ERROR: Please enter a name for your task.";
    private final CharSequence EMPTY_DUE_DATE = "ERROR: Please select a due date";
    private int duration;
    private Toast emptyName;
    private Toast emptyDueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Intent intent = getIntent();
        edit = intent.getIntExtra("Edit", 0);
        id = intent.getIntExtra("id", -1);

        Context context = getApplicationContext();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        // Set the title of the action bar
        getSupportActionBar().setTitle("Update Task");

        duration = Toast.LENGTH_SHORT;
        emptyName = Toast.makeText(context, EMPTY_NAME, duration);
        emptyDueDate = Toast.makeText(context, EMPTY_DUE_DATE, duration);

        // Set up the date-picker for the due date editText
        final EditText eTextDueDate = (EditText) findViewById(R.id.due_date_input);
        eTextDueDate.setInputType(InputType.TYPE_NULL);
        eTextDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                // Calendar date picker dialog
                dPicker = new DatePickerDialog(AddTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        eTextDueDate.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                    }
                }, year, month, day);
            dPicker.show();
            }
        });
    }

    /**
     * Function that will be called when the SAVE button is clicked.
     * @param view the current view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveButton(View view) {
        // Retrieve the user-inputted values
        EditText eName = (EditText) findViewById(R.id.name_input);
        EditText eDueDate = (EditText) findViewById(R.id.due_date_input);
        EditText eCategory = (EditText) findViewById(R.id.category_input);

        // Convert the user-inputted values to Strings
        String name = eName.getText().toString();
        String dueDate = eDueDate.getText().toString();
        String category = eCategory.getText().toString();

        if ((name == null || name.isEmpty()) && (dueDate == null || dueDate.isEmpty())) {
            emptyName.show();
            emptyDueDate.show();
        } else if (name == null || name.isEmpty()) {
            emptyName.show();
        } else if (dueDate == null || dueDate.isEmpty()) {
            emptyDueDate.show();
        } else {
            if (edit == 0) {
                // Create the new task from the user-input
                Task newTask = new Task(name, dueDate, category);

                // Create the ArrayList that holds all the tasks currently in the application
                ArrayList<Task> tasks = new ArrayList<>();

                //        totalNumTasks = myPrefs.getInt("totalNumTasks", 0);
                //        totalNumTasks++;

                // Retrieve the json of the ArrayList of tasks from the shared preferences and then add it to the ArrayList
                Gson gson = new Gson();
                String json = myPrefs.getString("tasks", null);
                Type type = new TypeToken<List<Task>>() {
                }.getType();
                if (json != null) {
                    tasks = gson.fromJson(json, type);
                }
                tasks.add(newTask);

                // Store the updated ArrayList back into the editor
                SharedPreferences.Editor peditor = myPrefs.edit();
                gson = new Gson();
                json = gson.toJson(tasks);
                peditor.putString("tasks", json);
                //        peditor.putInt("totalNumTasks", 0);
                peditor.apply();
            } else {
                ArrayList<Task> tasks = new ArrayList<>();
                Gson gson = new Gson();
                String json = myPrefs.getString("tasks", null);
                Type type = new TypeToken<List<Task>>() {
                }.getType();
                Task toUpdate = null;
                if (json != null) {
                    tasks = gson.fromJson(json, type);
                    for (Task t : tasks) {
                        if (t.getId() == id) {
                            toUpdate = t;
                        }
                    }
                }
                if (toUpdate != null) {
                    toUpdate.setName(name);
                    toUpdate.setDueDate(dueDate);
                    toUpdate.setCategory(category);
                }
                gson = new Gson();
                json = gson.toJson(tasks);
                SharedPreferences.Editor peditor = myPrefs.edit();
                peditor.putString("tasks", json);
                peditor.apply();

            }
            // Return back to the tasks page
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Function that is called when the cancel button is clicked.
     * @param view the current view
     */
    public void cancelButton(View view) {
        EditText eName = (EditText) findViewById(R.id.name_input);
        EditText eDueDate = (EditText) findViewById(R.id.due_date_input);
        EditText eCategory = (EditText) findViewById(R.id.category_input);

        eName.getText().clear();
        eDueDate.getText().clear();
        eCategory.getText().clear();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        totalNumTasks = (savedInstanceState != null) ? savedInstanceState.getInt("totalNumTasks") : 0;
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle myBundle) {
//        myBundle.putInt("totalNumTasks", totalNumTasks);
        super.onSaveInstanceState(myBundle);
    }

    @Override
    public void onPause() {
//        SharedPreferences.Editor peditor = myPrefs.edit();
//        peditor.putInt("totalNumTasks", totalNumTasks);
//        peditor.apply();
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
//        SharedPreferences.Editor peditor = myPrefs.edit();
//        peditor.putInt("totalNumTasks", totalNumTasks);
//        peditor.apply();
        super.onDestroy();
    }
}