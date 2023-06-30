package com.example.PocketMaths;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

/**
 * This class is used instead of the default PercentageFormatter to hide the percentage values of
 * the pie chart if the value is 0.
 * The values are passed into the pie chart even if they are 0 to maintain the correct color order.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * CustomPercentFormatter extends ValueFormatter to gain access to its methods.
 * It does not extend the PercentFormatter directly, since it may be changed in the future like it
 * has been changed before.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

public class CustomPercentFormatter extends ValueFormatter {

    public DecimalFormat decimalFormat;
    private PieChart pieChart;

    /**
     * Constructor
     * Used for formatting decimal values.
     */
    public CustomPercentFormatter() {
        decimalFormat = new DecimalFormat("###,###,##0.0");
    }

    /**
     * Constructor
     * For pie charts, percentage sign is added depending on external factors.
     *
     * @param pieChart Pie chart with which it is used.
     */
    public CustomPercentFormatter(PieChart pieChart) {
        this();
        this.pieChart = pieChart;
    }

    /**
     * Applies the correct formatting and returns the value.
     *
     * @param value Value to be formatted.
     * @return Formatted Value
     */
    @Override
    public String getFormattedValue(float value) {
        // Only creating value label if the value is not 0:
        if (value != 0) {
            return decimalFormat.format(value) + " %";
        }
        return "";
    }

    /**
     * Applies the correct formatting for pie chart labels.
     *
     * @param value    Value to be formatted
     * @param pieEntry An entry of the pie chart that the label is for
     * @return Formatted Value
     */
    @Override
    public String getPieLabel(float value, PieEntry pieEntry) {
        if (pieChart != null && pieChart.isUsePercentValuesEnabled()) {
            // Converting value to percentage:
            return getFormattedValue(value);
        } else {
            // If percentage values are disabled, returning decimal:
            return decimalFormat.format(value);
        }
    }
}

