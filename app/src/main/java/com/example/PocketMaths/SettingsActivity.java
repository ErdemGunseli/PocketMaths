package com.example.PocketMaths;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This activity relates to the Settings Page of the app.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * SettingsActivity extends AppCompatActivity class to have access to Activity methods.
 * SettingsActivity implements View.OnCLickListener interface to detect touch input.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * It displays all themes stored in the relational database.
 * Clicking a theme results in it being applied app-wide.
 * Has an option to enable or disable refreshers popping up automatically.
 */
public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseHelper databaseHelper = new DatabaseHelper(this);

    private ImageView imgExit;
    private RecyclerView rvThemes;
    private SwitchCompat swShowRefreshers;

    /**
     * Overrides the onCreate method of the super class.
     * Runs when SettingsActivity starts.
     * Sets the layout and theme.
     * Calls all necessary functions, either directly or through other functions.
     *
     * @param savedInstanceState Required for super constructor.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Utils.getInstance().getThemeId());
        setContentView(R.layout.activity_settings);

        initViews();

        setUpRecyclerView();

        setData();
    }

    /**
     * Initialises View objects.
     * Sets the activity's click listener to appropriate View objects.
     * Creates and applies an instance of CompoundButton.OnCheckChangeListener() to the Switch
     * instance to detect when the Switch object has been clicked, and to change the refresher
     * setting from the database and in Utils.
     */
    private void initViews() {
        rvThemes = findViewById(R.id.rvThemes);
        imgExit = findViewById(R.id.imgExit);
        swShowRefreshers = findViewById(R.id.swShowRefreshers);

        imgExit.setOnClickListener(this);
        swShowRefreshers.setOnCheckedChangeListener((CompoundButton compoundButton, boolean checked) -> {
            Utils.getInstance().setShowRefreshers(checked);
            databaseHelper.setShowRefreshers(checked);
        });
    }


    /**
     * Performs the setting-up of the RecyclerView, using ThemeRecyclerAdapter.
     */
    private void setUpRecyclerView() {
        ThemeRecyclerAdapter themeRecyclerAdapter = new ThemeRecyclerAdapter(this);
        rvThemes.setAdapter(themeRecyclerAdapter);
        rvThemes.setLayoutManager((new GridLayoutManager(this, 2)));
    }

    /**
     * Sets the state of the Switch object depending on the user preference.
     */
    private void setData() {
        swShowRefreshers.setChecked(Utils.getInstance().refreshersEnabled());
    }

    /**
     * Determines which View object has been clicked and performs the appropriate action.
     *
     * @param view Used to determine the View object clicked.
     */
    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.imgExit) {
            // We cannot simply finish if the theme has been changed,
            // restarting the main menu instead:
            startActivity(new Intent(this, MainMenuActivity.class));
            finish();
        }
    }

    /**
     * Overrides the action to be performed when the back button is pressed.
     * In this case, the MainMenuActivity should be started to prevent the reversing of the theme
     * setting.
     */
    @Override
    public void onBackPressed() {
        // Go to the main menu to prevent accidental changing of themes
        // when the back button is pressed
        startActivity(new Intent(this, MainMenuActivity.class));
        finish();
    }

}
