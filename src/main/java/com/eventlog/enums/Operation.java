package com.eventlog.enums;

public enum Operation {
  CREATE("CREATE"),
  UPDATE("UPDATE"),
  DELETE("DELETE");

  public final String value;

  private Operation(String value) {
    this.value = value;
  }

  public static Operation getByValue(String value) {
    for (Operation e : values()) {
      if (e.value.equals(value)) {
        return e;
      }
    }
    return null;
  }
}
