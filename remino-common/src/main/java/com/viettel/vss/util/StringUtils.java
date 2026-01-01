package com.viettel.vss.util;

import java.util.function.Consumer;

public class StringUtils extends org.springframework.util.StringUtils {
    public static boolean isNullOrEmpty(String str) {
        return (str == null || "".equals(str.trim()));
    }
    public static <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
