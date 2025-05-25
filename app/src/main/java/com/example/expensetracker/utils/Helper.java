package com.example.expensetracker.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Helper class providing utility methods for formatting dates.
 */
public class Helper {

    /**
     * Formats a given Date object into a string with the pattern "dd MMMM, YYYY".
     * Example output: "25 May, 2025"
     *
     * @param date the Date object to format
     * @return a formatted date string
     */
    public static String formateDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM, yyyy");
        return simpleDateFormat.format(date);
    }

    /**
     * Formats a given Date object to show only the month and year.
     * Example output: "May, 2025"
     *
     * @param date the Date object to format
     * @return a formatted string representing month and year
     */
    public static String formateDateByMonth(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM, yyyy");
        return simpleDateFormat.format(date);
    }
}
