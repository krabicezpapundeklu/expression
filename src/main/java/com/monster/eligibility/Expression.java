package com.monster.eligibility;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ExpressionList.class, name = "expressionList"),
    @JsonSubTypes.Type(value = QuestionExpression.class, name = "questionExpression")
})
public interface Expression {
    boolean evaluate(Map<String, String> context, List<Question> questions) throws Exception;
}
