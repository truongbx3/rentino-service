package com.viettel.vss.util;


import com.google.common.base.Strings;
import com.viettel.vss.constant.CommonConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * DateUtil handle date and time of the system
 */
public class DateUtil {

    protected static final Logger iLogger = LogManager.getLogger(DateUtil.class);

    private DateUtil() {
    }

    public static Date convertStringToDate(String dateInString, String dateFormat) {
        try {
            DateFormat formatter = new SimpleDateFormat(dateFormat);
            if (Strings.isNullOrEmpty(dateInString)) {
                return null;
            }
            return formatter.parse(dateInString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date convertStringToDate(String dateInString, String dateFormat, String timeZone) {
        try {
            DateFormat formatter = new SimpleDateFormat(dateFormat);
            formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
            if (Strings.isNullOrEmpty(dateInString)) {
                return null;
            }
            return formatter.parse(dateInString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date convertStringToDateForAsia(String dateInString, String dateFormat) {
        try {
            DateFormat formatter = new SimpleDateFormat(dateFormat);
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
            if (Strings.isNullOrEmpty(dateInString)) {
                return null;
            }
            return formatter.parse(dateInString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String convertDateToString(Date date, String datePattern) {
        if (date == null) {
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat(datePattern);
        return dateFormat.format(date);
    }

    public static String convertDateToString(Date date) {
        StringBuilder sb = new StringBuilder();
        if (date != null) {
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            sb.append(localDate.getDayOfMonth()).append("/").append(localDate.getMonthValue()).append("/")
                    .append(localDate.getYear());
        }
        return sb.toString();
    }

    public static String convertDateToString2(Date date) {
        StringBuilder sb = new StringBuilder();
        if (date != null) {
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            sb.append(localDate.getYear()).append("-").append(localDate.getMonthValue()).append("-")
                    .append(localDate.getDayOfMonth());
        }
        return sb.toString();
    }

    public static Long minusDate(String date1, String date2) {
        try {
            DateFormat df = new SimpleDateFormat(CommonConstants.DATE_TIME_FORMAT.DATE_FORMAT_DD_MM_YYYY);
            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            if (!Strings.isNullOrEmpty(date1)) {
                calendar1.setTime(df.parse(date1));
            }
            if (!Strings.isNullOrEmpty(date2)) {
                calendar2.setTime(df.parse(date2));
            }
            return (calendar2.getTime().getTime() - calendar1.getTime().getTime()) / (24 * 3600 * 1000);
        } catch (ParseException e) {
            return null;
        }
    }

    public static int getValueFromDate(Date date, int unit) {
        Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);
        return calendar.get(unit) + 1;
    }


    public static Date parseStringToDate(Object dateString) {
        try {
            if (dateString == null) {
                return null;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CommonConstants.DATE_TIME_FORMAT.DATE_FORMAT_YYYY_MM_DD);
            return simpleDateFormat.parse(dateString.toString());
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseStringToDatetime(Object dateString) {
        try {
            if (dateString == null) {
                return null;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CommonConstants.DATE_TIME_FORMAT.DATE_TIME_FORMAT_DD_MM_YYYY_HH_MM);
            return simpleDateFormat.parse(dateString.toString());
        } catch (ParseException e) {
            return null;
        }
    }

    public static double calculateAge(Date dob) {
        try {
            DateFormat df = new SimpleDateFormat(CommonConstants.DATE_TIME_FORMAT.DATE_FORMAT_DD_MM_YYYY);
            Calendar birth = Calendar.getInstance();
            Calendar now = Calendar.getInstance();
            birth.setTime(df.parse(df.format(dob)));
            now.setTime(new Date());
            double numberOfDaysOld = (double)
                    (now.getTime().getTime() - birth.getTime().getTime()) / (24 * 3600 * 1000);
            return numberOfDaysOld / 365;
        } catch (Exception ex) {
            iLogger.error(ex.getMessage(), ex);
            return 0;
        }
    }

    public static Date addDate(Date date, int plus) {
        if (date == null) {
            return null;
        }
        return new Date(date.getTime() + 1000L * 60 * 60 * 24 * plus);
    }

    public static int compareDate(String dateStr1, String dateStr2, String pattern)
            throws ParseException {
        SimpleDateFormat sdformat = new SimpleDateFormat(pattern);
        Date d1 = sdformat.parse(dateStr1);
        Date d2 = sdformat.parse(dateStr2);
        return d1.compareTo(d2);
    }

    public static int compareTo(Date thisTime, Date anotherDate) {
        thisTime = setTimeToMidnight(thisTime);
        anotherDate = setTimeToMidnight(anotherDate);
        return thisTime.compareTo(anotherDate);
    }

    public static Date setTimeToMidnight(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date setTimeToMidnightNextDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    public static Timestamp convertDateToTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }


    public static Date convertTimestampToDate(Timestamp timestamp, String pattern) {
        try {
            DateFormat df = new SimpleDateFormat(pattern);
            return df.parse(df.format(timestamp));
        } catch (Exception ex) {
            iLogger.error(ex.getMessage(), ex);
            return null;
        }
    }


    public static Date covertDateToAnotherFormat(Date date, String pattern) {
        try {
            if (date == null) {
                return null;
            }
            DateFormat df = new SimpleDateFormat(pattern);
            return df.parse(df.format(date));
        } catch (Exception ex) {
            iLogger.error(ex.getMessage(), ex);
            return null;
        }
    }


    public static boolean isValidDateFormat(String date) {
        /**
         * Regular Expression that matches String with format dd/MM/yyyy.
         * dd -> 01-31
         * MM -> 01-12
         * yyyy -> 4 digit number
         */
        String pattern = "(0?[1-9]|[12][0-9]|3[01])\\/(0?[1-9]|1[0-2])\\/([0-9]{4})";
        boolean result = false;
        if (date.matches(pattern)) {
            result = true;
        }
        return result;
    }


    public static int calculateYearsOld(Date birthDay) {
        Date current = new Date();
        Date mydob = covertDateToAnotherFormat(birthDay, CommonConstants.DATE_TIME_FORMAT.DATE_FORMAT_DD_MM_YYYY);
        if (mydob != null && mydob.before(current)) {
            // Get default Time zone Id.
            ZoneId defaultZoneId = ZoneId.systemDefault();
            // Convert Date mydob to LocalDate
            Instant instant1 = mydob.toInstant();
            LocalDate localeDateMyDob = instant1.atZone(defaultZoneId).toLocalDate();
            // Convert Date current to LocalDate
            Instant instant2 = current.toInstant();
            LocalDate localeDateCurrent = instant2.atZone(defaultZoneId).toLocalDate();
            Period period = Period.between(localeDateMyDob, localeDateCurrent);
            return period.getYears();
        } else {
            return 0;
        }
    }

    public static Date increaseDate(Date date, Integer value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.roll(Calendar.DATE, value);
        return calendar.getTime();
    }

    public static Date setMinFromDate(Date date) {
        if (Objects.isNull(date)) return null;
        Calendar cal = Calendar.getInstance(); // locale-specific
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date setMaxToDate(Date date) {
        if (Objects.isNull(date)) return null;
        Calendar cal = Calendar.getInstance(); // locale-specific
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    public static String subDateTimeToString(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        long timeInSeconds = diff / 1000;
        long hours = timeInSeconds / 3600;
        timeInSeconds = timeInSeconds - (hours * 3600);
        long minutes = timeInSeconds / 60;
        timeInSeconds = timeInSeconds - (minutes * 60);
        long seconds = timeInSeconds;
        return convertToZero(hours) + ":" + convertToZero(minutes) + ":" + convertToZero(seconds);

    }

    public static String convertDurationToString(Long duration) {
        if (duration == null) {
            return "00:00:00";
        }
        Duration d = Duration.ofMillis(duration);
        int hours = d.toHoursPart();
        int minutes = d.toMinutesPart();
        int seconds = d.toSecondsPart();
        return convertToZero(hours) + ":" + convertToZero(minutes) + ":" + convertToZero(seconds);
    }

    public static String convertToZero(long number) {
        if (number >= 10) {
            return String.valueOf(number);
        }
        return "0" + number;
    }

    public static boolean isValidFormat(String strDate, String dateFormat) {
        DateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(strDate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
