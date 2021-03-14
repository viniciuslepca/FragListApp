package com.cs250.joanne.myfragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment currentTasksFrag;
    private Fragment completedTasksFrag;
    private Fragment statsFrag;
    private FragmentTransaction transaction;
    private SharedPreferences myPrefs;
    private Gson gson;
//    protected Integer totalNumTasks;
    protected TaskAdapter aa;
    protected ArrayList<Task> tasks;
    protected ArrayList<Task> todo;
    protected ArrayList<Task> completed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Context context = getApplicationContext();

        // Get the current values of the total number of tasks and the ArrayList of tasks
        todo = new ArrayList<>();
        completed = new ArrayList<>();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
//        totalNumTasks = myPrefs.getInt("totalNumTasks", 0);
        gson = new Gson();
        String json = myPrefs.getString("tasks", null);
        Type type = new TypeToken<List<Task>>(){}.getType();
        if (json == null) {
            tasks = new ArrayList<>();
        } else {
            tasks = gson.fromJson(json, type);
            for (Task t : tasks) {
                if (t.getCompletedDate() == null) {
                    todo.add(t);
                } else {
                    completed.add(t);
                }
            }
        }
        // make array adapter to bind arraylist to listview with custom item layout
        aa = new TaskAdapter(this, R.layout.task_layout, todo, "todo");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Bundle bundleCurrent = new Bundle();
        bundleCurrent.putString("fragTitle", "Current Tasks");
        currentTasksFrag = new TasksListFrag();
        currentTasksFrag.setArguments(bundleCurrent);

        Bundle bundleCompleted = new Bundle();
        bundleCompleted.putString("fragTitle", "Completed Tasks");
        completedTasksFrag = new TasksListFrag();
        completedTasksFrag.setArguments(bundleCompleted);

        statsFrag = new StatsFrag();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, currentTasksFrag).commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Store the total number of tasks and also the ArrayList of tasks back into the shared preferences
        SharedPreferences.Editor peditor = myPrefs.edit();
//        peditor.putInt("totalNumTasks", totalNumTasks);
        // Store the arraylist back as a json
        peditor.putString("tasks", json);
        peditor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.action_add) {
            // Open up the create/add task intent
            Intent intent = new Intent(this, AddTaskActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.todo_frag) {
            aa = new TaskAdapter(this, R.layout.task_layout, todo, "todo");
            transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, currentTasksFrag);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();

        } else if (id == R.id.done_frag) {
            aa = new TaskAdapter(this, R.layout.task_layout, completed, "completed");
            transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, completedTasksFrag);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        } else if (id == R.id.stats_frag) {
            transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, statsFrag);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
