package com.example.PocketMaths;


/**
 * The instances of this class contain the result of a question set.
 * It has some of the same attributes as QuestionSet.
 * This is because complex objects are pointer-based in Java, so we cannot get these attributes on
 * demand using the QuestionSet methods, since once a question set is reset, all identical copies are
 * as well.
 * The instances of other classes are associated with instances of this class, such as QuestionSet, Account
 */
public class QuestionSetResult {

    private int id;
    private int questionSetId;
    private int accountId;

    private int pointsPossible;
    private int pointsEarned;
    private int result;

    private int firstAttempt;
    private int secondAttempt;
    private int moreAttempts;

    private String dateCompleted;

    /**
     * Constructor for Question Set Result
     *
     * @param id             The ID of the question set result.
     * @param questionSetId  The ID of the question set the result is for.
     * @param accountId      The ID of the account that completed the question set.
     * @param pointsEarned   The number of points earned from the question set.
     * @param pointsPossible The number of points possible from the question set.
     * @param firstAttempt   The number of questions solved with 1 attempt.
     * @param secondAttempt  The number of questions solved with 2 attempts.
     * @param moreAttempts   The number of questions solved with 3 attempts.
     * @param dateCompleted  The date when the question set has been completed.
     */
    public QuestionSetResult(int id, int questionSetId, int accountId, int pointsEarned, int pointsPossible, int firstAttempt, int secondAttempt, int moreAttempts, String dateCompleted) {
        this.id = id;
        this.accountId = accountId;
        this.questionSetId = questionSetId;
        this.pointsEarned = pointsEarned;
        this.pointsPossible = pointsPossible;
        this.result = (int) (((float) pointsEarned / (float) pointsPossible) * 100);
        this.firstAttempt = firstAttempt;
        this.secondAttempt = secondAttempt;
        this.moreAttempts = moreAttempts;
        this.dateCompleted = dateCompleted;
    }

    public int getId() {
        return id;
    }

    public int getQuestionSetId() {
        return questionSetId;
    }

    public int getPointsPossible() {
        return pointsPossible;
    }

    public int getPointsEarned() {
        return pointsEarned;
    }

    public int getResult() {
        return result;
    }

    public int getFirstAttempt() {
        return firstAttempt;
    }

    public int getSecondAttempt() {
        return secondAttempt;
    }

    public int getMoreAttempts() {
        return moreAttempts;
    }

    public String getDateCompleted() {
        return dateCompleted;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
