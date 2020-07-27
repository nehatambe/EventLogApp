package com.eventlog.enums;

public enum LogObjectType {

    INVOICE("INVOICE");

    public final String value;

    private LogObjectType(String value) {
        this.value = value;
    }

    public static LogObjectType getByValue(String value) {
        for (LogObjectType e : values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        return null;
    }
}
