package com.example.PocketMaths;

import java.util.Locale;

/**
 * The instances of this class contain the details associated with a task.
 * The instances of other classes are associated with instances of this class, such as QuestionSetResult
 */
public class Task {

    private int id;
    private String name;
    private int accountId;
    private int passMark;
    private String reward;
    private int questionSetId;
    private boolean isCompleted;


    /**
     * Constructor for Task
     *
     * @param id            Task ID.
     * @param accountId     ID of account that made the task.
     * @param name          Name of the task.
     * @param passMark      The percentage which needs to be exceeded for the task to be completed.
     * @param reward        The reward for the task (optional).
     * @param questionSetId The ID of the question set associated with the task.
     * @param isCompleted   Whether or not the task has been completed.
     */
    public Task(int id, int accountId, String name, int passMark, String reward, int questionSetId, boolean isCompleted) {
        this.id = id;
        this.name = name.toUpperCase(Locale.ROOT);
        this.accountId = accountId;
        this.passMark = passMark;
        this.reward = reward.toUpperCase(Locale.ROOT);
        this.questionSetId = questionSetId;
        this.isCompleted = isCompleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public int getQuestionSetId() {
        return questionSetId;
    }

    public void setQuestionSetId(int questionSetId) {
        this.questionSetId = questionSetId;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }

    public int getPassMark() {
        return passMark;
    }

    public void setPassMark(int passMark) {
        this.passMark = passMark;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
