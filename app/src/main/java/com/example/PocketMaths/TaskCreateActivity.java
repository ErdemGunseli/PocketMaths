package com.example.PocketMaths;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.slider.Slider;

import java.util.ArrayList;

/**
 * This activity relates to the Task Creation Page of the app.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * TaskCreateActivity extends AppCompatActivity class to have access to Activity methods.
 * TaskCreateActivity implements View.OnCLickListener interface to detect touch input.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * It displays all incomplete tasks in the form of a scrollable list.
 * Each incomplete task can be deleted by clicking on its delete button.
 * Has inputs for creating a new task.
 * Retrieves data from these inputs, performs validation checks.
 * If these checks are successful, creates a new Task object and saves it to the relational database.
 */
public class TaskCreateActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseHelper databaseHelper = new DatabaseHelper(this);
    private String[] inputs;
    private TaskRecyclerAdapter taskRecyclerAdapter = new TaskRecyclerAdapter(this, databaseHelper, true, false);
    private int questionSetId = -1;
    private ArrayList<String> questionSetNames = new ArrayList<>();
    private ArrayList<Task> tasks = new ArrayList<>();
    private int passMark;

    private ScrollView svCreateTask;
    private ImageView imgExit;
    private TextView txtPassMark;
    private EditText edtTxtTaskName, edtTxtReward;
    private Slider sliderPassMark;
    private Spinner spQuestionSets;
    private SwitchCompat swAddReward;
    private Button btnCreate;
    private RecyclerView rvTasks;

    /**
     * Overrides the onCreate method of the super class.
     * Runs when TaskCreateActivity starts.
     * Sets the layout and theme.
     * Calls all necessary functions, either directly or through other functions.
     *
     * @param savedInstanceState Required for super constructor.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Utils.getInstance().getThemeId());
        setContentView(R.layout.activity_create_task);

        initViews();

        setData();

        setUpRecyclerView();

        showIncompleteTasks();
    }

    /**
     * Initialises View objects.
     * Sets the activity's click listener to appropriate View objects.
     */
    private void initViews() {
        txtPassMark = findViewById(R.id.txtPassMark);
        imgExit = findViewById(R.id.imgExit);
        edtTxtTaskName = findViewById(R.id.edtTxtTaskName);
        svCreateTask = findViewById(R.id.svCreateTask);
        sliderPassMark = findViewById(R.id.sliderPassMark);
        edtTxtReward = findViewById(R.id.edtTxtReward);
        swAddReward = findViewById(R.id.swAddReward);
        btnCreate = findViewById(R.id.btnCreate);
        svCreateTask = findViewById(R.id.svCreateTask);
        rvTasks = findViewById(R.id.rvTasks);
        spQuestionSets = findViewById(R.id.spQuestionSets);

        imgExit.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
    }

    /**
     * Calls the necessary functions for all of the data to be set and displayed correctly.
     * Updates the pass mark in real time.
     */
    private void setData() {
        txtPassMark.setText(String.format(getString(R.string.pass_Mark), passMark));
        setUpSwitch();
        setUpSpinner();
        setUpSlider();
    }

    /**
     * Performs the setting up of the SwitchCompat instance.
     * Sets a new instance of CompoundButton.OnCheckChangeListener() to the SwitchCompat instance
     * such that appropriate View objects can be displayed and the appropriate validation checks can
     * be performed depending on the state of the switch.
     */
    private void setUpSwitch() {
        swAddReward.setOnCheckedChangeListener((CompoundButton compoundButton, boolean checked) -> {
            if (checked) {
                TransitionManager.beginDelayedTransition(svCreateTask);
                edtTxtReward.setVisibility(View.VISIBLE);
            } else {
                TransitionManager.beginDelayedTransition(svCreateTask);
                edtTxtReward.setVisibility(View.GONE);
                edtTxtReward.setText("");
            }
        });
    }

    /**
     * Performs the setting up of the Spinner instance.
     * Sets a new instance of AdapterView.OnItemSelectedListener() to the Spinner instance such that
     * the question set selected by the user can be retrieved and processed.
     * Sets an instance of ArrayAdapter to the Spinner instance such that the question sets can be
     * displayed properly.
     */
    private void setUpSpinner() {
        spQuestionSets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                questionSetId = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                questionSetId = -1;
            }
        });

        for (QuestionSet questionSet : Utils.getQuestionSets()) {
            questionSetNames.add(questionSet.getName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, questionSetNames);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spQuestionSets.setAdapter(arrayAdapter);
    }

    /**
     * Performs the setting up of the Slider instance.
     * Sets a new instance of Slider.OnChangeListener() to the Slider instance such that the data
     * from the slider can be retrieved and updated in real-time.
     */
    private void setUpSlider() {
        sliderPassMark.setValue(0.0F);
        sliderPassMark.addOnChangeListener((@NonNull Slider slider, float value, boolean fromUser) -> {
            passMark = (int) value;
            txtPassMark.setText(String.format(getString(R.string.pass_Mark), passMark));
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
     * Retrieves and displays all incomplete tasks.
     */
    private void showIncompleteTasks() {
        this.tasks = new ArrayList<>();
        for (Task task : databaseHelper.getTasks(Utils.getInstance().getUserAccount().getId())) {
            if (!task.isCompleted()) {
                this.tasks.add(task);
            }
        }
        taskRecyclerAdapter.setTasks(tasks);
    }

    /**
     * Determines which View object has been clicked and performs the appropriate action.
     *
     * @param view Used to determine the View object clicked.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case (R.id.btnCreate):
                if (taskValidationChecks()) {
                    createTask();
                }
                break;

            case (R.id.imgExit):
                startActivity(new Intent(this, MainMenuActivity.class));
                break;

            default:
                break;
        }
    }

    /**
     * Retrieves the appropriate inputs depending on whether rewards are enabled.
     * Performs the necessary validation checks.
     * These only check whether the data inputted is of an acceptable type.
     * Shows appropriate prompts if any of these checks fail.
     */
    private boolean taskValidationChecks() {
        if (swAddReward.isChecked()) {
            this.inputs = new String[]{edtTxtTaskName.getText().toString(),
                    edtTxtReward.getText().toString()};
        } else {
            this.inputs = new String[]{edtTxtTaskName.getText().toString()};
        }

        if (Utils.getInstance().emptyInputs(inputs)) {
            // If all of the inputs are not filled, they need to check:
            Utils.getInstance().showSnackBar(TaskCreateActivity.this, svCreateTask, getString(R.string.empty_inputs), getString(R.string.ok));
        } else if (Utils.getInstance().inputsInvalid(inputs, 2)) {
            // If input length invalid, they need too check:
            Utils.getInstance().showSnackBar(TaskCreateActivity.this, svCreateTask, getString(R.string.input_lengths), getString(R.string.ok));
        } else if (questionSetId == -1) {
            // If no chosen question set, they need to check:
            Utils.getInstance().showSnackBar(TaskCreateActivity.this, svCreateTask, getString(R.string.choose_qs), getString(R.string.ok));
        } else {
            return true;
        }
        return false;
    }

    /**
     * Hides the virtual keyboard.
     * Retrieves validated data and uses it to create a Task object.
     * Saves this object onto the database.
     */
    private void createTask() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(svCreateTask.getWindowToken(), 0);

        Task task = new Task(questionSetId, Utils.getInstance().getUserAccount().getId(), edtTxtTaskName.getText().toString(), passMark, edtTxtReward.getText().toString(), spQuestionSets.getSelectedItemPosition(), false);
        databaseHelper.addTask(task);
        showIncompleteTasks();
        TransitionManager.beginDelayedTransition(svCreateTask);
    }

    /**
     * Overrides the action to be performed when the back button is pressed.
     * In this case, MainMenuActivity should be started.
     */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainMenuActivity.class));
    }
}
