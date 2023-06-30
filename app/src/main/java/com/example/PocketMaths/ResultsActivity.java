package com.example.PocketMaths;


import static com.example.PocketMaths.QuestionSetActivity.QUESTION_SET_ID;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

/**
 * This activity relates to the Results Page of the app.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * ResultsActivity extends AppCompatActivity class to have access to Activity methods.
 * ResultsActivity implements View.OnCLickListener interface to detect touch input.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * It displays the result of a question set attempt.
 * It displays the question set attempt in the form of a pie chart.
 * It shows the number of points possible, number of points possible, result in percentage.
 * It gives details of how many questions required a given number of attempts.
 * It gives the user feedback, listing the topics of the questions that the user did great in,
 * and those in which the user made mistakes.
 * It has a 'Review' section in which each question, the number of points it's worth, the number of
 * points the user earned, the user's answer, the correct answer and the question image is shown in
 * the form of a scrollable list.
 * It saves data from the result to an instance of QuestionSetResult and triggers the resetting of
 * the relevant question set.
 */
public class ResultsActivity extends AppCompatActivity implements View.OnClickListener {


    private DatabaseHelper databaseHelper = new DatabaseHelper(this);

    private ScrollView svResults;
    private ImageView imgExit, imgPerfect, imgPractice;
    private PieChart pieResults;
    private TextView txtQuestionSetName, txtResult, txtFirstAttempt, txtSecondAttempt, txtResultPercentage, txtPerfect, txtPractice;
    private QuestionSet questionSet;
    private RecyclerView rvQuestions;
    private Button btnFinish;

    /**
     * Overrides the onCreate method of the super class.
     * Runs when ResultsActivity starts.
     * Sets the layout and theme.
     * Calls all necessary functions, either directly or through other functions.
     *
     * @param savedInstanceState Required for super constructor.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Utils.getInstance().getThemeId());
        setContentView(R.layout.activity_results);

        initViews();

        setDataFromIntent();

        saveQuestionSet();

        setUpRecyclerView();
    }

    /**
     * Initialises View objects.
     * Sets the activity's click listener to appropriate View objects.
     */
    private void initViews() {
        svResults = findViewById(R.id.svResults);
        rvQuestions = findViewById(R.id.rvQuestions);
        imgExit = findViewById(R.id.imgExit);
        txtQuestionSetName = findViewById(R.id.txtQuestionSetName);
        txtResult = findViewById(R.id.txtResult);
        imgPerfect = findViewById(R.id.imgPerfect);
        imgPractice = findViewById(R.id.imgPractice);
        txtFirstAttempt = findViewById(R.id.txtFirstAttempt);
        txtSecondAttempt = findViewById(R.id.txtSecondAttempt);
        txtResultPercentage = findViewById(R.id.txtResultPercentage);
        txtPerfect = findViewById(R.id.txtPerfect);
        txtPractice = findViewById(R.id.txtPractice);
        btnFinish = findViewById(R.id.btnFinish);
        pieResults = findViewById(R.id.pieResults);

        imgExit.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
    }

    /**
     * Sets the appropriate instance of QuestionSet depending on extra data from the Intent instance.
     * This activity can only be started by passing the question set Id through the Intent.
     */
    private void setDataFromIntent() {
        Intent intent = getIntent();

        if (intent != null) {

            int questionSetID = intent.getIntExtra(QUESTION_SET_ID, -1);

            if (questionSetID != -1) {
                questionSet = Utils.getInstance().getQuestionSetById(questionSetID);
                if (questionSet != null) {
                    setData(questionSet);
                }
            }
        }
    }

    /**
     * Uses data from the QuestionSet instance to create a QuestionSetResult object which is added
     * to the database.
     */
    private void saveQuestionSet() {
        // We are passing 0 as the ID because the correct one will be set in the database.
        QuestionSetResult questionSetResult = new QuestionSetResult(
                0,
                questionSet.getId(),
                Utils.getInstance().getUserAccount().getId(),
                questionSet.calculatePointsEarned(),
                questionSet.calculatePointsPossible(),
                questionSet.calculateNumberOfQuestionsSolved()[0],
                questionSet.calculateNumberOfQuestionsSolved()[1],
                questionSet.calculateNumberOfQuestionsSolved()[2],
                DateFormat.getDateInstance().format(Calendar.getInstance().getTime())
        );
        databaseHelper.addQuestionSetResult(questionSetResult, Utils.getInstance().getUserAccount().getId());
    }

    /**
     * Performs the setting-up of the RecyclerView, using ResultsRecyclerAdapter.
     */
    private void setUpRecyclerView() {
        ResultsRecyclerAdapter resultsRecyclerAdapter = new ResultsRecyclerAdapter(this);
        resultsRecyclerAdapter.setQuestionSet(questionSet);

        rvQuestions.setAdapter(resultsRecyclerAdapter);

        rvQuestions.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Sets the data from the instance of QuestionSet retrieved from the Intent object.
     * Sets the visibility of View objects depending on external factors.
     * Causes the creation and the handling of the relevant pie chart.
     * Displays all feedback messages.
     * Shows the topics that the user
     *
     * @param questionSet The question set that has just been completed.
     */
    private void setData(QuestionSet questionSet) {

        completeTask();

        txtQuestionSetName.setText(questionSet.getName());
        txtResult.setText(String.format(getString(R.string.x_points_out_of_x), questionSet.calculatePointsEarned(), questionSet.calculatePointsPossible()));
        txtFirstAttempt.setText(String.format(getString(R.string.solved_x_out_of_x_1attempt), questionSet.calculateNumberOfQuestionsSolved()[0], questionSet.getQuestions().length));

        // Calculating the remaining questions:
        int remaining = (questionSet.getQuestions().length - questionSet.calculateNumberOfQuestionsSolved()[0]);

        // Only present if score isn't 100%
        if (remaining > 0) {
            txtSecondAttempt.setVisibility(View.VISIBLE);
            // Solved X out of the remaining X with two attempts.
            txtSecondAttempt.setText(String.format(getString(R.string.solved_x_out_of_x_2attempts), questionSet.calculateNumberOfQuestionsSolved()[1], (remaining)));
        } else {
            txtSecondAttempt.setVisibility(View.GONE);
        }

        // Result: X%
        txtResultPercentage.setText(String.format(getString(R.string.result_percent), questionSet.calculateResult()));

        ArrayList<String> failedTopics = questionSet.getFailedTopics();
        ArrayList<String> perfectTopics = new ArrayList<>();

        // Adding failed topics from all topics to a new ArrayList to get perfect topics:
        // Elaborate since ArrayLists are pointer-based and any other technique does not work.
        for (String topic : questionSet.getTopics()) {
            if (!failedTopics.contains(topic)) {
                perfectTopics.add(topic);
            }
        }

        if (failedTopics.size() == 0) {
            // Invisible instead of gone to maintain layout
            txtPractice.setVisibility(View.GONE);
            imgPractice.setVisibility(View.GONE);
        } else {
            txtPractice.setVisibility(View.VISIBLE);
            imgPractice.setVisibility(View.VISIBLE);

            int count = 1;
            for (String topic : failedTopics) {
                txtPractice.append(String.format(getString(R.string.list), count, topic));
                count++;
            }
        }

        if (perfectTopics.size() == 0) {
            txtPerfect.setVisibility(View.GONE);
            imgPerfect.setVisibility(View.GONE);
        } else {
            txtPerfect.setVisibility(View.VISIBLE);
            imgPerfect.setVisibility(View.VISIBLE);

            int count = 1;
            for (String perfectTopic : perfectTopics) {
                txtPerfect.append(String.format(getString(R.string.list), count, perfectTopic));
                count++;
            }
        }

        // Creating values for the pie chart:
        int firstAttempt = questionSet.calculateNumberOfQuestionsSolved()[0];
        int secondAttempt = questionSet.calculateNumberOfQuestionsSolved()[1];
        int moreAttempts = questionSet.getQuestions().length - (questionSet.calculateNumberOfQuestionsSolved()[0] + questionSet.calculateNumberOfQuestionsSolved()[1]);

        int[] values = {firstAttempt, secondAttempt, moreAttempts};
        String[] labels = {getString(R.string.first_attempt), getString(R.string.second_attempt), getString(R.string.other)};

        // Creating the Pie Chart
        Utils.getInstance().createPieChart(this, pieResults,
                values,
                13,
                "",
                0,
                labels,
                13,
                R.color.Secondary,
                getString(R.string.results),
                16,
                R.color.Primary,
                R.color.Silver);
    }

    /**
     * Retrieves all tasks from the database.
     * Completes the first task for which the pass mark has been met, if the task is associated with
     * the question set that the user just completed.
     * Only completes one task - all tasks for a given question set should be completed separately.
     */
    private void completeTask() {
        ArrayList<Task> tasks = databaseHelper.getTasks(Utils.getInstance().getUserAccount().getId());

        // If the question set ID of the task matches that of the completed task, and the pass mark
        // is reached, complete the question set.
        for (Task task : tasks) {
            if (Objects.requireNonNull(Utils.getInstance().getQuestionSetById(task.getQuestionSetId())).getId() == questionSet.getId() &&
                    questionSet.calculateResult() >= task.getPassMark() && !task.isCompleted()) {
                Utils.getInstance().showSnackBar(this, svResults, String.format(getString(R.string.task_completed), task.getName()), getString(R.string.ok));

                // Only completing one task - all of them should not be able to be completed simultaneously:
                databaseHelper.completeTask(task);
                break;
            }
        }
    }

    /**
     * Determines which View object has been clicked and performs the appropriate action.
     *
     * @param view Used to determine the View object clicked.
     */
    @Override
    public void onClick(View view) {
        int v = view.getId();
        if (v == R.id.imgExit || v == R.id.btnFinish) {
            onBackPressed();
        }
    }


    /**
     * Overrides the action to be performed when the back button is pressed.
     * In this case, nothing should should be done.
     */
    @Override
    public void onBackPressed() {
        questionSet.reset();
        startActivity(new Intent(this, MainMenuActivity.class));
        finish();
    }
}
