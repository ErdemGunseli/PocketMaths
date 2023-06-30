package com.example.PocketMaths;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This activity relates to the Sign In Page of the app.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * SignInActivity extends AppCompatActivity class to have access to Activity methods.
 * SignInActivity implements View.OnCLickListener interface to detect touch input.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Prompts the user to sign into a pre-existing account.
 * Validates and verifies user input, displaying appropriate prompts.
 * In a client-server version of the app, the verification would be done server-side.
 * If inputs are verified, locates appropriate account, creating an instance and setting it as active.
 */
public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseHelper databaseHelper = new DatabaseHelper(this);
    private String[] inputs;

    private RelativeLayout relSignIn;
    private EditText edtTxtEmail, edtTxtPassword;
    private Button btnForgotPassword, btnBack, btnConfirm;


    /**
     * Overrides the onCreate method of the super class.
     * Runs when SignInActivity starts.
     * Sets the layout and theme.
     * Calls all necessary functions, either directly or through other functions.
     *
     * @param savedInstanceState Required for super constructor.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Utils.getInstance().getThemeId());
        setContentView(R.layout.activity_sign_in);

        initViews();
    }

    /**
     * Initialises View objects.
     * Sets the activity's click listener to appropriate View objects.
     */
    private void initViews() {
        relSignIn = findViewById(R.id.relSignIn);
        edtTxtEmail = findViewById(R.id.edtTxtEmail);
        edtTxtPassword = findViewById(R.id.edtTxtPassword);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnBack = findViewById(R.id.btnBack);

        btnForgotPassword.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    /**
     * Determines which View object has been clicked and performs the appropriate action.
     *
     * @param view Used to determine the View object clicked.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case (R.id.btnBack):
                finish();
                break;

            case (R.id.btnForgotPassword):
                // No functionality, just the front-end:
                if (Utils.getInstance().isValidEmail(edtTxtEmail.getText().toString())) {
                    Utils.getInstance().showSnackBar(this, relSignIn, getString(R.string.email_sent), getString(R.string.ok));
                } else {
                    // If the email is invalid, prompting user to check:
                    Utils.getInstance().showSnackBar(this, relSignIn, getString(R.string.check_email), getString(R.string.ok));
                }
                break;

            case (R.id.btnConfirm):
                // Calling account validation and verification checks:
                if (!accountValidationChecks()) {
                    break;
                }
                if (accountVerificationChecks()) {
                    startActivity(new Intent(this, MainMenuActivity.class));
                    finish();
                }
                break;

            default:
                break;
        }
    }

    /**
     * Retrieves the appropriate inputs.
     * Performs the many account validation checks.
     * These only check whether the data inputted is of an acceptable type, not if they are
     * actually correct.
     * Shows appropriate prompts if any of these checks fail.
     *
     * @return Whether or not all of the checks are passed.
     */
    private boolean accountValidationChecks() {
        inputs = new String[]{edtTxtEmail.getText().toString(), edtTxtPassword.getText().toString()};

        // Displaying appropriate Snack Bar if inputs are invalid:
        if (Utils.getInstance().emptyInputs(inputs)) {
            // If all of the inputs are not filled, they need to check:
            Utils.getInstance().showSnackBar(this, relSignIn, getString(R.string.empty_inputs), getString(R.string.ok));
        } else if (!Utils.getInstance().isValidEmail(edtTxtEmail.getText().toString())) {
            // If Email invalid, they need to check:
            Utils.getInstance().showSnackBar(this, relSignIn, getString(R.string.check_email), getString(R.string.ok));
        } else if (Utils.getInstance().inputsInvalid(new String[]{edtTxtPassword.getText().toString()}, 6)) {
            // If Password invalid, they need to check:
            Utils.getInstance().showSnackBar(this, relSignIn, getString(R.string.check_password), getString(R.string.ok));
        } else if (databaseHelper.getAccountByEmail(edtTxtEmail.getText().toString()) == null) {
            // If the account of the email address entered does not exist, the user cannot be found:
            Utils.getInstance().showSnackBar(this, relSignIn, getString(R.string.user_not_found), getString(R.string.ok));
        } else {
            return true;
        }
        return false;
    }

    /**
     * Performs account verification checks.
     * These check whether the data entered is correct.
     * Shows prompts if any of the checks fail.
     *
     * @return Whether or not all of the checks are passed.
     */
    private boolean accountVerificationChecks() {
        String email = edtTxtEmail.getText().toString();
        String password = edtTxtPassword.getText().toString();

        // Looking for an account of the same email in the database:
        Account account = databaseHelper.getAccountByEmail(email);

        if (!password.equals(account.getPassword())) {
            // If the account with the email address does exist, but the password is not correct, the user should check the password:
            Utils.getInstance().showSnackBar(this, relSignIn, getString(R.string.check_password), getString(R.string.ok));
        } else {
            Utils.getInstance().setUserAccount(account);
            databaseHelper.useAccount(account.getId());
            return true;
        }
        return false;
    }

}

