package com.example.PocketMaths;

import static com.example.PocketMaths.Utils.SHOW_REFRESHERS;
import static com.example.PocketMaths.Utils.THEME_ID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * This class allows user data to be saved onto the SQLite Database and persist after the app is closed.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * DatabaseHelper extends SQLiteOpenHelper to gain access to SQLite Database for Java methods.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * This class creates and maintains many tables pertaining to different aspects of the app.
 * It has many methods for adding, retrieving and deleting records from each of the tables.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * Constructor
     *
     * @param context Required for super constructor.
     */
    public DatabaseHelper(@Nullable Context context) {
        super(context, "database", null, 1);
    }

    /**
     * Runs after the constructor.
     * Creates all of the necessary tables for the relational database, if they do not already exist.
     */
    @Override
    public void onCreate(SQLiteDatabase database) {

        // ### Creating Tasks table ###:
        database.execSQL("CREATE TABLE IF NOT EXISTS TASKS (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "ACCOUNT_ID INTEGER NOT NULL, " +
                "NAME TEXT NOT NULL, " +
                "PASS_MARK INTEGER NOT NULL, " +
                "REWARD TEXT NOT NULL, " +
                "QUESTION_SET_ID INTEGER NOT NULL, " +
                "IS_COMPLETED BOOL NOT NULL" +
                ")");

        // ### Creating Question Set Results Table ###:
        database.execSQL("CREATE TABLE IF NOT EXISTS QUESTION_SET_RESULTS (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "QUESTION_SET_ID INTEGER NOT NULL, " +
                "ACCOUNT_ID INTEGER NOT NULL, " +
                "QUESTION_SET_POINTS_EARNED INTEGER NOT NULL, " +
                "QUESTION_SET_POINTS_POSSIBLE INTEGER NOT NULL, " +
                "FIRST_ATTEMPT INTEGER, " +
                "SECOND_ATTEMPT INTEGER, " +
                "MORE_ATTEMPTS INTEGER, " +
                "DATE_COMPLETED INTEGER" +
                ")");

        // ### Creating Accounts Table ###:
        // (In a client-server version of the app, this table would be hosted on the server)
        database.execSQL("CREATE TABLE IF NOT EXISTS ACCOUNTS (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "PARENT_NAME TEXT NOT NULL," +
                "STUDENT_NAME TEXT NOT NULL," +
                "EMAIL TEXT NOT NULL," +
                "PASSWORD TEXT NOT NULL," +
                "PIN INTEGER NOT NULL" +
                ")");

        // ### Current Account Table ###:
        // (In a client-server version of the app, this table would have the same columns as the
        //  Accounts table)
        database.execSQL("CREATE TABLE IF NOT EXISTS CURRENT_ACCOUNT (" +
                "ACCOUNT_ID INTEGER PRIMARY KEY NOT NULL" +
                ")");

        // ### Creating App Preferences Table ###:
        database.execSQL("CREATE TABLE IF NOT EXISTS APP_PREFERENCES (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "DESCRIPTION TEXT NOT NULL," +
                "VALUE INTEGER NOT NULL" +
                ")");

    }

    /**
     * Runs on pre-existing versions of the app if the database version is updated after the user
     * installing it.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public long getTableLength(String tableName) {
        SQLiteDatabase database = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(database, tableName);
        database.close();
        return count;
    }

    ////#### TASKS TABLE ####////:
    public boolean addTask(Task task) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("ACCOUNT_ID", task.getAccountId());
        contentValues.put("NAME", task.getName());
        contentValues.put("REWARD", task.getReward());
        contentValues.put("PASS_MARK", task.getPassMark());
        contentValues.put("QUESTION_SET_ID", task.getQuestionSetId());
        contentValues.put("IS_COMPLETED", task.isCompleted());

        long insert = database.insert("TASKS", null, contentValues);

        // Cleaning Up:
        database.close();

        // if insert is negative, it has failed, if it is positive, it was successful:
        return insert > 0;
    }

    public ArrayList<Task> getTasks(int accountId) {
        ArrayList<Task> tasks = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM TASKS WHERE ACCOUNT_ID=?", new String[]{String.valueOf(accountId)});

        if (cursor.moveToFirst()) {
            do {
                tasks.add(new Task(cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getInt(5),
                        cursor.getInt(6) == 1));
            } while (cursor.moveToNext());
        }

        // Cleaning Up:
        cursor.close();
        database.close();

        return tasks;
    }

    public boolean deleteTask(Task task) {
        SQLiteDatabase database = this.getWritableDatabase();

        long result = database.delete("TASKS", "ID =?", new String[]{String.valueOf(task.getId())});

        // Cleaning Up:
        database.close();

        // if insert is negative, it has failed, if it is positive, it was successful:
        return result > 0;
    }

    public void completeTask(Task task) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("UPDATE TASKS SET IS_COMPLETED = 1 WHERE ID = ?", new String[]{String.valueOf(task.getId())});

        // Cleaning Up:
        database.close();
    }

    ////#### QUESTION SET RESULT TABLE ####/////:
    public boolean addQuestionSetResult(QuestionSetResult questionSetResult, int accountId) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("QUESTION_SET_ID", questionSetResult.getQuestionSetId());
        contentValues.put("ACCOUNT_ID", accountId);
        contentValues.put("QUESTION_SET_POINTS_EARNED", questionSetResult.getPointsEarned());
        contentValues.put("QUESTION_SET_POINTS_POSSIBLE", questionSetResult.getPointsPossible());
        contentValues.put("FIRST_ATTEMPT", questionSetResult.getFirstAttempt());
        contentValues.put("SECOND_ATTEMPT", questionSetResult.getSecondAttempt());
        contentValues.put("MORE_ATTEMPTS", questionSetResult.getMoreAttempts());
        contentValues.put("DATE_COMPLETED", questionSetResult.getDateCompleted());

        long insert = database.insert("QUESTION_SET_RESULTS", null, contentValues);

        // Cleaning Up:
        database.close();

        // if insert is negative, it has failed, if it is positive, it was successful:
        return insert > 0;
    }

    public ArrayList<QuestionSetResult> getQuestionSetResults() {
        ArrayList<QuestionSetResult> questionSetResults = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM QUESTION_SET_RESULTS WHERE ACCOUNT_ID=?", new String[]{String.valueOf(Utils.getInstance().getUserAccount().getId())});

        if (cursor.moveToFirst()) {
            do {
                questionSetResults.add(new QuestionSetResult(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getInt(5),
                        cursor.getInt(6),
                        cursor.getInt(7),
                        cursor.getString(8)
                ));
            } while (cursor.moveToNext());
        }

        // Cleaning Up:
        cursor.close();
        database.close();

        return questionSetResults;
    }

    ////#### ACCOUNT TABLE ####////:
    public boolean addAccount(Account account) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("PARENT_NAME", account.getParentName());
        contentValues.put("STUDENT_NAME", account.getStudentName());
        contentValues.put("EMAIL", account.getEmail());
        contentValues.put("PASSWORD", account.getPassword());
        contentValues.put("PIN", account.getPin());

        long insert = database.insert("ACCOUNTS", null, contentValues);

        // Cleaning Up:
        database.close();

        // if insert is negative, it has failed, if it is positive, it was successful:
        return insert > 0;
    }

    public Account getAccountById(int id) {

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM ACCOUNTS WHERE ID = ?", new String[]{String.valueOf(id)});

        Account account = new Account();
        if (cursor.moveToFirst()) {
            account = new Account(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5)
            );
        }

        // Cleaning Up:
        cursor.close();
        database.close();

        if (account.getAccountType().equals(Account.Guest)) {
            return null;
        }
        return account;
    }

    public Account getAccountByEmail(String email) {

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM ACCOUNTS WHERE EMAIL = ?", new String[]{email});

        Account account = new Account();
        if (cursor.moveToFirst()) {
            account = new Account(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5)
            );
        }

        // Cleaning Up:
        cursor.close();
        database.close();

        if (account.getAccountType().equals(Account.Guest)) {
            return null;
        }
        return account;
    }

    public void updateAccount(Account account) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("UPDATE ACCOUNTS SET PARENT_NAME = ?, STUDENT_NAME =?, EMAIL = ?, PASSWORD = ?, PIN = ? WHERE ID = ?",
                new String[]{String.valueOf(account.getParentName()), String.valueOf(account.getStudentName()), String.valueOf(account.getEmail()), String.valueOf(account.getPassword()), String.valueOf(account.getPin()), String.valueOf(account.getId())});

        // Cleaning Up:
        database.close();
    }

    ////#### CURRENT ACCOUNT TABLE ####////:
    public boolean useAccount(int id) {
        Account account = getAccountById(id);
        if (account == null) {
            return false;
        } else {
            SQLiteDatabase database = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("ACCOUNT_ID", account.getId());

            long insert = database.insert("CURRENT_ACCOUNT", null, contentValues);

            // if insert is negative, it has failed, if it is positive, it was successful:
            if (insert > 0) {
                // Cleaning Up:
                database.close();
                return false;
            } else {
                // If the new account has successfully been set, delete any pre-existing current account:
                database.execSQL("DELETE FROM CURRENT_ACCOUNT WHERE NOT ACCOUNT_ID =?", new String[]{String.valueOf(id)});

                // Cleaning Up:
                database.close();
                return true;
            }
        }

    }

    public boolean removeCurrentAccount() {
        SQLiteDatabase database = this.getWritableDatabase();

        database.execSQL("DELETE FROM CURRENT_ACCOUNT");


        // Cleaning Up:
        database.close();

        return (getTableLength("CURRENT_ACCOUNT") == 0);
    }

    public Account getCurrentAccount() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM CURRENT_ACCOUNT", null);

        Account account = new Account();
        if (cursor.moveToFirst()) {
            account = getAccountById(cursor.getInt(0));
        }

        // Cleaning Up:
        cursor.close();
        database.close();

        if (account.getAccountType().equals(Account.Guest)) {
            return null;
        }
        return account;

    }

    ////#### PREFERENCES TABLE ####////:
    public boolean setTheme(int themeId) {
        SQLiteDatabase database = this.getWritableDatabase();

        // Checking if the exact setting-value pair already exists in the table:
        Cursor cursor = database.rawQuery("SELECT * FROM APP_PREFERENCES WHERE DESCRIPTION =? AND VALUE =?", new String[]{THEME_ID, String.valueOf(themeId)});
        // If the exact setting is already set, return
        if (cursor.moveToFirst()) {
            return true;
        }


        ContentValues contentValues = new ContentValues();

        contentValues.put("DESCRIPTION", THEME_ID);
        contentValues.put("VALUE", themeId);

        long insert = database.insert("APP_PREFERENCES", null, contentValues);

        // if insert is negative, it has failed, if it is positive, it was successful:
        if (insert < 0) {
            // Cleaning Up:
            database.close();
            cursor.close();
            return false;
        } else {

            // If the new current theme has successfully been set, delete any pre-existing records of the current theme:
            database.execSQL("DELETE FROM APP_PREFERENCES WHERE DESCRIPTION =? AND NOT VALUE =?", new String[]{THEME_ID, String.valueOf(themeId)});

            // Cleaning Up:
            database.close();
            cursor.close();
            return true;
        }
    }

    public int getTheme() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM APP_PREFERENCES WHERE DESCRIPTION =?", new String[]{THEME_ID});

        int themeId = 0;
        if (cursor.moveToFirst()) {
            themeId = cursor.getInt(2);
        }

        // Cleaning Up:
        cursor.close();
        database.close();

        return themeId;
    }

    public boolean setShowRefreshers(boolean bool) {
        SQLiteDatabase database = this.getWritableDatabase();

        // Checking if the exact setting-value pair already exists in the table:
        Cursor cursor = database.rawQuery("SELECT * FROM APP_PREFERENCES WHERE DESCRIPTION =? AND VALUE =?", new String[]{SHOW_REFRESHERS, String.valueOf(bool ? 1 : 0)});
        // If the exact setting is already set, return
        if (cursor.moveToFirst()) {
            return true;
        }


        ContentValues contentValues = new ContentValues();

        contentValues.put("DESCRIPTION", SHOW_REFRESHERS);
        contentValues.put("VALUE", (bool ? 1 : 0));

        long insert = database.insert("APP_PREFERENCES", null, contentValues);

        // if insert is negative, it has failed, if it is positive, it was successful:
        if (insert < 0) {
            // Cleaning Up:
            database.close();
            cursor.close();
            return false;
        } else {

            // If the new current theme has successfully been set, delete any pre-existing records of the current theme:
            database.execSQL("DELETE FROM APP_PREFERENCES WHERE DESCRIPTION =? AND NOT VALUE =?", new String[]{SHOW_REFRESHERS, String.valueOf(bool ? 1 : 0)});

            // Cleaning Up:
            database.close();
            cursor.close();
            return true;
        }

    }

    public boolean getShowRefreshers() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM APP_PREFERENCES WHERE DESCRIPTION =?", new String[]{SHOW_REFRESHERS});

        // If setting not present, it should default to True.
        boolean bool = true;
        if (cursor.moveToFirst()) {
            bool = cursor.getInt(2) == 1;
        }

        // Cleaning Up:
        cursor.close();
        database.close();

        return bool;

    }
}
