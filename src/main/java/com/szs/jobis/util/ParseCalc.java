package com.szs.jobis.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParseCalc {
    
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");

    private static final SimpleDateFormat formatterTZ = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final DecimalFormat decFormat = new DecimalFormat("###,###");
    public static long ObjToLong(Object obj){
       return Long.parseLong(String.valueOf(obj).replaceAll(",", ""));
    }
    public static double objToDouble(Object obj){
        return Double.valueOf (String.valueOf(obj).replaceAll(",", ""));
    }

    public static Date ObjToDate(Object obj) throws ParseException{
        return formatter.parse(String.valueOf(obj));
    }
    public static Date ObjToDateTZ(Object obj) throws ParseException{
        return formatterTZ.parse(String.valueOf(obj));
    }
    public static String LongToString(long value){
        return decFormat.format(value);
    }
    public static String BigDecimalToString(BigDecimal value){
        return decFormat.format(value);
    }
    public static BigDecimal objToBigDecimal(Object obj){
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(obj).replaceAll(",", ""));
        return bigDecimal;
    }

}
