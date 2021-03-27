package com.monster.eligibility;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = "type")
public interface Expression {
    boolean evaluate(Map<String, String> context, List<Question> questions) throws Exception;
}
