package com.viettel.vss.dto.attach_file;

public enum FunctionItem {


    VIBRATION("VIBRATION"),
    WIFI("WIFI"),
    VOLUME_UP("VOLUME_UP"),
    VOLUME_DOWN("VOLUME_DOWN"),
    POWER("POWER"),
    BLUE_TOOTH("BLUE_TOOTH"),
    GPS("GPS"),
    SPEAKER("SPEAKER"),
    MIC("MIC"),
    CHARGING("CHARGING"),
    SIM("SIM"),
    HEADPHONE("HEADPHONE"),
    BIOMETRICS("BIOMETRICS"),
    SCREENSHOT_FRONT("SCREENSHOT_FRONT"),
    SCREENSHOT_REAR("SCREENSHOT_REAR"),
    EXTERNAL_SPEAKER("EXTERNAL_SPEAKER"),
    INTERNAL_SPEAKER("INTERNAL_SPEAKER"),
    TOUCH_SCREEN("TOUCH_SCREEN");
    private final String function;

    FunctionItem(String function) {
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
    public static FunctionItem fromString(String function) {
        for (FunctionItem a : FunctionItem.values()) {
            if (a.function.equalsIgnoreCase(function)) {
                return a;
            }
        }
        throw new IllegalArgumentException("Unknown action: " + function);
    }
}
