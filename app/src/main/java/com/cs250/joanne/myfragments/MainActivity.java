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

    private Fragment item;
    private Fragment list;
    private FragmentTransaction transaction;
    private SharedPreferences myPrefs;
    protected Integer totalNumTasks;
    protected TaskAdapter aa;
    protected ArrayList<Task> tasks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Context context = getApplicationContext();

        // create ArrayList of items
        tasks = new ArrayList<Task>();
        // make array adapter to bind arraylist to listview with custom item layout
        aa = new TaskAdapter(this, R.layout.item_layout, tasks);

        // Get the current values of the total number of tasks and the ArrayList of tasks
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        totalNumTasks = myPrefs.getInt("totalNumTasks", 0);
        Gson gson = new Gson();
        String json = myPrefs.getString("tasks", "");
        Type type = new TypeToken<List<Task>>(){}.getType();
        tasks = gson.fromJson(json, type);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        item = new ItemFrag();
        list = new ListFrag();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, item).commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Store the total number of tasks and also the ArrayList of tasks back into the shared preferences
        SharedPreferences.Editor peditor = myPrefs.edit();
        peditor.putInt("totalNumTasks", totalNumTasks);
        // Store the arraylist back as a json
        gson = new Gson();
        json = gson.toJson(tasks);
        peditor.putString("tasks", json);
        peditor.apply();
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.action_add) {
            // Open up the create/add task intent
            Intent intent = new Intent(this, AddTaskActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.todo_frag) {
            transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, this.item);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();

        } else if (id == R.id.done_frag) {
            transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, list);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        } else if (id == R.id.stats_frag) {
            transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, list);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
