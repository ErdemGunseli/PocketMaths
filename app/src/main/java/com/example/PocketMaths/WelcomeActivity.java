package com.example.PocketMaths;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This activity relates to the Welcome Page of the app.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * WelcomeActivity extends AppCompatActivity class to have access to Activity methods.
 * WelcomeActivity implements View.OnCLickListener interface to detect touch input.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * It greets the user.
 * It prompts the user to sign-up or sign-in.
 * It provides the user the option to 'Continue as Guest'.
 * If the user already has an account with which they have already signed into the device, it
 * launches MainMenuActivity automatically.
 */
public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnSignUp, btnSignIn, btnContinueAsGuest;

    /**
     * Overrides the onCreate method of the super class.
     * Runs when AccountActivity starts.
     * If the user has not already signed in, sets the layout and theme and calls all necessary
     * functions, either directly or through other functions.
     * Otherwise, automatically signs the user in.
     * The user can sign out from the Account page if they no longer want to be signed in automatically.
     *
     * @param savedInstanceState Required for super constructor.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        Account account = databaseHelper.getCurrentAccount();
        if (account != null) {
            Utils.getInstance().setUserAccount(account);
            startActivity(new Intent(this, MainMenuActivity.class));
            finish();
        } else {
            setTheme(Utils.getInstance().getThemeId());
            setContentView(R.layout.activity_welcome);

            initViews();
        }
    }

    /**
     * Initialises View objects.
     * Sets the activity's click listener to appropriate View objects.
     */
    private void initViews() {
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnContinueAsGuest = findViewById(R.id.btnContinueAsGuest);

        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        btnContinueAsGuest.setOnClickListener(this);
    }

    /**
     * Determines which View object has been clicked and performs the appropriate action.
     *
     * @param view Used to determine the View object clicked.
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case (R.id.btnSignUp):
                startActivity(new Intent(this, SignUpActivity.class));
                break;

            case (R.id.btnSignIn):
                startActivity(new Intent(this, SignInActivity.class));
                break;

            case (R.id.btnContinueAsGuest):
                // Creating a guest account:
                Utils.getInstance().setUserAccount(new Account());
                startActivity(new Intent(this, MainMenuActivity.class));
                finish();
                break;
            default:
                break;

        }

    }

}