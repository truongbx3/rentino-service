package com.viettel.vss.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StringUtilsJr extends org.springframework.util.StringUtils {
    private StringUtilsJr() {
    }

    private static final String TEXT_PATTERN = "^[a-zA-Z0-9._]{1,}$";

    public static <T, U> List<U> transform(List<T> list, Function<T, U> function) {
        return list.stream()
                .map(function)
                .collect(Collectors.toList());
    }

    public static String buildRandomFileName(String identifier) {
        if (identifier != null) {
            return identifier + "_" + UUID.randomUUID().toString();
        } else {
            return UUID.randomUUID().toString();
        }
    }

    public static boolean isCheckValue(String value, String valueCompare) {
        return isNotNullOrEmpty(value) && valueCompare.equalsIgnoreCase(value);
    }

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isNotNullOrEmpty(String value) {
        return !isNullOrEmpty(value);
    }

    public static boolean isEmpty(String str) {
        return (str == null || "".equals(str.trim()));
    }

    public static String trimToEmpty(String str) {
        return str == null ? "" : str.trim();
    }

    public static String trimToNull(String str) {
        return str == null ? null : str.trim();
    }
    public static String currencyToText(BigDecimal value) {
        if (value == null) {
            return "0";
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        DecimalFormat df = new DecimalFormat("#,##0.00", symbols);

       return df.format(value);
    }
}
