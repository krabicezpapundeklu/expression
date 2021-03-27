package com.monster.eligibility;

public class ExpressionList implements ExpressionOrExpressionList {
    private Operator operator;
    private ExpressionOrExpressionList[] expressions;

    public ExpressionList() {
        this(Operator.AND, new ExpressionOrExpressionList[0]);
    }

    public ExpressionList(Operator operator, ExpressionOrExpressionList... expressions) {
        this.operator = operator;
        this.expressions = expressions;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public ExpressionOrExpressionList[] getExpressions() {
        return expressions;
    }

    public void setExpressions(ExpressionOrExpressionList[] expressions) {
        this.expressions = expressions;
    }
}
