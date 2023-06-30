package com.example.PocketMaths;

import static com.example.PocketMaths.Account.Member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

/**
 * This activity relates to the Account Page of the app.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * AccountActivity extends AppCompatActivity class to have access to Activity methods.
 * AccountActivity implements View.OnCLickListener interface to detect touch input.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * It displays all instances of QuestionSetResult stored in the relational database, associated with
 * the instance of Account set as active.
 * If there are none, it prompts the user to try some questions.
 * If there is an active instance of Account, it displays user account details.
 * If there is none, it prompts the user to sign-in or sign-up.
 * It has the option to change user account details, targeting ChangeDetailsActivity through
 * PinVerificationActivity.
 * It has the option to sign out, which clears the contents of CurrentAccount table from the
 * relational database.
 */

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private Account account = Utils.getInstance().getUserAccount();
    private DatabaseHelper databaseHelper = new DatabaseHelper(this);

    private RecyclerView rvAccountHistory;
    private TextView txtDetails;
    private Button btnTryQuestions, btnChangeDetails, btnSignOut, btnSignUp, btnSignIn;
    private ImageView imgExit;

    /**
     * Overrides the onCreate method of the super class.
     * Runs when AccountActivity starts.
     * Sets the layout and theme.
     * Calls all necessary functions, either directly or through other functions.
     *
     * @param savedInstanceState Required for super constructor.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Utils.getInstance().getThemeId());
        setContentView(R.layout.activity_account);

        initViews();

        setUpRecyclerView();

        setData();
    }

    /**
     * Initialises View objects.
     * Sets the activity's click listener to appropriate View objects.
     */
    private void initViews() {
        rvAccountHistory = findViewById(R.id.rvAccountHistory);
        txtDetails = findViewById(R.id.txtDetails);
        btnTryQuestions = findViewById(R.id.btnTryQuestions);
        btnChangeDetails = findViewById(R.id.btnChangeDetails);
        btnSignOut = findViewById(R.id.btnSignOut);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignIn = findViewById(R.id.btnSignIn);
        imgExit = findViewById(R.id.imgExit);

        btnTryQuestions.setOnClickListener(this);
        btnChangeDetails.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        imgExit.setOnClickListener(this);
    }

    /**
     * Performs the setting-up of the RecyclerView, using AccountHistoryRecyclerAdapter.
     */
    private void setUpRecyclerView() {
        AccountHistoryRecyclerAdapter accountHistoryRecyclerAdapter = new AccountHistoryRecyclerAdapter(this);
        accountHistoryRecyclerAdapter.setQuestionSets(databaseHelper.getQuestionSetResults());

        rvAccountHistory.setAdapter(accountHistoryRecyclerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        // Reversing the order of the ArrayList, since the last question set done should be displayed first:
        linearLayoutManager.setReverseLayout(true);
        rvAccountHistory.setLayoutManager(linearLayoutManager);

        // Scrolling to the bottom of the array, top of the RecyclerView:
        rvAccountHistory.scrollToPosition(databaseHelper.getQuestionSetResults().size() - 1);
    }

    /**
     * Sets user account details to the appropriate View object.
     * Sets the visibility of certain View objects and displays appropriate prompts depending on
     * external factors.
     */
    private void setData() {
        // If no question sets have been completed yet,
        // hiding the RecyclerView and displaying 'Try Questions' Button:
        if (Objects.requireNonNull(rvAccountHistory.getAdapter()).getItemCount() == 0) {
            rvAccountHistory.setVisibility(View.GONE);
            btnTryQuestions.setVisibility(View.VISIBLE);
        } else {
            // Otherwise, reverse this process:
            rvAccountHistory.setVisibility(View.VISIBLE);
            btnTryQuestions.setVisibility(View.GONE);
        }

        if (account.getAccountType().equals(Member)) {
            // If the Account type is Member, displaying the user account details,
            // the 'Change Details' Button, and the 'Sign Out' Button:
            txtDetails.setVisibility(View.VISIBLE);
            btnChangeDetails.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.VISIBLE);

            btnSignUp.setVisibility(View.GONE);
            btnSignIn.setVisibility(View.GONE);

            txtDetails.setText(String.format(getString(R.string.view_account_details),
                    account.getParentName(),
                    account.getStudentName(),
                    account.getEmail()
            ));
        } else {
            // Otherwise, reversing this process:
            txtDetails.setVisibility(View.GONE);
            btnChangeDetails.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.GONE);

            btnSignUp.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Determines which View object has been clicked and performs the appropriate action.
     *
     * @param view Used to determine the View object clicked.
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case (R.id.btnTryQuestions):
            case (R.id.imgExit):
                startActivity(new Intent(this, MainMenuActivity.class));
                finish();
                break;

            case (R.id.btnSignUp):
                startActivity(new Intent(this, SignUpActivity.class));
                break;

            case (R.id.btnSignIn):
                startActivity(new Intent(this, SignInActivity.class));
                break;

            case (R.id.btnChangeDetails):
                // Setting the target class to ChangeDetailsActivity:
                Utils.getInstance().setTargetClass(ChangeDetailsActivity.class);
                startActivity(new Intent(this, PinVerificationActivity.class));
                break;

            case (R.id.btnSignOut):
                Utils.getInstance().setUserAccount(new Account());
                databaseHelper.removeCurrentAccount();
                startActivity(new Intent(this, WelcomeActivity.class));
                finish();
                break;

            default:
                break;
        }

    }
}
