package com.example.PocketMaths;

import static com.example.PocketMaths.Question.MULTIPLE_CHOICE;
import static com.example.PocketMaths.Question.WRITTEN;
import static com.example.PocketMaths.RefresherActivity.REFRESHER_ID;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Html;
import android.text.InputType;
import android.transition.TransitionManager;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * This activity relates to the Question Set Page of the app.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * QuestionSetActivity extends AppCompatActivity class to have access to Activity methods.
 * QuestionSetActivity implements View.OnCLickListener interface to detect touch input.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * It displays one question at a time for a given question set.
 * It causes the displaying of any refreshers associated with a given question.
 * It displays the question text, the question set name, the index of the question, number of points possible
 * It displays any images associated with the question.
 * It has arrow buttons to navigate through the question set
 * If the question is multiple choice, it displays the options.
 * If the question is written, it displays the answer box as well as the 'Show Answer' Button.
 * Depending on the checks for each question, it displays appropriate icons to show the result.
 */
public class QuestionSetActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String QUESTION_SET_ID = "questionSetID";
    private QuestionSet questionSet;
    private Question currentQuestion;
    private boolean inTransition = false;
    private DecimalFormat decimalFormat = new DecimalFormat("#.###");

    private RelativeLayout relQuestionSet, relQuestionAnswerOptions, relWrittenAnswer;
    private ScrollView svQuestionSet;
    private TextView txtQuestion, txtQuestionSetName, txtCurrentQuestionIndex, txtPointsPossible, txtMessage;
    private Button btnConfirm, btnRevealAnswer;
    private ImageView imgExit, imgQuestion, imgPrevious, imgNext, imgHelp, imgResult;
    private EditText edtTxtAnswer;
    private RadioButton[] radioButtons;
    private RadioGroup radioGroup;


    /**
     * Overrides the onCreate method of the super class.
     * Runs when QuestionSetActivity starts.
     * Sets the layout and theme.
     * Calls all necessary functions, either directly or through other functions.
     *
     * @param savedInstanceState Required for super constructor.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Utils.getInstance().getThemeId());
        setContentView(R.layout.activity_question_set);

        initViews();

        setDataFromIntent();
    }

    /**
     * Initialises View objects.
     * Sets the activity's click listener to appropriate View objects.
     * Sets custom EditText input type.
     */
    private void initViews() {
        relQuestionSet = findViewById(R.id.relQuestionSet);
        relQuestionAnswerOptions = findViewById(R.id.relQuestionAnswerOptions);
        relWrittenAnswer = findViewById(R.id.relWrittenAnswer);
        svQuestionSet = findViewById(R.id.svQuestionSet);
        txtQuestionSetName = findViewById(R.id.txtQuestionSetName);
        txtCurrentQuestionIndex = findViewById(R.id.txtCurrentQuestionIndex);
        txtPointsPossible = findViewById(R.id.txtPointsPossible);
        txtQuestion = findViewById(R.id.txtQuestion);
        txtMessage = findViewById(R.id.txtMessage);
        imgExit = findViewById(R.id.imgExit);
        imgQuestion = findViewById(R.id.imgQuestion);
        imgPrevious = findViewById(R.id.imgPrevious);
        imgNext = findViewById(R.id.imgNext);
        imgHelp = findViewById(R.id.imgHelp);
        imgResult = findViewById(R.id.imgResult);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnRevealAnswer = findViewById(R.id.btnRevealAnswer);
        edtTxtAnswer = findViewById(R.id.edtTxtAnswer);

        imgExit.setOnClickListener(this);
        imgPrevious.setOnClickListener(this);
        imgNext.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        btnRevealAnswer.setOnClickListener(this);
        imgHelp.setOnClickListener(this);

        // Setting the EditText input type here to allow signed decimals.
        edtTxtAnswer.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
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
     * Sets data from the current instance of Question.
     * Formats the question text such that subscripts and superscripts can be displayed correctly.
     * Performs various additional actions such as hiding the keyboard, scrolling to the top etc.
     *
     * @param questionSet The question set from which the data should be set.
     */
    private void setData(QuestionSet questionSet) {
        // Scrolling to the top:
        svQuestionSet.smoothScrollTo(0, 0);

        currentQuestion = questionSet.getQuestions()[questionSet.getCurrentQuestionIndex()];

        setResultImage();
        showRefresher();

        //Hiding Keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(svQuestionSet.getWindowToken(), 0);

        if (currentQuestion.getType().equals(MULTIPLE_CHOICE)) {
            createRadioButtons();
            showMultipleChoice();
            checkRelevantRadioButton();
        } else if (currentQuestion.getType().equals(WRITTEN)) {
            displayRelevantAnswer();
            showWritten();
        }

        txtMessage.setText("");
        txtQuestionSetName.setText(questionSet.getName());
        txtQuestion.setText(Html.fromHtml(currentQuestion.getText()));


        txtCurrentQuestionIndex.setText(String.format(getString(R.string.question_x_of_x), (questionSet.getCurrentQuestionIndex() + 1), questionSet.getQuestions().length));

        setPointsPossible();

        imgQuestion.setImageResource(currentQuestion.getImageId());

        setNavigationVisibility();


    }

    /**
     * Creates RadioButton instances and sets the appropriate data to it depending on the number and data
     * from each answer option.
     */
    private void createRadioButtons() {
        String[] answerOptions = currentQuestion.getAnswerOptions();
        radioButtons = new RadioButton[answerOptions.length];
        radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.VERTICAL);

        for (int index = 0; index < answerOptions.length; index++) {
            radioButtons[index] = new RadioButton(this);
            radioButtons[index].setTextColor(getResources().getColor(R.color.Secondary));
            radioButtons[index].setTextSize(16);
            radioButtons[index].setPadding(0, 20, 0, 20);
            radioButtons[index].setText(Html.fromHtml(currentQuestion.getAnswerOptions()[index]));
            radioGroup.addView(radioButtons[index]);
        }
        relQuestionAnswerOptions.addView(radioGroup);
    }

    private void removeRadioButtons(){
        relQuestionAnswerOptions.removeView(radioGroup);
    }


    /**
     * Hides back arrow in the first question.
     * Hides the forward arrow in the last question.
     * Changes the 'Change' Button to 'Finish' in the last question.
     */
    private void setNavigationVisibility() {
        if (firstQuestion()) {
            btnConfirm.setText(getString(R.string.check));
            imgNext.setVisibility(View.VISIBLE);
            imgPrevious.setVisibility(View.GONE);
        } else if (lastQuestion()) {
            btnConfirm.setText(getString(R.string.finish));
            imgNext.setVisibility(View.GONE);
            imgPrevious.setVisibility(View.VISIBLE);
        } else {
            btnConfirm.setText(getString(R.string.check));
            imgPrevious.setVisibility(View.VISIBLE);
            imgNext.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Required for button check persistence whilst navigating back and forth through the question
     * set.
     * Checks the relevant radio button depending on the user answer.
     * If the question has been answered correctly, checks the correct answer.
     * Otherwise, checks the user's last attempt.
     */
    private void checkRelevantRadioButton() {
        int indexToDisplay;
        radioGroup.clearCheck();

        if (currentQuestion.getAttempts() > 0) {
            if (currentQuestion.getPointsEarned() != 0) {
                indexToDisplay = currentQuestion.getCorrectAnswerIndex();
            } else {
                ArrayList<Integer> userAnswers = currentQuestion.getUserAnswerIndexes();
                indexToDisplay = userAnswers.get(userAnswers.size() - 1);
            }
            RadioButton targetButton = radioButtons[indexToDisplay];
            targetButton.setChecked(true);
        }
    }

    /**
     * Required for written answer persistence whilst navigating back and forth throughout the question
     * set.
     * Displays the relevant answer in the answer box.
     * If the user has answered the question correctly, displays the correct answer.
     * Otherwise, displays the user's last attempt.
     */
    private void displayRelevantAnswer() {
        if (currentQuestion.getAttempts() > 0) {
            if (currentQuestion.getPointsEarned() != 0) {
                edtTxtAnswer.setText(decimalFormat.format(currentQuestion.getCorrectWrittenAnswer()));
            } else {
                ArrayList<Float> userAnswers = currentQuestion.getUserWrittenAnswers();
                edtTxtAnswer.setText(String.valueOf(decimalFormat.format(userAnswers.get(userAnswers.size() - 1))));
            }
        } else {
            edtTxtAnswer.setText("");
        }
    }


    /**
     * Hides the View elements pertaining to written questions and displays those for multiple
     * choice questions.
     */
    private void showMultipleChoice() {
        relQuestionAnswerOptions.setVisibility(View.VISIBLE);
        relWrittenAnswer.setVisibility(View.GONE);
    }

    /**
     * Hides the View elements pertaining to multiple choice questions and displays those for written
     * questions.
     */
    private void showWritten() {
        relWrittenAnswer.setVisibility(View.VISIBLE);
        relQuestionAnswerOptions.setVisibility(View.GONE);

        edtTxtAnswer.clearFocus();
    }

    /**
     * Displays the number of points possible given the number of attempts made.
     * This is done independently from the pointsPossible attribute of the QuestionSet instance,
     * as this must be preserved for correctly resetting the QuestionSet instance.
     */
    private void setPointsPossible() {
        // If no points have been earned:
        if (currentQuestion.getPointsEarned() == 0) {
            // If the question has not been attempted, displaying the full points possible:
            if (currentQuestion.getAttempts() == 0) {
                txtPointsPossible.setText(String.format(getString(R.string.points), (currentQuestion.getPointsPossible())));
            } else if (currentQuestion.getAttempts() == 1) {
                // If the question has been attempted once, displaying half the initial points possible:
                txtPointsPossible.setText(String.format(getString(R.string.points), currentQuestion.getPointsPossible() / 2));
            } else {
                // If the question set has been attempted more than once, displaying 0 points:
                txtPointsPossible.setText(String.format(getString(R.string.points), 0));
            }
        } else {
            // If points have been earned, displaying the number of points earned.
            txtPointsPossible.setText(String.format(getString(R.string.points), currentQuestion.getPointsEarned()));
        }

    }

    /**
     * Shows a tick if the question has been answered correctly.
     * Shows a negative sign if the question has been answered incorrectly.
     * Otherwise, shows no result image.
     */
    private void setResultImage() {

        if ((currentQuestion.getPointsEarned() == 0 && currentQuestion.getAttempts() > 0) || currentQuestion.answerShown()) {
            imgResult.setImageResource(R.drawable.ic_practice);
        } else if (currentQuestion.getPointsEarned() > 0) {
            imgResult.setImageResource(R.drawable.ic_perfect);
        } else {
            imgResult.setImageResource(0);
        }

    }

    /**
     * If refreshers have been enabled by the user, and there is a Refresher instance linked to the
     * topic of this question, and that refresher has not already been shown in this session, start
     * RefresherActivity to automatically display the refresher.
     * Repeat this process for however many such refreshers there are.
     */
    private void showRefresher() {
        // If refreshers are not enabled, returning:
        if (!Utils.getInstance().refreshersEnabled()) {
            return;
        }
        for (Refresher refresher : questionSet.getRefreshers()) {
            // If a refresher is supposed to be shown before this question,
            // and it has not been shown this time, showing it:
            if (refresher.getTopic().equals(currentQuestion.getTopic()) && !refresher.isShown()) {
                startActivity(new Intent(this, RefresherActivity.class)
                        .putExtra(REFRESHER_ID, refresher.getId()));
                refresher.setShown(true);
            }
        }
    }

    /**
     * Causes the displaying of refreshers for the topic of the current question.
     */
    private void viewLastRefresher() {
        ArrayList<Refresher> refreshers = questionSet.getRefreshers();

        int maxRefresherIndex = -1;
        int currentRefresherIndex = 0;
        for (Refresher refresher : refreshers) {
            if (currentQuestion.getTopic().equals(refresher.getTopic())) {
                maxRefresherIndex = currentRefresherIndex;
            }
            currentRefresherIndex++;
        }
        if (maxRefresherIndex != -1) {
            startActivity(new Intent(this, RefresherActivity.class)
                    .putExtra(REFRESHER_ID, refreshers.get(maxRefresherIndex).getId()));
        }
    }

    /**
     * Determines if the current question is the first question.
     *
     * @return Whether or not the current question is the first one.
     */
    private boolean firstQuestion() {
        return questionSet.getCurrentQuestionIndex() == 0;
    }

    /**
     * Determines of the current question is the last question.
     *
     * @return Whether or not the current question is the last one.
     */
    private boolean lastQuestion() {
        return questionSet.getCurrentQuestionIndex() == questionSet.length() - 1;
    }

    /**
     * Determines which View object has been clicked and performs the appropriate action.
     *
     * @param view Used to determine the View object clicked.
     */
    @Override
    public void onClick(View view) {
        if (inTransition) {
            return;
        }

        switch (view.getId()) {

            case (R.id.imgExit):
                startActivity(new Intent(this, MainMenuActivity.class));
                finish();
                break;

            case (R.id.imgPrevious):
                // Decreasing the current question index by 1:
                removeRadioButtons();
                questionSet.setCurrentQuestionIndex(questionSet.getCurrentQuestionIndex() - 1);
                TransitionManager.beginDelayedTransition(relQuestionSet);
                setData(questionSet);
                break;

            case (R.id.imgNext):
                // If the current question has been attempted, going to the next:
                if (currentQuestion.getAttempts() > 0) {
                    removeRadioButtons();
                    // Increasing the current question index by 1
                    questionSet.setCurrentQuestionIndex(questionSet.getCurrentQuestionIndex() + 1);

                    TransitionManager.beginDelayedTransition(relQuestionSet);
                    setData(questionSet);

                } else {
                    // Otherwise, displaying message:
                    TransitionManager.beginDelayedTransition(relQuestionSet);
                    txtMessage.setText(getString(R.string.try_first));
                }
                break;

            case (R.id.btnConfirm):
                // Error catching if input is invalid:
                try {
                    questionFeedback();
                } catch (Exception e) {
                    TransitionManager.beginDelayedTransition(relQuestionSet);
                    txtMessage.setText(getString(R.string.invalid_answer));
                    vibrate();
                }
                break;

            case (R.id.btnRevealAnswer):
                //Only losing points if they have not earned points:

                if (currentQuestion.getPointsEarned() == 0) {
                    currentQuestion.setAnswerShown(true);
                    txtPointsPossible.setText(String.format(getString(R.string.points), 0));
                }

                TransitionManager.beginDelayedTransition(relQuestionSet);
                txtMessage.setText(Html.fromHtml(decimalFormat.format(currentQuestion.getCorrectWrittenAnswer())));
                setResultImage();
                break;

            case (R.id.imgHelp):
                viewLastRefresher();
                break;

            default:
                break;
        }

    }

    /**
     * After the user has clicked the 'Check' button, performs many actions:
     * Calls the Question instance methods to check the question.
     * Displays points possible.
     * Provides Haptic Feedback if the question is answered incorrectly.
     * Provides a delay after the question is answered.
     * Displays various messages.
     */
    private void questionFeedback() {
        // If the question isn't left empty:
        if ((currentQuestion.getType().equals(MULTIPLE_CHOICE) && getCheckedRadioButtonIndex() != -1)
                || (currentQuestion.getType().equals(WRITTEN) && !edtTxtAnswer.getText().toString().equals(""))) {

            boolean answerCorrect;

            if (currentQuestion.getType().equals(MULTIPLE_CHOICE)) {
                answerCorrect = currentQuestion.checkMultipleChoiceAnswer(getCheckedRadioButtonIndex());
            } else {
                answerCorrect = currentQuestion.checkWrittenAnswer(Float.parseFloat(String.valueOf(edtTxtAnswer.getText())));
            }


            // If the question has been answered correctly:
            if (answerCorrect) {
                // If this is the last question, start ResultsActivity:
                if (lastQuestion()) {
                    questionSet.setCurrentQuestionIndex(0);
                    startActivity(new Intent(this, ResultsActivity.class)
                            .putExtra(QUESTION_SET_ID, questionSet.getId()));
                    finish();
                } else {
                    TransitionManager.beginDelayedTransition(relQuestionSet);
                    txtMessage.setText("");

                    // Otherwise, increase the question index by 1:
                    questionSet.setCurrentQuestionIndex(questionSet.getCurrentQuestionIndex() + 1);

                    inTransition = true;
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        removeRadioButtons();
                        TransitionManager.beginDelayedTransition(relQuestionSet);
                        inTransition = false;
                        setData(questionSet);
                    }, 1000);
                }
            } else {
                // If the question has been answered incorrectly:
                TransitionManager.beginDelayedTransition(relQuestionSet);
                txtMessage.setText(getString(R.string.try_again));

                vibrate();

                setPointsPossible();
            }
        } else {
            // If the question has been left empty, showing message:
            TransitionManager.beginDelayedTransition(relQuestionSet);
            txtMessage.setText(getString(R.string.enter_answer));
        }
        setResultImage();
    }

    /**
     * Returns the index of the checked RadioButton instance.
     * If none are checked, returns -1.
     *
     * @return The index of the checked RadioButton instance.
     */
    private int getCheckedRadioButtonIndex() {
        for (int index = 0; index < radioButtons.length; index++) {
            if (radioButtons[index].isChecked()) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Creates haptic feedback.
     * Uses the appropriate method depending on the device version.
     */
    private void vibrate() {
        // Creating haptic feedback:
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(75, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            // Old API:
            vibrator.vibrate(75);
        }
    }

    /**
     * Overrides the action to be performed when the back button is pressed.
     * In this case, MainMenuActivity should be started.
     */
    @Override
    public void onBackPressed() {
        // The back button should go to the main menu here:
        startActivity(new Intent(this, MainMenuActivity.class));
        finish();
    }
}