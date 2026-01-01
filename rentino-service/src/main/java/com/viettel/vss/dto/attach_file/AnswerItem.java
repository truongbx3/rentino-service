package com.viettel.vss.dto.attach_file;

public enum AnswerItem {


    DEVICE_FRAME("DEVICE_FRAME"),
    FRONT_GRASS("FRONT_GRASS"),
    FRONT_CAMERA("FRONT_CAMERA"),
    BACK_CAMERA("BACK_CAMERA"),
    FLASH_CAMERA("FLASH_CAMERA");
    private final String function;

    AnswerItem(String function) {
        this.function = function;
    }

    public String getFunction() {
        return function;
    }

    @Override
    public String toString() {
        return function;
    }

    // Optional: convert String -> enum
    public static AnswerItem fromString(String function) {
        for (AnswerItem a : AnswerItem.values()) {
            if (a.function.equalsIgnoreCase(function)) {
                return a;
            }
        }
        throw new IllegalArgumentException("Unknown action: " + function);
    }
}
