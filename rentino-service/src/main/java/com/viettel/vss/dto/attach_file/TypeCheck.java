package com.viettel.vss.dto.attach_file;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TypeCheck {


    LOAI_1("LOAI_1",1),
    LOAI_2("LOAI_2",2),
    LOAI_3("LOAI_3",3),
    LOAI_4("LOAI_4",4),
    LOAI_5("LOAI_5",5),
    LOAI_0("LOAI_0",0);
    private final String type;
    private final int  valueType;

    TypeCheck(String type,int  valueType) {
        this.type = type;
        this.valueType=valueType;
    }

    public String getType() {
        return type;
    }
    public int getValueType() {
        return valueType;
    }
    
    @JsonCreator
    public static TypeCheck fromJson(String value) {
        if (value == null) return null;
        return TypeCheck.valueOf(value.toUpperCase()); // convert JSON → UPPERCASE
    }

    @JsonValue
    public String toJson() {
        return this.name().toLowerCase(); // serialize enum → lowercase (nếu bạn muốn)
    }
    @Override
    public String toString() {
        return type;
    }

    // Optional: convert String -> enum
    public static TypeCheck fromString(String function) {
        for (TypeCheck a : TypeCheck.values()) {
            if (a.getType().equalsIgnoreCase(function)) {
                return a;
            }
        }
        throw new IllegalArgumentException("Unknown action: " + function);
    }
}
