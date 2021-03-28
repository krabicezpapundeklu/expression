package expression;

import java.util.List;
import java.util.Map;

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
            case AND: {
                for(Expression expression : expressions) {
                    if(!expression.evaluate(context, questions)) {
                        return false;
                    }
                }

                return true;
            }

            case OR: {
                for(Expression expression : expressions) {
                    if(expression.evaluate(context, questions)) {
                        return true;
                    }
                }

                return false;
            }

            default:
                throw new Exception("Invalid operator " + operator);
        }
    }

    @Override
    public String toString() {
        if(expressions.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < expressions.length; ++i) {
            if(i > 0) {
                sb.append(' ').append(operator.getText()).append(' ');
            }

            sb.append(expressions[i]);
        }

        if(expressions.length > 1) {
            sb.insert(0, '(').append(')');
        }

        return sb.toString();
    }
}
