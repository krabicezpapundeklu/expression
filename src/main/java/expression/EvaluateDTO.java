package expression;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

public class EvaluateDTO {
    private final boolean useTree;
    private final Expression expression;
    private final String expressionString;
    private final Map<String, String> context;

    @JsonCreator
    public EvaluateDTO(boolean useTree, Expression expression, String expressionString, Map<String, String> context) {
        this.useTree = useTree;
        this.expression = expression;
        this.expressionString = expressionString;
        this.context = context;
    }

    public boolean isUseTree() {
        return useTree;
    }

    public Expression getExpression() {
        return expression;
    }

    public String getExpressionString() {
        return expressionString;
    }

    public Map<String, String> getContext() {
        return context;
    }
}
