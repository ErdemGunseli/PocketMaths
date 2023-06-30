package com.example.PocketMaths;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This class is required to efficiently display View objects containing data from instances of Task
 * in the form of a scrollable list.
 * It is used to display the tasks in the task creation page.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * ThemeRecyclerAdapter extends RecyclerView.Adapter<ThemeRecyclerAdapter.ViewHolder>
 * to have access to RecyclerView methods.
 * ThemeRecyclerAdapter has inner class ViewHolder to store content to be displayed on each
 * RecyclerView container.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * The name of the task, the pass mark, the name of the associated question set will be shown.
 * If the instance is set as deletable, clicking the delete button of a task will cause it to be
 * deleted.
 * If the instance is set as linked, clicking on the task will result in the appropriate question set
 * being started.
 */
public class ThemeRecyclerAdapter extends RecyclerView.Adapter<ThemeRecyclerAdapter.ViewHolder> {

    private Context context;
    private DatabaseHelper databaseHelper;
    private int[] themeIds;

    /**
     * Constructor
     *
     * @param context Required to get strings from resources.
     */
    public ThemeRecyclerAdapter(Context context) {
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context);
        this.themeIds = new int[]{R.drawable.theme1, R.drawable.theme2, R.drawable.theme3, R.drawable.theme4};
    }

    /**
     * Runs when an instance of ViewHolder is created.
     * Attaches the XML layout file 'account_history_item' to the ViewHolder.
     *
     * @param parent   Required to inflate XML layout file into a View object.
     * @param viewType Required for background processing.
     * @return ViewHolder object.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.theme_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Runs when an instance of ViewHolder object attaches to a container.
     * Sets the appropriate data to the ViewHolder instance layout.
     *
     * @param holder   The instance of ViewHolder object, required for accessing data.
     * @param position The index of the ArrayList, required for accessing ArrayList items.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imgTheme.setImageResource(themeIds[position]);
    }

    @Override
    public int getItemCount() {
        return themeIds.length;
    }

    /**
     * Sets and applies the theme, saving it as the current theme in the Preferences table of the
     * relational database.
     *
     * @param id The ID of the theme chosen.
     */
    public void setTheme(int id) {
        Utils.getInstance().setThemeId(id);
        databaseHelper.setTheme(id);
        context.startActivity(new Intent(context, SettingsActivity.class));
    }

    /**
     * This class is required to hold View objects for every item in the RecyclerView.
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * ViewHolder extends RecyclerView.ViewHolder to have access to its constructor.
     * ViewHolder implements View.OnCLickListener interface to detect touch input.
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgTheme;

        /**
         * Constructor
         *
         * @param itemView Required for super constructor
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            initViews();
        }

        /**
         * Initialises View objects.
         * Sets the class' click listener to the appropriate View objects.
         */
        private void initViews() {
            imgTheme = itemView.findViewById(R.id.imgTheme);
            imgTheme.setOnClickListener(this);
        }

        /**
         * Determines which View object has been clicked and performs the appropriate action.
         *
         * @param view Used to determine the View object clicked.
         */
        @Override
        public void onClick(View view) {
            switch (getAdapterPosition()) {

                case (0):
                    setTheme(R.style.Theme_PocketMaths);
                    break;

                case (1):
                    setTheme(R.style.RedGreen);
                    break;

                case (2):
                    setTheme(R.style.GreenYellow);
                    break;

                case (3):
                    setTheme(R.style.BlueGreen);
                    break;
            }
        }
    }
}

