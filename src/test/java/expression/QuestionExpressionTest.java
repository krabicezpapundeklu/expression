package expression;

import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static expression.Operator.*;
import static org.testng.AssertJUnit.*;

@Test
public class QuestionExpressionTest {
    public void testBooleanQuestions() throws Exception {
        Map<String, String> context = new HashMap<>();
        List<Question> questions = Collections.singletonList(new Question("Q", AnswerType.BOOLEAN));
        Expression expression;

        context.put("Q", "true");

        expression = new QuestionExpression("Q", EQUAL, "true");
        assertTrue(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", EQUAL, "false");
        assertFalse(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", NOT_EQUAL, "true");
        assertFalse(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", NOT_EQUAL, "false");
        assertTrue(expression.evaluate(context, questions));

        context.put("Q", "false");

        expression = new QuestionExpression("Q", EQUAL, "true");
        assertFalse(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", EQUAL, "false");
        assertTrue(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", NOT_EQUAL, "true");
        assertTrue(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", NOT_EQUAL, "false");
        assertFalse(expression.evaluate(context, questions));
    }

    public void testNumberQuestions() throws Exception {
        Map<String, String> context = new HashMap<>();
        List<Question> questions = Collections.singletonList(new Question("Q", AnswerType.NUMBER));
        Expression expression;

        context.put("Q", "10");

        expression = new QuestionExpression("Q", EQUAL, "0");
        assertFalse(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", EQUAL, "10");
        assertTrue(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", EQUAL, "20");
        assertFalse(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", NOT_EQUAL, "0");
        assertTrue(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", NOT_EQUAL, "10");
        assertFalse(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", NOT_EQUAL, "20");
        assertTrue(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", LESS_THAN, "0");
        assertFalse(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", LESS_THAN, "10");
        assertFalse(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", LESS_THAN, "20");
        assertTrue(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", LESS_THAN_OR_EQUAL, "0");
        assertFalse(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", LESS_THAN_OR_EQUAL, "10");
        assertTrue(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", LESS_THAN_OR_EQUAL, "20");
        assertTrue(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", GREATER_THAN, "0");
        assertTrue(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", GREATER_THAN, "10");
        assertFalse(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", GREATER_THAN, "20");
        assertFalse(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", GREATER_THAN_OR_EQUAL, "0");
        assertTrue(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", GREATER_THAN_OR_EQUAL, "10");
        assertTrue(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", GREATER_THAN_OR_EQUAL, "20");
        assertFalse(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", IN, "");
        assertFalse(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", IN, "0");
        assertFalse(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", IN, "0, 10, 20");
        assertTrue(expression.evaluate(context, questions));
    }

    public void testTextQuestions() throws Exception {
        Map<String, String> context = new HashMap<>();
        List<Question> questions = Collections.singletonList(new Question("Q", AnswerType.TEXT));
        Expression expression;

        context.put("Q", "");

        expression = new QuestionExpression("Q", EQUAL, "");
        assertTrue(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", NOT_EQUAL, "");
        assertFalse(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", CONTAINS, "B");
        assertFalse(expression.evaluate(context, questions));

        context.put("Q", "ABC");

        expression = new QuestionExpression("Q", EQUAL, "");
        assertFalse(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", EQUAL, "ABC");
        assertTrue(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", NOT_EQUAL, "");
        assertTrue(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", NOT_EQUAL, "ABC");
        assertFalse(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", CONTAINS, "B");
        assertTrue(expression.evaluate(context, questions));

        expression = new QuestionExpression("Q", CONTAINS, "X");
        assertFalse(expression.evaluate(context, questions));
    }
}
