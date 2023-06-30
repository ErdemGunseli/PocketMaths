package com.example.PocketMaths;

import java.util.Locale;

/**
 * The instances of this class contain the account details of a user account.
 * The instances of other classes are associated with instances of this class, such as QuestionSetResult, Task, Utils
 */

public class Account {

    public static final String Member = "Member";
    public static final String Guest = "Guest";
    private int id, pin;
    private String parentName, studentName, email, password;
    private String accountType;

    /**
     * Constructor for Member Account
     *
     * @param id          Required for processing the user account.
     * @param parentName  Parent Name
     * @param studentName Student Name
     * @param email       Email
     * @param password    Password
     * @param pin         Pin
     */
    public Account(int id, String parentName, String studentName, String email, String password, int pin) {
        this.id = id;
        this.parentName = parentName.toUpperCase(Locale.ROOT);
        this.studentName = studentName.toUpperCase(Locale.ROOT);
        this.email = email.toLowerCase(Locale.ROOT);
        this.password = password;
        this.pin = pin;
        this.accountType = Member;
    }

    /**
     * Constructor for Guest Account
     */
    public Account() {
        this.accountType = Guest;
        this.id = -1;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public boolean checkPin(int pin) {
        return this.pin == pin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
