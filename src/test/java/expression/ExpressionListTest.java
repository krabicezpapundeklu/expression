package expression;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

@Test
public class ExpressionListTest {
    private static final Expression TRUE_EXPRESSION = (context, questions) -> true;
    private static final Expression FALSE_EXPRESSION = (context, questions) -> false;

    private Expression expression;

    public void testAnd() throws Exception {
        expression = new ExpressionList(Operator.AND);
        assertTrue(expression.evaluate(null, null));

        expression = new ExpressionList(Operator.AND, TRUE_EXPRESSION, TRUE_EXPRESSION, TRUE_EXPRESSION);
        assertTrue(expression.evaluate(null, null));

        expression = new ExpressionList(Operator.AND, TRUE_EXPRESSION, TRUE_EXPRESSION, FALSE_EXPRESSION);
        assertFalse(expression.evaluate(null, null));
    }

    public void testOr() throws Exception {
        expression = new ExpressionList(Operator.OR);
        assertTrue(expression.evaluate(null, null));

        expression = new ExpressionList(Operator.OR, FALSE_EXPRESSION, FALSE_EXPRESSION, TRUE_EXPRESSION);
        assertTrue(expression.evaluate(null, null));

        expression = new ExpressionList(Operator.OR, FALSE_EXPRESSION, FALSE_EXPRESSION, FALSE_EXPRESSION);
        assertFalse(expression.evaluate(null, null));
    }
}
