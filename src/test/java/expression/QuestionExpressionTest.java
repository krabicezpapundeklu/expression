package expression;

import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static expression.AnswerType.*;
import static expression.Operator.*;
import static org.testng.Assert.*;

@Test
public class QuestionExpressionTest {
    public void testBooleanQuestions() throws Exception {
        assertTrue(eval(BOOLEAN, "true", EQUAL, "true"));
        assertFalse(eval(BOOLEAN, "true", EQUAL, "false"));

        assertFalse(eval(BOOLEAN, "true", NOT_EQUAL, "true"));
        assertTrue(eval(BOOLEAN, "true", NOT_EQUAL, "false"));

        assertFalse(eval(BOOLEAN, "false", EQUAL, "true"));
        assertTrue(eval(BOOLEAN, "false", EQUAL, "false"));

        assertTrue(eval(BOOLEAN, "false", NOT_EQUAL, "true"));
        assertFalse(eval(BOOLEAN, "false", NOT_EQUAL, "false"));
    }

    public void testNumberQuestions() throws Exception {
        assertFalse(eval(NUMBER, "10", EQUAL, "0"));
        assertTrue(eval(NUMBER, "10", EQUAL, "10"));
        assertFalse(eval(NUMBER, "10", EQUAL, "20"));

        assertTrue(eval(NUMBER, "10", NOT_EQUAL, "0"));
        assertFalse(eval(NUMBER, "10", NOT_EQUAL, "10"));
        assertTrue(eval(NUMBER, "10", NOT_EQUAL, "20"));

        assertFalse(eval(NUMBER, "10", LESS_THAN, "0"));
        assertFalse(eval(NUMBER, "10", LESS_THAN, "10"));
        assertTrue(eval(NUMBER, "10", LESS_THAN, "20"));

        assertFalse(eval(NUMBER, "10", LESS_THAN_OR_EQUAL, "0"));
        assertTrue(eval(NUMBER, "10", LESS_THAN_OR_EQUAL, "10"));
        assertTrue(eval(NUMBER, "10", LESS_THAN_OR_EQUAL, "20"));

        assertTrue(eval(NUMBER, "10", GREATER_THAN, "0"));
        assertFalse(eval(NUMBER, "10", GREATER_THAN, "10"));
        assertFalse(eval(NUMBER, "10", GREATER_THAN, "20"));

        assertTrue(eval(NUMBER, "10", GREATER_THAN_OR_EQUAL, "0"));
        assertTrue(eval(NUMBER, "10", GREATER_THAN_OR_EQUAL, "10"));
        assertFalse(eval(NUMBER, "10", GREATER_THAN_OR_EQUAL, "20"));

        assertFalse(eval(NUMBER, "10", IN, ""));
        assertFalse(eval(NUMBER, "10", IN, "0"));
        assertTrue(eval(NUMBER, "10", IN, "0, 10, 20"));
    }

    public void testTextQuestions() throws Exception {
        assertTrue(eval(TEXT, "", EQUAL, ""));
        assertFalse(eval(TEXT, "", NOT_EQUAL, ""));

        assertTrue(eval(TEXT, "", CONTAINS, ""));
        assertFalse(eval(TEXT, "", CONTAINS, "B"));

        assertFalse(eval(TEXT, "ABC", EQUAL, ""));
        assertTrue(eval(TEXT, "ABC", EQUAL, "ABC"));

        assertTrue(eval(TEXT, "ABC", NOT_EQUAL, ""));
        assertFalse(eval(TEXT, "ABC", NOT_EQUAL, "ABC"));

        assertTrue(eval(TEXT, "ABC", CONTAINS, "B"));
        assertFalse(eval(TEXT, "ABC", CONTAINS, "X"));
    }

    private boolean eval(AnswerType answerType, String left, Operator operator, String right) throws Exception {
        Expression expression = new QuestionExpression("Q", operator, right);
        Map<String, String> context = new HashMap<>();
        context.put("Q", left);
        return expression.evaluate(context, Collections.singletonList(new Question("Q", answerType)));
    }
}
