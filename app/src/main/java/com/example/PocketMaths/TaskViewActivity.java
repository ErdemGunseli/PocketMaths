package com.example.PocketMaths;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * This activity relates to the Task Viewing Page of the app.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * TaskViewActivity extends AppCompatActivity class to have access to Activity methods.
 * TaskViewActivity implements View.OnCLickListener interface to detect touch input.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Depending on whether the user has chosen to view the complete or incomplete tasks, displays a
 * list of tasks.
 * Clicking on any of the tasks results in the linked question set to be launched.
 */
public class TaskViewActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseHelper databaseHelper = new DatabaseHelper(this);
    private TaskRecyclerAdapter taskRecyclerAdapter = new TaskRecyclerAdapter(this, databaseHelper, false, true);
    private ArrayList<Task> taskedTasks = new ArrayList<>();
    private ArrayList<Task> completedTasks = new ArrayList<>();

    private ScrollView svViewTask;
    private ImageView imgExit;
    private TextView txtNothingHere;
    private RecyclerView rvTasks;
    private TabLayout tlStatus;

    /**
     * Overrides the onCreate method of the super class.
     * Runs when TaskViewActivity starts.
     * Sets the layout and theme.
     * Calls all necessary functions, either directly or through other functions.
     *
     * @param savedInstanceState Required for super constructor.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Utils.getInstance().getThemeId());
        setContentView(R.layout.activity_view_task);

        initViews();

        setUpTabLayout();

        setUpRecyclerView();

        organiseTasks();

        showMessageIfEmpty(taskedTasks);
    }

    /**
     * Initialises View objects.
     * Sets the activity's click listener to appropriate View objects.
     */
    private void initViews() {
        svViewTask = findViewById(R.id.svViewTask);
        tlStatus = findViewById(R.id.tlStatus);
        imgExit = findViewById(R.id.imgExit);
        txtNothingHere = findViewById(R.id.txtNothingHere);
        rvTasks = findViewById(R.id.rvTasks);

        imgExit.setOnClickListener(this);
    }

    /**
     * Performs the setting up of the TabLayout instance.
     * Sets a new instance of TabLayout.OnTabSelectedListener() to the TabLayout instance
     * such that the appropriate ArrayList can be displayed depending on the user's selection.
     */
    private void setUpTabLayout() {
        tlStatus.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case (0):
                        taskRecyclerAdapter.setTasks(taskedTasks);
                        // Show message if there are no tasks:
                        showMessageIfEmpty(taskedTasks);
                        break;

                    case (1):
                        taskRecyclerAdapter.setTasks(completedTasks);
                        // Show message if there are no tasks:
                        showMessageIfEmpty(completedTasks);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /**
     * Performs the setting-up of the RecyclerView, using TaskRecyclerAdapter.
     */
    private void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        // Reverse order since latest task should appear first:
        linearLayoutManager.setReverseLayout(true);

        rvTasks.setLayoutManager(linearLayoutManager);
        rvTasks.setAdapter(taskRecyclerAdapter);
    }

    /**
     * Sorts each task linked to the current account into two ArrayLists - complete and incomplete.
     */
    private void organiseTasks() {
        for (Task task : databaseHelper.getTasks(Utils.getInstance().getUserAccount().getId())) {
            if (task.isCompleted()) {
                completedTasks.add(task);
            } else {
                taskedTasks.add(task);
            }
        }
        taskRecyclerAdapter.setTasks(taskedTasks);
    }


    /**
     * Determines which View object has been clicked and performs the appropriate action.
     *
     * @param view Used to determine the View object clicked.
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgExit){
            startActivity(new Intent(this, MainMenuActivity.class));
            finish();
        }
    }

    /**
     * Displays a message if the ArrayList is empty.
     * Otherwise, hides the message.
     *
     * @param tasks The ArrayList in question.
     */
    private void showMessageIfEmpty(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            TransitionManager.beginDelayedTransition(svViewTask);
            txtNothingHere.setVisibility(View.VISIBLE);
        } else {
            TransitionManager.beginDelayedTransition(svViewTask);
            txtNothingHere.setVisibility(View.GONE);
        }

    }
}
