package com.example.PocketMaths;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class provides many app-wide functionalities.
 * It gives access to many methods and attributes that are required throughout the app.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Utils follows the Singleton pattern so that only one instance of it can be made.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class Utils {

    public static final String THEME_ID = "themeId";
    public static final String SHOW_REFRESHERS = "showRefreshers";
    private static Utils instance;
    private static ArrayList<QuestionSet> questionSets;
    private static ArrayList<Refresher> refreshers;
    private Account userAccount;
    private Class<?> targetClass = MainMenuActivity.class;
    private int themeId = R.style.Theme_PocketMaths;
    private boolean showRefreshers = true;


    /**
     * Private Constructor
     * Follows the Singleton pattern such that new Utils objects can only be constructed from within
     * the class.
     * Causes data to be initialised.
     */
    private Utils() {
        // If the Question Sets have not been initialised yet, initialise them.
        if (questionSets == null) {
            initData();
        }
    }

    /**
     * Retrieves instance of Utils.
     * If there are none, creates an instance.
     * Synchronised to be Thread-Safe (if multi-threaded processing was to be added).
     *
     * @return The instance created now or previously.
     */
    public static synchronized Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }
        return instance;
    }

    public static ArrayList<QuestionSet> getQuestionSets() {
        return questionSets;
    }

    /**
     * Initialises Question Sets
     */
    private void initData() {
        questionSets = new ArrayList<>();
        refreshers = new ArrayList<>();

        Question[] fractionBasics = new Question[]{

                new Question("Expressing Fractions",
                        "David has a pizza consisting of 8 slices. He eats 1 of the slices. What fraction of the pizza has he eaten?", 0,
                        new String[]{"<sup>8</sup>/<sub>1</sub>", "<sup>1</sup>/<sub>2</sub>", "<sup>1</sup>/<sub>8</sub>", "<sup>1</sup>/<sub>4</sub>"}, 2, 10),

                new Question("Expressing Fractions",
                        "What is the <b>denominator</b> of the fraction represented by the diagram?", R.drawable.fraction_3_4,
                        4, 20),

                new Question("Expressing Fractions",
                        "What is the <b>numerator</b> of the fraction represented by the diagram?", R.drawable.fraction_3_4,
                        3, 20),

                new Question("Expressing Fractions",
                        "Jenna, Emma and Alex split 23 marbles between themselves. Emma gets 8 and Alex gets 6. What fraction of the marbles does Jenna receive?", 0,
                        new String[]{"<sup>14</sup>/<sub>23</sub>", "<sup>6</sup>/<sub>23</sub>", "<sup>8</sup>/<sub>23</sub>", "<sup>9</sup>/<sub>23</sub>"}, 3, 50),


                new Question("Expressing Fractions",
                        "Edward's mother's age is 3 times that of Edward's. Edward's mother is 36 years old. Which of the following shows Edward's age as a fraction of his mother's?", 0,
                        new String[]{"<sup>1</sup>/<sub>3</sub>", "<sup>1</sup>/<sub>36</sub>", "<sup>1</sup>/<sub>12</sub>", "<sup>1</sup>/<sub>6</sub>"}, 0, 50),


                new Question("Expressing Fractions",
                        "James is given £15 to spend in the museum. He decides that he will spend <sup>1</sup>/<sub>3</sub> of this money. He would like to buy a gift each for 5 of his friends. He will spend the same amount of money on each gift. Express the money he spends on <b>2</b> of the gifts as a fraction of his initial money.",
                        0, new String[]{"<sup>5</sup>/<sub>15</sub>", "<sup>2</sup>/<sub>15</sub>", "<sup>2</sup>/<sub>5</sub>", "<sup>1</sup>/<sub>3</sub>"}, 1, 70),


                new Question("Simplifying Fractions",
                        "Ronald buys 9 watermelons, 6 of which he later sells. Which of the following shows the number of watermelons <b>not sold</b> as a fraction of all the watermelons in its <b>simplest form<b>?", 0,
                        new String[]{"<sup>6</sup>/<sub>9</sub>", "<sup>1</sup>/<sub>3</sub>", "<sup>2</sup>/<sub>3</sub>", "<sup>3</sup>/<sub>9</sub>"}, 1, 50),

                new Question("Simplifying Fractions",
                        "The following is an ingredients list for a cake. Flour comes in packets of 1kg. Which of the following is the amount of flour used for the cake expressed as a fraction of the amount of flour in a packet in its <b>simplest form</b>?", R.drawable.cake_ingredients_list,
                        new String[]{"<sup>200</sup>/<sub>1000</sub>", "<sup>200</sup>/<sub>1</sub>", "<sup>2</sup>/<sub>10</sub>", "<sup>1</sup>/<sub>5</sub>"}, 3, 100),

                new Question("Simplifying Fractions",
                        "The following is an ingredients list for a cake. Kate would like to follow this recipe. She uses 1 cup of vegetable oil. How many cups of milk must she use?", R.drawable.cake_ingredients_list,
                        2, 150),

                new Question("Improper Fractions",
                        "Robert spends £3.30 of his £5 in the grocery store. What fraction <b>of a pound</b> in its <b>simplest form</b> does he have left?", 0,
                        new String[]{"<sup>17</sup>/<sub>10</sub>", "<sup>17</sup>/<sub>50</sup>", "<sub>33</sup>/<sub>50</sub>", "<sup>33</sup>/<sub>10</sub>"}, 0, 250),

                new Question("Improper Fractions",
                        "Michael finishes a 500m race in 2 minutes and 5 seconds. Jacob finishes the same race in 1 minute and 40 seconds. Express Jacob's speed as a fraction of Michael's speed in its <b>simplest form.</b>", 0,
                        new String[]{"<sup>4</sup>/<sub>5</sub>", "<sup>100</sup>/<sub>125</sub>", "<sup>125</sup>/<sub>100</sub>", "<sup>5</sup>/<sub>4</sub>"}, 3, 350),


                new Question("Mixed Numbers",
                        "<br>Which of the following shows the Improper Fraction, <sup>11</sup>/<sub>2</sub>, as a <b>Mixed Number</b>?<br>", 0,
                        new String[]{"5.5", "5 &times; <sup>1</sup>/<sub>2</sub>", "5 <sup>1</sup>/<sub>2</sub>", "<sup>1</sup>/<sub>2</sub>"}, 2, 50),

                new Question("Mixed Numbers",
                        "<br>Which of the following shows the Improper Fraction, <sup>16</sup>/<sub>3</sub>, as a <b>Mixed Number</b>?<br>", 0,
                        new String[]{"16.3", "5 <sup>1</sup>/<sub>3</sub>", "<sup>1</sup>/<sub>3</sub>", "5 &times; <sup>1</sup>/<sub>3</sub>"}, 1, 50),


                new Question("Comparing Fractions",
                        "Which of the following is <b>not equal</b> in value to the rest?", 0,
                        new String[]{"<sup>17</sup>/<sub>3</sub>", "17 + <sup>1</sup>/<sub>3</sup>", "5 + <sup>2</sup>/<sub>3</sub>", "5 <sup>2</sup>/<sub>3</sub> "}, 1, 50),


                new Question("Comparing Fractions",
                        "Which of the following is the <b>greatest<b> in value?", 0,
                        new String[]{"<sup>1</sup>/<sub>2</sub>", "<sup>5</sup>/<sub>3</sub>", "1", "<sup>7</sup>/<sub>2</sub>"}, 3, 100),

                new Question("Comparing Fractions",
                        "Liam claims that the fractions <sup>4</sup>/<sub>5</sub> and <sup>5</sup>/<sub>6</sub> are <b>equal</b>, since 1 is added to both the numerator and the denominator. Lucy claims that <sup>4</sup>/<sub>5</sub> is <b>greater</b>.", 0,
                        new String[]{"Liam is correct.", "Lucy is correct.", "Neither is correct."}, 2, 100),


                new Question("Comparing Fractions",
                        "Which of the following is the <b>greatest<b> in value?", 0,
                        new String[]{"<sup>33</sup>/<sub>2</sub>", "16", "9 <sup>2</sup>/<sub>3</sub>", "<sup>81</sup>/<sub>5</sub>"}, 0, 100),


                new Question("Comparing Fractions",
                        "Twins Cameron and Luke each get an identically sized cake for their birthday. Cameron slices his cake into 5 equal pieces and eats 2. Luke slices his cake into 7 equal pieces and eats 3. Which one of them has had more cake? By how much?",
                        0, new String[]{"Luke by <sup>1</sup>/<sub>35</sub>", "Cameron by <sup>1</sup>/<sub>35</sub>", "Luke by <sup>1</sup>/<sub>7</sub>", "Cameron by <sup>1</sup>/<sub>5</sub>"}, 0, 150),


                new Question("Comparing Fractions",
                        "A cinema offers 3 sizes of popcorn. The small size is 100g and costs 50p. The medium size is 250g and costs £1.10. The large size is 350g and costs £2. Which size has the best value?", 0,
                        new String[]{"Small Size", "Medium Size", "Large Size", "All sizes have the same value."}, 1, 200)

        };

        Question[] workingWithFractions = new Question[]{
                new Question("Addition & Subtraction with Fractions",
                        "<br>Calculate <sup>1</sup>/<sub>2</sub> + <sup>1</sup>/<sub>2</sub><br>", 0,
                        1, 20),

                new Question("Addition & Subtraction with Fractions",
                        "<br>Which of the following is <b>not equal</b> to <sup>1</sup>/<sub>3</sub>?<br>", 0,
                        new String[]{"<sup>2</sup>/<sub>3</sub> - <sup>1</sup>/<sub>3</sub>", "<sup>4</sup>/<sub>9</sub> - <sup>2</sup>/<sub>9</sub>", "<sup>1</sup>/<sub>6</sub> + <sup>1</sup>/<sub>6</sub>", "2 - <sup>15</sup>/<sub>9</sub>"},
                        1, 100),

                new Question("Addition & Subtraction with Fractions",
                        "<br>Calculate 3 - <sup>4</sup>/<sub>2</sub><br>", 0,
                        1, 50),

                new Question("Addition & Subtraction with Fractions",
                        "<br>Which of the following is <b>equal</b> to <sup>1</sup>/<sub>2</sub> + <sup>2</sup>/<sub>5</sub> - <sup>1</sup>/<sub>3</sub>?<br>", 0,
                        new String[]{"<sup>1</sup>/<sub>15</sub>", "<sup>2</sup>/<sub>6</sub>", "<sup>37</sup>/<sub>30</sub>", "<sup>17</sup>/<sub>30</sub>"},
                        3, 150),

                new Question("Multiplication & Division with Fractions",
                        "<br>Calculate <sup>1</sup>/<sub>3</sub> &times; <sup>12</sup>/<sub>2</sub>", 0,
                        2, 100),

                new Question("Multiplication & Division with Fractions",
                        "<br>Which of the following is <b>equal</b> to <sup>1</sup>/<sub>3</sub> &#247; <sup>12</sup>/<sub>2</sub> in its <b>simplest form<b>?<br>", 0,
                        new String[]{"2", "<sup>1</sup>/<sub>18</sub>", "<sup>19</sup>/<sub>3</sub>", "<sup>17</sup>/<sub>3</sub>"},
                        1, 125),

                new Question("Multiplication & Division with Fractions",
                        "Which of the following is the <b>greatest<b> in value?", 0,
                        new String[]{"5 &#247; <sup>1</sup>/<sub>5</sub>", "<sup>7</sup>/<sub>3</sub> &times; <sup>9</sup>/<sub>3</sub>", "<sup>2</sup>/<sub>6</sub> &#247; <sup>1</sup>/<sub>36</sub>", "<sup>1</sup>/<sub>3</sub> &times; 18<sup>1</sup>/<sub>3</sub>"},
                        0, 250),

                new Question("Multiplication & Division with Fractions",
                        "George gets 9 questions correct in a test of 11 questions. His friend, Oliver, gets 8 questions correct in a test of 10 questions. Assume each question is worth the same number of marks. Express George's result as a fraction of Oliver's result.", 0,
                        new String[]{"<sup>9</sup>/<sub>8</sub>", "<sup>45</sup>/<sub>44</sub>", "<sup>10</sup>/<sub>11</sub>", "<sup>36</sup>/<sub>55</sub>"},
                        1, 300),
        };

        Question[] decimalsFractions = new Question[]{

                new Question("Fractions & Decimals",
                        "<br>Express <sup>1</sup>/<sub>10</sub> as a <b>decimal</b>.<br>", 0,
                        (float) 0.1, 50),

                new Question("Fractions & Decimals",
                        "Express the fraction represented by the diagram as a <b>decimal</b>.", R.drawable.fraction_3_4,
                        (float) 0.75, 100),

                new Question("Fractions & Decimals",
                        "<br>Express <sup>1</sup>/<sub>8</sub> as a <b>decimal</b>.<br>", 0,
                        (float) 0.125, 125),

                new Question("Fractions & Decimals",
                        "Which of the following is <b>equal</b> to 0.24?", 0,
                        new String[]{"<sup>4</sup>/<sub>20</sub>", "<sup>24</sup>/<sub>10</sub>", "<sup>48</sup>/<sub>50</sub>", "<sup>6</sup>/<sub>25</sub>"},
                        3, 150),

                new Question("Fractions & Decimals",
                        "Which of the following is the <b>greatest</b> in value?", 0,
                        new String[]{"1.2", "<sup>12</sup>/<sub>7</sub>", "1<sup>1</sup>/<sub>10</sub>", "1.12"},
                        1, 150),

                new Question("Fractions & Decimals",
                        "Which of the following is <b>not equal</b> to the rest?", 0,
                        new String[]{"<sup>8</sup>/<sub>5</sub>", "1.6", "1<sup>3</sup>/<sub>5</sub>", "<sup>18</sup>/<sub>10</sub>"},
                        3, 150),
        };


        Question[] difficultFractions = new Question[]{

                new Question("Fractions & Decimals",
                        "<br>Express <sup>26</sup>/<sub>5</sub> &#247; <sup>260</sup>/<sub>5</sub> as a <b>decimal</b>.<br>", 0,
                        (float) 0.1, 200),

                new Question("Multiplication & Division with Fractions",
                        "<br>Simplify <sup>(2 - <sup>1</sup>/<sub>5</sub>)</sup>/<sub>(1 - <sup>2</sup>/<sub>5</sub>)</sub><br>", 0,
                        3, 300),

                new Question("Simplifying Fractions",
                        "<br>Simplify (<sup>400</sup>/<sub>5.2</sub>) &times; (<sup>0.26</sup>/<sub>5</sub>)<br>", 0,
                        4, 300),

                new Question("Simplifying Fractions",
                        "It is given that a > b > c > 1. Which of the following is the greatest in value? <br><br> <i>(Tip: 'a', 'b', and 'c' each represent a number. You do not need to know what they represent.)</i>", 0,
                        new String[]{"<sup>a</sup>/<sub>b</sub> &times; <sup>b</sup>/<sub>a</sub>", "<sup>b</sup>/<sub>a</sub> &#247; <sup>a</sup>/<sub>b</sub>", "<sup>c</sup>/<sub>c</sub> - (<sup>b</sup>/<sub>b</sub> &#247; <sup>a</sup>/<sub>a</sub>)", "<sup>c</sup>/<sub>c</sub> &#247; (<sup>1</sup>/<sub>b</sub> &#247; a)"},
                        3, 500),
        };


        refreshers.add(new Refresher(0, "Expressing Fractions", R.drawable.fraction_basics_1));
        refreshers.add(new Refresher(1, "Simplifying Fractions", R.drawable.fraction_basics_2));
        refreshers.add(new Refresher(2, "Improper Fractions", R.drawable.fraction_basics_3));
        refreshers.add(new Refresher(3, "Mixed Numbers", R.drawable.fraction_basics_4));
        refreshers.add(new Refresher(4, "Comparing Fractions", R.drawable.fraction_basics_5));
        refreshers.add(new Refresher(5, "Addition & Subtraction with Fractions", R.drawable.working_with_fractions_1));
        refreshers.add(new Refresher(6, "Multiplication & Division with Fractions", R.drawable.working_with_fractions_2));
        refreshers.add(new Refresher(7, "Fractions & Decimals", R.drawable.decimals_fractions_1));
        // Reversing the order of the refreshers such that they are displayed correctly:
        Collections.reverse(refreshers);


        questionSets.add(new QuestionSet(0, "Fractions Basics", "Get the hang of expressing, simplifying & comparing fractions and mixed numbers.", R.drawable.qs_fraction_basics,
                fractionBasics));

        questionSets.add(new QuestionSet(1, "Working with Fractions", "Master adding, subtracting, multiplying & dividing fractions, mixed and whole numbers.", R.drawable.qs_working_with_fractions,
                workingWithFractions));

        questionSets.add(new QuestionSet(2, "Fractions & Decimals", "Practice converting between fractions and decimals.", R.drawable.qs_fractions_decimals,
                decimalsFractions));

        questionSets.add(new QuestionSet(3, "Difficult Fractions Questions", "Go above and beyond the syllabus with this set of questions. Discover tricky questions about fractions and understand how to solve them.", R.drawable.qs_difficult_fractions,
                difficultFractions));

        // Learn, Discover, Understand, find out, comprehend, retain
    }

    /**
     * Iterates through each QuestionSet instance, returning the object of the required ID.
     *
     * @param questionSetId The ID of the Question Set
     * @return The QuestionSet instance retrieved, or null if none found.
     */
    public QuestionSet getQuestionSetById(int questionSetId) {
        for (QuestionSet questionSet : questionSets) {
            if (questionSet.getId() == questionSetId) {
                return questionSet;
            }
        }
        return null;
    }

    public ArrayList<Refresher> getRefreshers() {
        return refreshers;
    }


    /**
     * Iterates through each Refresher instance, returning the object of the required ID.
     *
     * @param refresherId The ID of the Refresher
     * @return The Refresher instance retrieved, or null if none found.
     */
    public Refresher getRefresherById(int refresherId) {
        for (Refresher refresher : refreshers) {
            if (refresher.getId() == refresherId) {
                return refresher;
            }
        }
        return null;
    }

    public Account getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(Account userAccount) {
        this.userAccount = userAccount;
    }

    public int getThemeId() {
        return this.themeId;
    }

    public void setThemeId(int id) {
        this.themeId = id;
    }

    /**
     * Creates an interactive pie chart according to the parameters.
     *
     * @param context             Required to get colours.
     * @param pieChart            The PieChart instance that is going to display the pie chart.
     * @param values              The values that are going to be displayed on the pie chart.
     * @param valueTextSize       The text size of the values in sp.
     * @param description         The description of the pie chart that will appear underneath it.
     * @param descriptionTextSize The text size of the description in sp.
     * @param labels              The labels of the pie chart.
     * @param labelTextSize       The text size of the labels in sp.
     * @param labelColourId       The ID colour of the labels from resources.
     * @param centerText          The text that will go to the center of the pie chart.
     * @param centerTextSize      The text size of the center text in sp.
     * @param textColourId        The ID of the colour of other texts - center and label.
     * @param backgroundColourId  The ID of the background colour of the pie chart.
     */
    public void createPieChart(Context context, PieChart pieChart, int[] values, int valueTextSize, String description, int descriptionTextSize, String[] labels, int labelTextSize, int labelColourId, String centerText, int centerTextSize, int textColourId, int backgroundColourId) {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(context.getResources().getColor(backgroundColourId));
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(labelTextSize);
        pieChart.setEntryLabelColor(context.getResources().getColor(labelColourId));
        pieChart.setCenterText(centerText);
        pieChart.setCenterTextColor(context.getResources().getColor(textColourId));
        pieChart.setCenterTextSize(centerTextSize);
        pieChart.getDescription().setText(description);
        pieChart.getDescription().setTextSize(descriptionTextSize);

        ArrayList<PieEntry> entries = new ArrayList<>();
        String label;
        for (int index = 0; index < values.length; index++) {
            label = null;
            if (values[index] > 0) {
                label = labels[index];
            }
            entries.add(new PieEntry(values[index], label));
        }

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, null);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new CustomPercentFormatter(pieChart));
        data.setValueTextSize(valueTextSize);
        data.setValueTextColor(context.getResources().getColor(textColourId));

        pieChart.setData(data);
        pieChart.invalidate();
    }

    /**
     * Checks if any string in an array is empty.
     *
     * @param inputs Array of Strings
     * @return If any string is empty.
     */
    public boolean emptyInputs(String[] inputs) {
        for (String input : inputs) {
            if (input.isEmpty()) {
                return true;
            }
        }
        return false;
    }


    /**
     * Checks if any string in an array is less than the minimum length required.
     *
     * @param inputs    Array of Strings
     * @param minLength Minimum length that each String must match.
     * @return Whether the length of any of the strings is less than the minimum.
     */
    public boolean inputsInvalid(String[] inputs, int minLength) {
        for (String input : inputs) {
            if (input.length() < minLength) {
                return true;
            }
        }
        return false;
    }


    /**
     * Checks if an email is valid.
     *
     * @param target Email
     * @return Whether or not the email is valid.
     */
    public boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    /**
     * Displays a SnackBar according to the parameters.
     *
     * @param layout     The layout where the SnackBar will be displayed
     * @param mainText   The main text of the SnackBar.
     * @param actionText The action text of the SnackBar.
     */
    public void showSnackBar(Context context, ViewGroup layout, String mainText, String actionText) {
        Snackbar.make(layout, mainText, Snackbar.LENGTH_LONG)
                .setAction(actionText, (View v) -> {
                })
                .setActionTextColor(context.getResources().getColor(R.color.YellowOrange))
                .setTextColor(context.getResources().getColor(R.color.YellowOrange))
                .setBackgroundTint(context.getResources().getColor(R.color.OxfordBlue))
                .show();
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        if (targetClass != null) {
            this.targetClass = targetClass;

        }
    }

    public boolean refreshersEnabled() {
        return showRefreshers;
    }

    public void setShowRefreshers(boolean showRefreshers) {
        this.showRefreshers = showRefreshers;
    }

    /**
     * Applies the correct formatting.
     * @param string String to be formatted
     * @return Formatted string
     */
    public String formatString(String string) {
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        try {
            return decimalFormat.format(Float.parseFloat(string));
        } catch (Exception e) {
            return string;
        }

    }


}
