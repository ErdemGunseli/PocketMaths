package com.example.PocketMaths;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

/**
 * This activity relates to the Pin Verification Page of the app.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * PinVerificationActivity extends AppCompatActivity class to have access to Activity methods.
 * PinVerificationActivity implements View.OnCLickListener interface to detect touch input.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * If there is an active instance of Account, it prompts the user to enter their pin.
 * If the pin is correct, it accesses Utils to get the target class and starts it.
 * This functionality allows certain activities to only be started after the user verifies the pin.
 * If there is no active instance of Account, it prompts the user to sign-in / sign-up.
 */

public class PinVerificationActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout relPinVerification;
    private CardView cvPinVerification, cvSign;
    private Button btnContinue, btnCancel, btnSignUp, btnSignIn, btnBack;
    private EditText edtTxtPin;

    /**
     * Overrides the onCreate method of the super class.
     * Runs when PinVerificationActivity starts.
     * Sets the layout and theme.
     * Calls all necessary functions, either directly or through other functions.
     *
     * @param savedInstanceState Required for super constructor.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Utils.getInstance().getThemeId());
        setContentView(R.layout.activity_pin_verification);

        initViews();

        setData();

    }

    /**
     * Initialises View objects.
     * Sets the activity's click listener to appropriate View objects.
     */
    private void initViews() {
        relPinVerification = findViewById(R.id.relPinVerification);
        cvPinVerification = findViewById(R.id.cvPinVerification);
        btnContinue = findViewById(R.id.btnContinue);
        btnCancel = findViewById(R.id.btnCancel);
        btnBack = findViewById(R.id.btnBack);
        edtTxtPin = findViewById(R.id.edtTxtPin);
        cvSign = findViewById(R.id.cvSign);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignIn = findViewById(R.id.btnSignIn);

        btnBack.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
    }


    /**
     * Sets the visibility of certain View objects and displays appropriate prompts depending on
     * external factors.
     */
    private void setData() {
        Account account = Utils.getInstance().getUserAccount();

        if (account.getAccountType().equals(Account.Guest)) {
            // If the Account type is Guest, hiding pin verification, prompting sign-in / sign-up:
            cvSign.setVisibility(View.VISIBLE);
            cvPinVerification.setVisibility(View.GONE);
        } else {
            // Otherwise, reversing this process:
            cvSign.setVisibility(View.GONE);
            cvPinVerification.setVisibility(View.VISIBLE);
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

            case (R.id.btnContinue):
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                // Checking the pin entered by the user:
                if (edtTxtPin.getText().toString().isEmpty()){break;}
                else if (Utils.getInstance().getUserAccount().checkPin(Integer.parseInt(edtTxtPin.getText().toString()))) {
                    startActivity(new Intent(this, Utils.getInstance().getTargetClass()));
                    finish();
                } else {
                    Utils.getInstance().showSnackBar(this, relPinVerification, getString(R.string.incorrect_pin), getString(R.string.ok));
                }
                break;

            case (R.id.btnBack):
            case (R.id.btnCancel):
                finish();
                break;

            case (R.id.btnSignUp):
                startActivity(new Intent(this, SignUpActivity.class));
                break;

            case (R.id.btnSignIn):
                startActivity(new Intent(this, SignInActivity.class));
                break;

            default:
                break;

        }

    }
}
