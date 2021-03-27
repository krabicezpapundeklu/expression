package com.monster.eligibility;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Operator {
    AND("&&"),
    OR("||"),
    EQUAL("=="),
    NOT_EQUAL("!="),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUAL("<="),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUAL(">="),
    CONTAINS("contains"),
    IN("in");

    private final String text;

    Operator(String text) {
        this.text = text;
    }

    @JsonValue
    public String getText() {
        return text;
    }
}
