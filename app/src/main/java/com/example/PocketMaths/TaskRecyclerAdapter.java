package com.example.PocketMaths;

import static com.example.PocketMaths.QuestionSetActivity.QUESTION_SET_ID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * This class is required to efficiently display View objects containing data from instances of Task
 * in the form of a scrollable list.
 * It is used to display the tasks in the task creation page.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * TaskRecyclerAdapter extends RecyclerView.Adapter<ThemeRecyclerAdapter.ViewHolder>
 * to have access to RecyclerView methods.
 * TaskRecyclerAdapter has inner class ViewHolder to store content to be displayed on each
 * RecyclerView container.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * The name of the task, the pass mark, the name of the associated question set will be shown.
 * If the instance is set as deletable, clicking the delete button of a task will cause it to be
 * deleted.
 * If the instance is set as linked, clicking on the task will result in the appropriate question set
 * being started.
 */
public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.ViewHolder> {

    private ArrayList<Task> tasks = new ArrayList<>();
    private Context context;
    private DatabaseHelper databaseHelper;
    private boolean deletable;
    private boolean linked;

    /**
     * Constructor
     *
     * @param context Required to get strings from resources.
     */
    public TaskRecyclerAdapter(Context context, DatabaseHelper databaseHelper, boolean deletable, boolean linked) {
        this.context = context;
        this.databaseHelper = databaseHelper;
        this.deletable = deletable;
        this.linked = linked;
    }

    /**
     * Runs when an instance of ViewHolder is created.
     * Attaches the XML layout file 'account_history_item' to the ViewHolder.
     *
     * @param parent   Required to inflate XML layout file into a View object.
     * @param viewType Required for background processing.
     * @return ViewHolder object.
     */
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
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
        Task task = tasks.get(position);

        holder.txtTaskName.setText(task.getName());
        holder.txtQuestionSetName.setText(String.format(context.getString(R.string.view_task_question_set_name),
                Utils.getInstance().getQuestionSetById(task.getQuestionSetId()).getName()));
        holder.txtPassMark.setText(String.format(context.getString(R.string.view_task_pass_mark), task.getPassMark()));

        // Only showing the reward if there is one:
        if (task.getReward().isEmpty()) {
            holder.txtReward.setVisibility(View.GONE);
        } else {
            holder.txtReward.setText(String.format(context.getString(R.string.view_task_reward), task.getReward()));
        }

        // Setting the visibility of the delete button:
        if (this.deletable) {
            holder.imgDelete.setVisibility(View.VISIBLE);
        } else {
            holder.imgDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    /**
     * If the TaskRecyclerAdapter instance has been set to allow tasks to be deletable, deletes the
     * selected task.
     *
     * @param position The index of the Task instance in the ArrayList.
     */
    private void deleteTask(int position) {
        if (!this.deletable) {
            return;
        }

        // Deleting specified task from the database:
        if (databaseHelper.deleteTask(tasks.get(position))) {
            Toast.makeText(context, context.getString(R.string.task_deleted), Toast.LENGTH_SHORT).show();
        }
        // Deleting specified item from array list:
        tasks.remove(position);

        // Re-setting tasks:
        setTasks(tasks);

    }

    /**
     * If the TaskRecyclerAdapter instance has been set to allow the linked question set to be started
     * by clicking the task, starts the questions set.
     *
     * @param questionSetId The ID of the question set to be started.
     */
    private void startQuestionSet(int questionSetId) {
        if (!this.linked) {
            return;
        }
        context.startActivity(new Intent(context, QuestionSetActivity.class)
                .putExtra(QUESTION_SET_ID, questionSetId));
    }

    /**
     * This class is required to hold View objects for every item in the RecyclerView.
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * ViewHolder extends RecyclerView.ViewHolder to have access to its constructor.
     * ViewHolder implements View.OnCLickListener interface to detect touch input.
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView cvDetails;
        private TextView txtTaskName, txtQuestionSetName, txtPassMark, txtReward;
        private ImageView imgDelete;

        /**
         * Constructor
         *
         * @param itemView Required for super constructor
         */
        public ViewHolder(View itemView) {
            super(itemView);
            initViews();
        }

        /**
         * Initialises View objects.
         * Sets the class' click listener to the appropriate View objects.
         */
        private void initViews() {
            cvDetails = itemView.findViewById(R.id.cvDetails);
            txtTaskName = itemView.findViewById(R.id.txtTaskName);
            txtQuestionSetName = itemView.findViewById(R.id.txtQuestionSetName);
            txtPassMark = itemView.findViewById(R.id.txtPassMark);
            txtReward = itemView.findViewById(R.id.txtReward);
            imgDelete = itemView.findViewById(R.id.imgDelete);

            imgDelete.setOnClickListener(this);
            cvDetails.setOnClickListener(this);
        }

        /**
         * Determines which View object has been clicked and performs the appropriate action.
         *
         * @param view Used to determine the View object clicked.
         */
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case (R.id.imgDelete):
                    if (!deletable){return;}
                    deleteTask(getAdapterPosition());
                    break;

                case (R.id.cvDetails):
                    startQuestionSet(tasks.get(getAdapterPosition()).getQuestionSetId());
            }
        }
    }


}
