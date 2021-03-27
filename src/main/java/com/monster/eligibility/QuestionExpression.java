package com.monster.eligibility;

import static com.monster.eligibility.Operator.IN;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;

public class QuestionExpression implements Expression {
    private final String question;
    private final Operator operator;
    private final String value;

    @JsonCreator
    public QuestionExpression(String question, Operator operator, String value) {
        this.question = question;
        this.operator = operator;
        this.value = value;
    }

    public String getQuestion() {
        return question;
    }

    public Operator getOperator() {
        return operator;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean evaluate(Map<String, String> context, List<Question> questions) throws Exception {
        AnswerType answerType = questions.stream().filter(q -> q.getName().equals(question)).findFirst().get().getAnswerType();

        Object q = map(answerType, context.get(question));

        if(operator == IN) {
            return Arrays.stream(value.split(" ,;")).anyMatch(v -> Objects.equals(q, map(answerType, v)));
        }

        Object a = map(answerType, value);

        switch(operator) {
            case EQUAL:
                return Objects.equals(q, a);

            case NOT_EQUAL:
                return !Objects.equals(q, a);

            case LESS_THAN:
                return ((Comparable)q).compareTo(a) < 0;

            case LESS_THAN_OR_EQUAL:
                return ((Comparable)q).compareTo(a) <= 0;

            case GREATER_THAN:
                return ((Comparable)q).compareTo(a) > 0;

            case GREATER_THAN_OR_EQUAL:
                return ((Comparable)q).compareTo(a) >= 0;

            case CONTAINS:
                return ((String)q).contains((String)a);

            default:
                throw new Exception("Invalid operator " + operator);
        }
    }

    @Override
    public String toString() {
        return question + ' ' + operator.getText() + " \"" + value.replaceAll("\"", "\\\\\"") + '"';
    }

    private static Object map(AnswerType answerType, String value) {
        switch(answerType) {
            case BOOLEAN:
                return "true".equals(value);

            case NUMBER:
                return Long.parseLong(value);

            default:
                return value;
        }
    }
}
