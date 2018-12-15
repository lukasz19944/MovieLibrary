package library.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public static String format(Date date) {
        if (date == null) {
            return null;
        }
        return dateFormat.format(date);
    }

    public static Date parse(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static boolean validDate(String dateString) {
        return DateUtil.parse(dateString) != null;
    }
}
