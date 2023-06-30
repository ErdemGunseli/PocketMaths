package com.example.PocketMaths;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This activity relates to the Refresher Page of the app.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * RefresherActivity extends AppCompatActivity class to have access to Activity methods.
 * RefresherActivity implements View.OnCLickListener interface to detect touch input.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * It displays the content of a Refresher instance.
 */
public class RefresherActivity extends AppCompatActivity implements View.OnClickListener {

    public static String REFRESHER_ID = "refresherId";
    private Refresher refresher;

    private TextView txtRefresherModel;
    private ImageView imgRefresher;
    private Button btnContinue;

    /**
     * Overrides the onCreate method of the super class.
     * Runs when MainMenuActivity starts.
     * Sets the layout and theme.
     * Calls all necessary functions, either directly or through other functions.
     *
     * @param savedInstanceState Required for super constructor.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Utils.getInstance().getThemeId());
        setContentView(R.layout.activity_refresher);

        initViews();

        setDataFromIntent();
    }

    /**
     * Initialises View objects.
     * Sets the activity's click listener to appropriate View objects.
     * Sets custom EditText input type.
     */
    private void initViews() {
        txtRefresherModel = findViewById(R.id.txtRefresherTopic);
        imgRefresher = findViewById(R.id.imgRefresher);
        btnContinue = findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(this);
    }

    /**
     * Sets the appropriate instance of QuestionSet depending on extra data from the Intent instance.
     * This activity can only be started by passing the refresher set Id through the Intent.
     */
    private void setDataFromIntent() {
        Intent intent = getIntent();

        if (intent != null) {

            int refresherId = intent.getIntExtra(REFRESHER_ID, -1);

            if (refresherId != -1) {
                refresher = Utils.getInstance().getRefresherById(refresherId);

                if (refresher != null) {

                    setData(refresher);
                }
            }
        }
    }

    /**
     * Sets data from the instance of Refresher.
     *
     * @param refresher The refresher from which the data should be set.
     */
    private void setData(Refresher refresher) {
        txtRefresherModel.setText(refresher.getTopic());
        imgRefresher.setImageResource(refresher.getImageId());
    }

    /**
     * Determines which View object has been clicked and performs the appropriate action.
     *
     * @param view Used to determine the View object clicked.
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnContinue) {
            finish();
        }
    }
}
