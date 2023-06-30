package com.example.PocketMaths;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * This activity relates to the Main Menu Page of the app.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * MainMenuActivity extends AppCompatActivity class to have access to Activity methods.
 * MainMenuActivity implements View.OnCLickListener interface to detect touch input.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * It displays all instances of QuestionSet through its RecyclerView.
 * It has a search menu which filters through the QuestionSet instances by name and description.
 * It has buttons that take the user to various pages.
 */
public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseHelper databaseHelper = new DatabaseHelper(this);
    private MainMenuRecyclerAdapter mainMenuRecyclerAdapter;

    private RecyclerView rvMainMenu;
    private TextView txtNoResults;
    private RelativeLayout relMainMenu;
    private ImageView imgAccount, imgTasks, imgCreateTask, imgSettings;
    private SearchView svQuestionSet;

    /**
     * Overrides the onCreate method of the super class.
     * Runs when MainMenuActivity starts.
     * Sets the layout, theme and preferences.
     * Calls all necessary functions, either directly or through other functions.
     *
     * @param savedInstanceState Required for super constructor.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.getInstance().setThemeId(databaseHelper.getTheme());
        Utils.getInstance().setShowRefreshers(databaseHelper.getShowRefreshers());
        setTheme(Utils.getInstance().getThemeId());
        setContentView(R.layout.activity_main_menu);

        initViews();

        setUpRecyclerView();

        setUpSearchView();
    }

    /**
     * Initialises View objects.
     * Sets the activity's click listener to appropriate View objects.
     */
    private void initViews() {
        relMainMenu = findViewById(R.id.relMainMenu);
        rvMainMenu = findViewById(R.id.rvMainMenu);
        txtNoResults = findViewById(R.id.txtNoResults);
        imgAccount = findViewById(R.id.imgAccount);
        imgTasks = findViewById(R.id.imgTasks);
        imgCreateTask = findViewById(R.id.imgCreateTask);
        imgSettings = findViewById(R.id.imgSettings);
        svQuestionSet = findViewById(R.id.svQuestionSet);

        imgAccount.setOnClickListener(this);
        imgTasks.setOnClickListener(this);
        imgCreateTask.setOnClickListener(this);
        imgSettings.setOnClickListener(this);
    }

    /**
     * Performs the setting-up of the RecyclerView, using MainMenuRecyclerAdapter.
     */
    private void setUpRecyclerView() {
        mainMenuRecyclerAdapter = new MainMenuRecyclerAdapter(this);
        mainMenuRecyclerAdapter.setQuestionSets(Utils.getQuestionSets());

        rvMainMenu.setAdapter(mainMenuRecyclerAdapter);
        rvMainMenu.setLayoutManager((new LinearLayoutManager(this)));

        mainMenuRecyclerAdapter.setExpandedIndex(-1);
    }

    /**
     * Performs the setting up of the SearchView instance.
     * Sets a new instance of SearchView.OnQueryTextChangeListener to the SearchView object, such that
     * the QuestionSet objects can be filtered in real-time.
     */
    private void setUpSearchView() {
        svQuestionSet.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // Filtering the Question Sets based on the value of s:
                filterQuestionSets(s);
                return false;
            }
        });
    }

    /**
     * Iterates through QuestionSet objects, checking their names and descriptions, displaying those
     * that match the target text and updating them in real-time.
     * If there are no filter results, the function displays an appropriate message.
     *
     * @param targetText The text that is looked for in the filtering process.
     */
    public void filterQuestionSets(String targetText) {

        ArrayList<QuestionSet> questionSetsToShow = new ArrayList<>();
        targetText = targetText.toUpperCase(Locale.ROOT);

        // Iterating through the Question Set:
        for (QuestionSet questionSet : Utils.getQuestionSets()) {
            // If the Question Set Name or Description contains the target text, including it:
            if (questionSet.getName().toUpperCase(Locale.ROOT).contains(targetText) ||
                    questionSet.getDescription().toUpperCase(Locale.ROOT).contains(targetText)) {
                questionSetsToShow.add(questionSet);
            }
        }

        if (questionSetsToShow.size() == 0) {
            // Displaying message if none are found:
            TransitionManager.beginDelayedTransition(relMainMenu);
            txtNoResults.setVisibility(View.VISIBLE);
        } else {
            // Otherwise, hiding the message:
            TransitionManager.beginDelayedTransition(relMainMenu);
            txtNoResults.setVisibility(View.GONE);
        }

        // Setting the filtered array list of question sets:
        mainMenuRecyclerAdapter.setQuestionSets(questionSetsToShow);
    }

    /**
     * Determines which View object has been clicked and performs the appropriate action.
     *
     * @param view Used to determine the View object clicked.
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case (R.id.imgAccount):
                // Starting the Account Activity:
                startActivity(new Intent(this, AccountActivity.class));
                break;

            case (R.id.imgTasks):
                startActivity(new Intent(this, TaskViewActivity.class));

                break;

            case (R.id.imgCreateTask):
                Utils.getInstance().setTargetClass(TaskCreateActivity.class);
                startActivity(new Intent(this, PinVerificationActivity.class));
                System.out.println(Utils.getInstance().getTargetClass());
                break;

            case (R.id.imgSettings):
                startActivity(new Intent(this, SettingsActivity.class));
                finish();
                break;

            default:
                break;
        }
    }

    /**
     * Overrides the action to be performed when the back button is pressed.
     * In this case, nothing should should be done.
     */
    @Override
    public void onBackPressed() {

    }
}
