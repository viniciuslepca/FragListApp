package com.cs250.joanne.myfragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;


public class TasksListFrag extends Fragment {

    public static final int MENU_ITEM_EDIT = Menu.FIRST;
    public static final int MENU_ITEM_COPY = Menu.FIRST + 1;
    public static final int MENU_ITEM_DELETE = Menu.FIRST + 2;

    private ListView myList;
    private MainActivity myact;

    Context cntx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.list_frag, container, false);

        myact = (MainActivity) getActivity();
        final String fragTitle = getArguments().getString("fragTitle");
        myact.getSupportActionBar().setTitle(fragTitle);
        cntx = myact.getApplicationContext();

        myList = (ListView) myview.findViewById(R.id.mylist);
        // connect listview to the array adapter in MainActivity
        myList.setAdapter(myact.aa);
        registerForContextMenu(myList);
        // refresh view
        myact.aa.notifyDataSetChanged();

        // program a short click on the list item
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (fragTitle.equals("Current Tasks")) {
                    DialogFragment dFrag = new MarkListCompletedFrag();

                    Bundle bundle = new Bundle();

                    int index = (int) id;

                    String dueDate = myact.tasks.get(index).getDueDate();
                    String category = myact.tasks.get(index).getCategory();
                    String name = myact.tasks.get(index).getName();

                    bundle.putString("name", name);
                    bundle.putString("dueDate", dueDate);
                    bundle.putString("category", category);
                    bundle.putInt("position", (int) id);

                    dFrag.setArguments(bundle);
                    dFrag.show(getChildFragmentManager(), "current_complete");
                } else if (fragTitle.equals("Completed Tasks")) {
                    // Implement for Completed Tasks
                    DialogFragment dFrag = new ViewCompletedTaskFrag();

                    Bundle bundle = new Bundle();

                    int index = (int) id;

                    String dueDate = myact.tasks.get(index).getDueDate();
                    String doneDate = myact.tasks.get(index).getCompletedDate();
                    String category = myact.tasks.get(index).getCategory();
                    String name = myact.tasks.get(index).getName();

                    bundle.putString("name", name);com
                    bundle.putString("dueDate", dueDate);
                    bundle.putString("doneDate", doneDate);
                    bundle.putString("category", category);

                    dFrag.setArguments(bundle);
                    dFrag.show(getChildFragmentManager(), "completed_view");
                }
            }
        });

        return myview;
    }

    // for a long click on a menu item use ContextMenu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // create menu in code instead of in xml file (xml approach preferred)
        menu.setHeaderTitle("Select Item");

        // Add menu items
        menu.add(0, MENU_ITEM_EDIT, 0, R.string.menu_editview);
        menu.add(0, MENU_ITEM_COPY, 0, R.string.menu_copy);
        menu.add(0, MENU_ITEM_DELETE, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        AdapterView.AdapterContextMenuInfo menuInfo;
        menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = menuInfo.position; // position in array adapter

        switch (item.getItemId()) {
            case MENU_ITEM_EDIT: {
                Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                intent.putExtra("Edit", 1);
                intent.putExtra("id", myact.tasks.get(index).getId());
                startActivity(intent);
                Toast.makeText(cntx, "edit request",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            case MENU_ITEM_COPY: {
                // Get the task to be copied
                Task toCopy = myact.tasks.get(index);

                // Copy the fields of the task to be copied
                String title = toCopy.getName() + " (copy)";
                String dueDate = toCopy.getDueDate();
                String doneDate = toCopy.getCompletedDate();
                String category = toCopy.getCategory();

                // Create the copied task
                Task copyTask = new Task(title, dueDate, category);
                copyTask.setCompletedDate(doneDate);

                // Add the copied task to the ArrayList of tasks
                myact.tasks.add(copyTask);
                Context context = myact.getApplicationContext();
                SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                ArrayList<Task> updatedTasks = myact.tasks;
                SharedPreferences.Editor peditor = myPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(updatedTasks);
                peditor.putString("tasks", json);
                peditor.apply();

                Toast.makeText(cntx, "Copy Successfully Made!",
                        Toast.LENGTH_SHORT).show();
                myact.aa.notifyDataSetChanged();
                return false;
            }
            case MENU_ITEM_DELETE: {
                myact.tasks.remove(index);
                Context context = myact.getApplicationContext();
                SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                ArrayList<Task> updatedTasks = myact.tasks;
                SharedPreferences.Editor peditor = myPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(updatedTasks);
                peditor.putString("tasks", json);
                peditor.apply();
                Toast.makeText(cntx, "Task " + index + " successfully deleted!",
                        Toast.LENGTH_SHORT).show();
                // refresh view
//                getFragmentManager().beginTransaction().dietach(this).attach(this).commit();
                myact.aa.notifyDataSetChanged();
                return true;
            }
        }
        return false;
    }

    // Called at the start of the visible lifetime.
    @Override
    public void onStart(){
        super.onStart();
        Log.d ("Other Fragment2", "onStart");
        // Apply any required UI change now that the Fragment is visible.
    }

    // Called at the start of the active lifetime.
    @Override
    public void onResume(){
        super.onResume();
        Log.d ("Other Fragment", "onResume");
        // Resume any paused UI updates, threads, or processes required
        // by the Fragment but suspended when it became inactive.
    }

    // Called at the end of the active lifetime.
    @Override
    public void onPause(){
        Log.d ("Other Fragment", "onPause");
        // Suspend UI updates, threads, or CPU intensive processes
        // that don't need to be updated when the Activity isn't
        // the active foreground activity.
        // Persist all edits or state changes
        // as after this call the process is likely to be killed.
        super.onPause();
    }

    // Called to save UI state changes at the
    // end of the active lifecycle.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d ("Other Fragment", "onSaveInstanceState");
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate, onCreateView, and
        // onCreateView if the parent Activity is killed and restarted.
        super.onSaveInstanceState(savedInstanceState);
    }

    // Called at the end of the visible lifetime.
    @Override
    public void onStop(){
        Log.d ("Other Fragment", "onStop");
        // Suspend remaining UI updates, threads, or processing
        // that aren't required when the Fragment isn't visible.
        super.onStop();
    }

    // Called when the Fragment's View has been detached.
    @Override
    public void onDestroyView() {
        Log.d ("Other Fragment", "onDestroyView");
        // Clean up resources related to the View.
        super.onDestroyView();
    }

    // Called at the end of the full lifetime.
    @Override
    public void onDestroy(){
        Log.d ("Other Fragment", "onDestroy");
        // Clean up any resources including ending threads,
        // closing database connections etc.
        super.onDestroy();
    }

    // Called when the Fragment has been detached from its parent Activity.
    @Override
    public void onDetach() {
        Log.d ("Other Fragment", "onDetach");
        super.onDetach();
    }
}
