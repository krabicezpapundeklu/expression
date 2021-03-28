package expression;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ExpressionList implements Expression {
    private final Operator operator;
    private final Expression[] expressions;

    @JsonCreator
    public ExpressionList(Operator operator, Expression... expressions) {
        this.operator = operator;
        this.expressions = expressions;
    }

    public Operator getOperator() {
        return operator;
    }

    public Expression[] getExpressions() {
        return expressions;
    }

    @Override
    public boolean evaluate(Map<String, String> context, List<Question> questions) throws Exception {
        switch(operator) {
            case AND:
                for(Expression expression : expressions) {
                    if(!expression.evaluate(context, questions)) {
                        return false;
                    }
                }

                return true;

            case OR:
                for(Expression expression : expressions) {
                    if(expression.evaluate(context, questions)) {
                        return true;
                    }
                }

                return expressions.length == 0;

            default:
                throw new Exception("Invalid operator " + operator);
        }
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }

        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        ExpressionList that = (ExpressionList) o;
        return operator == that.operator && Arrays.equals(expressions, that.expressions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(operator);
        result = 31 * result + Arrays.hashCode(expressions);
        return result;
    }

    @Override
    public String toString() {
        return toString(0);
    }

    @Override
    public String toString(int level) {
        if(expressions.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        if(level > 0) {
            sb.append('(');
        }

        for(int i = 0; i < expressions.length; ++i) {
            if(i > 0) {
                sb.append(' ').append(operator.getText()).append(' ');
            }

            sb.append(expressions[i].toString(level + 1));
        }

        if(level > 0) {
            sb.append(')');
        }

        return sb.toString();
    }
}
