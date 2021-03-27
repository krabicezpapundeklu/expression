package com.monster.eligibility;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EvaluateDTO {
    private final Expression expression;
    private final Map<String, String> context;

    @JsonCreator
    public EvaluateDTO(@JsonProperty("expression") Expression expression, @JsonProperty("context") Map<String, String> context) {
        this.expression = expression;
        this.context = context;
    }

    public Expression getExpression() {
        return expression;
    }

    public Map<String, String> getContext() {
        return context;
    }
}
