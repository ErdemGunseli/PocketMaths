package com.example.PocketMaths;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class is required to efficiently display View objects containing data from instances of QuestionSetResult in the form of a
 * scrollable list.
 * It is used in the Account Page to display question set results in the form of pie charts, along
 * with some other data.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * AccountHistoryRecyclerAdapter extends RecyclerView.Adapter<AccountHistoryRecyclerAdapter.ViewHolder>
 * to have access to RecyclerView methods.
 * AccountHistoryRecyclerAdapter has inner class ViewHolder to store content to be displayed on each
 * RecyclerView container.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class AccountHistoryRecyclerAdapter extends RecyclerView.Adapter<AccountHistoryRecyclerAdapter.ViewHolder> {

    private Context context;

    private DatabaseHelper databaseHelper;

    private ArrayList<QuestionSetResult> questionSets;

    /**
     * Constructor
     *
     * @param context Required to get strings from resources.
     */
    public AccountHistoryRecyclerAdapter(Context context) {
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context);
        this.questionSets = databaseHelper.getQuestionSetResults();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_history_item, parent, false);
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

        QuestionSetResult questionSetHistory = questionSets.get(position);

        // Question Set Name:
        holder.txtQuestionSetName.setText(Objects.requireNonNull(Utils.getInstance().getQuestionSetById(questionSetHistory.getQuestionSetId())).getName());

        // X Points Out Of X:
        holder.txtPoints.setText(String.format(context.getString(R.string.x_points_out_of_x), questionSetHistory.getPointsEarned(), questionSetHistory.getPointsPossible()));

        // Result X%
        holder.txtResultPercentage.setText(String.format(context.getString(R.string.result_percent), questionSetHistory.getResult()));

        int[] values = {questionSetHistory.getFirstAttempt(), questionSetHistory.getSecondAttempt(), questionSetHistory.getMoreAttempts()};
        String[] labels = {context.getString(R.string.first_attempt), context.getString(R.string.second_attempt), context.getString(R.string.other)};

        // Creating the Pie Chart
        Utils.getInstance().createPieChart(context,
                holder.pieHistory,
                values,
                0,
                questionSetHistory.getDateCompleted(),
                11,
                labels,
                0,
                R.color.Primary,
                context.getString(R.string.results),
                13,
                R.color.Primary,
                R.color.Silver);
    }

    @Override
    public int getItemCount() {
        // Avoiding null pointer exception:
        if (questionSets != null) {
            return questionSets.size();
        }
        return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setQuestionSets(ArrayList<QuestionSetResult> questionSets) {
        this.questionSets = questionSets;
        notifyDataSetChanged();
    }

    /**
     * This class is required to hold View objects for every item in the RecyclerView.
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * ViewHolder extends RecyclerView.ViewHolder class to have access to its constructor.
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtQuestionSetName, txtPoints, txtResultPercentage;
        private PieChart pieHistory;

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
            // Outside an activity, we need the following syntax:
            txtQuestionSetName = itemView.findViewById(R.id.txtQuestionSetName);
            txtPoints = itemView.findViewById(R.id.txtPoints);
            txtResultPercentage = itemView.findViewById(R.id.txtResultPercentage);
            pieHistory = itemView.findViewById(R.id.pieHistory);
        }
    }
}
