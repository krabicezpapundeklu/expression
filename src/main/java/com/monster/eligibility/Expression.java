package com.monster.eligibility;

public class Expression implements ExpressionOrExpressionList {
    private String question;
    private Operator operator;
    private String value;

    public Expression() {
        this("", Operator.EQUAL, "");
    }

    public Expression(String question, Operator operator, String value) {
        this.question = question;
        this.operator = operator;
        this.value = value;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
