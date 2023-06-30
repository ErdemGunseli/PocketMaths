package com.example.PocketMaths;


import java.util.ArrayList;

/**
 * The instances of this class contain the contents of a question set.
 * The instances of this class are associated to other classes, such as to Question.
 */

public class QuestionSet {

    private int id;
    private String name, description;
    private int imageId;
    private Question[] questions;
    private int currentQuestionIndex;

    private ArrayList<String> topics = new ArrayList<>();
    private ArrayList<Refresher> refreshers = new ArrayList<>();

    /**
     * Constructor for Question Set
     *
     * @param id          Id of the question set used for locating it.
     * @param name        Name of the question set.
     * @param description Description of the question set.
     * @param imageId     The Id of the image.
     * @param questions   The ArrayList of questions in the question set.
     */
    public QuestionSet(int id, String name, String description, int imageId, Question[] questions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageId = imageId;
        this.questions = questions;
        this.currentQuestionIndex = 0;
        arrangeTopics();
        calculateRefreshers();
    }

    /**
     * Iterates through each question of the question set, making a list of the topics, removing any
     * duplicate topics.
     */
    private void arrangeTopics() {
        for (Question question : this.questions) {
            String topic = question.getTopic();

            if (!this.topics.contains(topic)) {
                this.topics.add(topic);
            }
        }
    }

    /**
     * Gets refreshers from Utils which are for the topics in the question set.
     */
    private void calculateRefreshers() {
        for (Refresher refresher : Utils.getInstance().getRefreshers()) {
            if (this.topics.contains(refresher.getTopic())) {
                this.refreshers.add(refresher);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Question[] getQuestions() {
        return questions;
    }

    public void setQuestions(Question[] questions) {
        this.questions = questions;
        arrangeTopics();
        currentQuestionIndex = 0;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        // To prevent any errors, validate first:
        if (currentQuestionIndex >= 0 && currentQuestionIndex < getQuestions().length) {
            this.currentQuestionIndex = currentQuestionIndex;
        }
    }

    public int length() {
        return this.questions.length;
    }

    /**
     * Calculates the overall result of the question set.
     *
     * @return The result to 0dp.
     */
    public int calculateResult() {
        return (int) (((float) calculatePointsEarned() / (float) calculatePointsPossible()) * 100);
    }

    /**
     * Iterates through each question, adding up the points possible.
     *
     * @return Total points possible.
     */
    public int calculatePointsPossible() {
        float totalPossible = 0;
        for (Question question : this.questions) {
            totalPossible += question.getPointsPossible();
        }
        return (int) totalPossible;
    }


    /**
     * Iterates through each question, adding up the points earned.
     *
     * @return Total points earned.
     */
    public int calculatePointsEarned() {
        float totalEarned = 0;
        for (Question question : this.questions) {
            totalEarned += question.getPointsEarned();
        }
        return (int) totalEarned;
    }

    /**
     * Calculates a list of how many times each question within the question set has been attempted.
     *
     * @return List {First Attempt, Second Attempt, More Attempts}
     */
    public int[] calculateNumberOfQuestionsSolved() {
        int firstAttempt = 0;
        int secondAttempt = 0;
        for (Question question : this.questions) {

            // If they have earned any points from a question, consider it solved:
            if (question.getPointsEarned() == question.getPointsPossible()) {
                firstAttempt += 1;
            } else if (question.getPointsEarned() == question.getPointsPossible() / 2) {
                secondAttempt += 1;
            }
        }
        int moreAttempts = this.questions.length - firstAttempt - secondAttempt;

        return new int[]{firstAttempt, secondAttempt, moreAttempts};
    }

    /**
     * After the question set has been completed and the attempt has been saved as an instance of
     * QuestionSetResult, resets the instance of QuestionSet to its initial value so that it can be
     * done again in the same session.
     */
    public void reset() {
        for (Question question : this.questions) {
            question.setPointsEarned(0);
            question.setAttempts(0);
            question.setAnswerShown(false);
            question.setUserAnswerIndexes(new ArrayList<>());
            question.setUserWrittenAnswers(new ArrayList<>());
        }
    }


    /**
     * Calculates the series of topics, in the questions of which, the user did not earn all of the
     * points possible.
     *
     * @return Topics mistakes were made in.
     */
    public ArrayList<String> getFailedTopics() {
        ArrayList<String> failedTopics = new ArrayList<>();

        for (Question question : this.questions) {
            String topic = question.getTopic();

            // If answered incorrectly, add the topic/model to arraylist.
            if (question.getPointsPossible() != question.getPointsEarned()) {
                if (!failedTopics.contains(topic)) {
                    failedTopics.add(topic);
                }
            }
        }
        return failedTopics;
    }

    public ArrayList<String> getTopics() {
        return topics;
    }

    public void setTopics(ArrayList<String> topics) {
        this.topics = topics;
    }

    public ArrayList<Refresher> getRefreshers() {
        return refreshers;
    }

    public void setRefreshers(ArrayList<Refresher> refreshers) {
        this.refreshers = refreshers;
    }
}
