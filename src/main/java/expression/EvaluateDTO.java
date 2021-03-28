package expression;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

public class EvaluateDTO {
    private final Expression expression;
    private final Map<String, String> context;

    @JsonCreator
    public EvaluateDTO(Expression expression, Map<String, String> context) {
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
