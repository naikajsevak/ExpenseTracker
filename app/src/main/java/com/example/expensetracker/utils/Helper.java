package com.example.expensetracker.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {
    public static String formateDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM, YYYY");
        return simpleDateFormat.format(date);
    }

}
