package expression;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


@Test
public class ParserTest {
    public void testParse() throws Exception {
        match("", "");
        match("A == \"A\"", "A == \"A\"");
        match("(A == \"A\")", "A == \"A\"");
        match("A == \"A\" && B == \"B\"", "A == \"A\" && B == \"B\"");
        match("(A == \"A\" && B == \"B\")", "A == \"A\" && B == \"B\"");
        match("(A == \"A\") && B == \"B\"", "(A == \"A\") && B == \"B\"");
        match("((A == \"A\") && B == \"B\")", "(A == \"A\") && B == \"B\"");
        match("((A == \"A\" && B == \"B\") && C == \"C\")", "(A == \"A\" && B == \"B\") && C == \"C\"");
        match("US_CITIZEN == \"true\" && (AGE >= \"21\" || NAME contains \"old\")", "US_CITIZEN == \"true\" && (AGE >= \"21\" || NAME contains \"old\")");
        match("(US_CITIZEN == \"true\" && (AGE >= \"21\" || NAME contains \"old\"))", "US_CITIZEN == \"true\" && (AGE >= \"21\" || NAME contains \"old\")");
        match("(AGE == \"1\" && AGE == \"2\") && AGE == \"3\" && AGE == \"4\" && (AGE == \"5\" && AGE == \"6\")", "(AGE == \"1\" && AGE == \"2\") && AGE == \"3\" && AGE == \"4\" && (AGE == \"5\" && AGE == \"6\")");
        match("((AGE == \"1\" && AGE == \"2\") && AGE == \"3\" && AGE == \"4\" && (AGE == \"5\" && AGE == \"6\"))", "(AGE == \"1\" && AGE == \"2\") && AGE == \"3\" && AGE == \"4\" && (AGE == \"5\" && AGE == \"6\")");
    }

    private static void match(String input, String output) throws Exception {
        Expression expression = Parser.parse(input);
        assertEquals(expression.toString(), output);
    }
}
