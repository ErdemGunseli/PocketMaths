package com.example.PocketMaths;

import static com.example.PocketMaths.Account.Guest;
import static com.example.PocketMaths.Account.Member;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

/**
 * This activity relates to the Sign Up Page of the app.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * SignUpActivity extends AppCompatActivity class to have access to Activity methods.
 * SignUpActivity implements View.OnCLickListener interface to detect touch input.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Prompts the user to create a new account.
 * Validates and verifies user input, displaying appropriate prompts.
 * In a client-server version of the app, the verification would be done server-side.
 * If inputs are verified, creates a new instance of Account, saves it to the relational database
 * and sets it as active.
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] inputs;
    private DatabaseHelper databaseHelper = new DatabaseHelper(this);

    private RelativeLayout relSignUp;
    private EditText edtTxtParentName, edtTxtStudentName, edtTxtEmail, edtTxtPassword, edtTxtConfirmPassword, edtTxtPin, edtTxtConfirmPin;
    private Button btnBack, btnConfirm;


    /**
     * Overrides the onCreate method of the super class.
     * Runs when SignUpActivity starts.
     * Sets the layout and theme.
     * Calls all necessary functions, either directly or through other functions.
     *
     * @param savedInstanceState Required for super constructor.
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Utils.getInstance().getThemeId());
        setContentView(R.layout.activity_sign_up);

        initViews();
    }

    /**
     * Initialises View objects.
     * Sets the activity's click listener to appropriate View objects.
     */
    private void initViews() {
        relSignUp = findViewById(R.id.relSignUp);
        edtTxtParentName = findViewById(R.id.edtTxtParentName);
        edtTxtStudentName = findViewById(R.id.edtTxtStudentName);
        edtTxtEmail = findViewById(R.id.edtTxtEmail);
        edtTxtPassword = findViewById(R.id.edtTxtPassword);
        edtTxtConfirmPassword = findViewById(R.id.edtTxtConfirmPassword);
        edtTxtPin = findViewById(R.id.edtTxtPin);
        edtTxtConfirmPin = findViewById(R.id.edtTxtConfirmPin);
        btnBack = findViewById(R.id.btnBack);
        btnConfirm = findViewById(R.id.btnConfirm);

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

            case (R.id.btnConfirm):
                if (accountChecks()) {
                    updateAccount();
                }
                break;

            default:
                break;
        }
    }


    /**
     * Retrieves the appropriate inputs.
     * Performs account validation and verification checks.
     * These check whether some data is acceptable, and whether other data is correct.
     * Shows prompts if any of the checks fail.
     *
     * @return Whether or not all of the checks are passed.
     */
    private boolean accountChecks() {
        inputs = new String[]{edtTxtParentName.getText().toString(),
                edtTxtStudentName.getText().toString(),
                edtTxtEmail.getText().toString(),
                edtTxtPassword.getText().toString(),
                edtTxtConfirmPassword.getText().toString(),
                edtTxtPin.getText().toString(),
                edtTxtConfirmPin.getText().toString()};

        // Displays appropriate Snack Bar if inputs are invalid:
        if (Utils.getInstance().emptyInputs(inputs)) {
            // If all of the inputs are not filled, they need to check:
            Utils.getInstance().showSnackBar(this, relSignUp, getString(R.string.empty_inputs), getString(R.string.ok));
        } else if (Utils.getInstance().inputsInvalid(new String[]{edtTxtParentName.getText().toString(), edtTxtStudentName.getText().toString()}, 2)) {
            // If input length invalid, they need to check:
            Utils.getInstance().showSnackBar(this, relSignUp, getString(R.string.input_lengths), getString(R.string.ok));
        } else if (Utils.getInstance().inputsInvalid(new String[]{edtTxtPassword.getText().toString()}, 6)) {
            // If Password too short, they need to check:
            Utils.getInstance().showSnackBar(this, relSignUp, getString(R.string.check_password_length), getString(R.string.ok));
        } else if (!Utils.getInstance().isValidEmail(edtTxtEmail.getText().toString())) {
            // If email invalid, they need to check:
            Utils.getInstance().showSnackBar(this, relSignUp, getString(R.string.check_email), getString(R.string.ok));
        } else if (!edtTxtPassword.getText().toString().equals(edtTxtConfirmPassword.getText().toString())) {
            // If Passwords do not match, they need to check:
            Utils.getInstance().showSnackBar(this, relSignUp, getString(R.string.passwords_do_not_match), getString(R.string.ok));
        } else if (Utils.getInstance().inputsInvalid(new String[]{edtTxtPin.getText().toString()}, 4)) {
            // If the pin is not 4 digits, they need to check:
            Utils.getInstance().showSnackBar(this, relSignUp, getString(R.string.pin_length), getString(R.string.ok));
        } else if (!edtTxtPin.getText().toString().equals(edtTxtConfirmPin.getText().toString())) {
            // If the pins do not match, they need to check:
            Utils.getInstance().showSnackBar(this, relSignUp, getString(R.string.pins_do_not_match), getString(R.string.ok));
        } else if (databaseHelper.getAccountByEmail(edtTxtEmail.getText().toString()) != null) {
            //
            Utils.getInstance().showSnackBar(this, relSignUp, getString(R.string.email_registered), getString(R.string.ok));
        } else {
            return true;
        }
        return false;

    }


    /**
     * If the checks have been successful, it updates the preexisting Guest account to Member, or
     * creates a new Member account.
     */
    private void updateAccount() {
        // We are declaring an account depending on the data entered by the user:
        Account account = Utils.getInstance().getUserAccount();

        // If they do not have a guest account:
        if (account == null) {
            account = new Account(0, edtTxtParentName.getText().toString(),
                    edtTxtStudentName.getText().toString(), edtTxtEmail.getText().toString(),
                    edtTxtPassword.getText().toString(),
                    Integer.parseInt(edtTxtPin.getText().toString()));

            // Adding the account into the database:
            databaseHelper.addAccount(account);

            // Finding the account from the database and setting it so that it has the correct ID:
            account = databaseHelper.getAccountByEmail(account.getEmail());

            Utils.getInstance().setUserAccount(account);
            databaseHelper.useAccount(account.getId());

        }
        // If they have a quest account:
        else if (account.getAccountType().equals(Guest)) {
            account.setParentName(edtTxtParentName.getText().toString().toUpperCase(Locale.ROOT));
            account.setStudentName(edtTxtStudentName.getText().toString().toUpperCase(Locale.ROOT));
            account.setEmail(edtTxtEmail.getText().toString().toLowerCase(Locale.ROOT));
            account.setPassword(edtTxtPassword.getText().toString());
            account.setPin(Integer.parseInt(edtTxtPin.getText().toString()));
            account.setAccountType(Member);

            databaseHelper.addAccount(account);

            // Finding the account from the database and setting it so that it has the correct ID:
            account = databaseHelper.getAccountByEmail(edtTxtEmail.getText().toString());

            Utils.getInstance().setUserAccount(account);
            databaseHelper.useAccount(account.getId());
        }
        // Going to the main menu activity:
        startActivity(new Intent(this, MainMenuActivity.class));
    }

}
