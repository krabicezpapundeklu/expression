package expression;

import org.testng.annotations.Test;

import static expression.Operator.*;
import static expression.Token.*;
import static org.testng.Assert.*;

@Test
public class LexerTest {
    private Lexer lexer;

    public void testIdentifiers() throws Exception {
        lexer = new Lexer(" a abc _abc_ X123 ");

        match(ID, "a");
        match(ID, "abc");
        match(ID, "_abc_");
        match(ID, "X123");

        matchEnd();
    }

    public void testParens() throws Exception {
        lexer = new Lexer("()");

        match(OPEN_PAREN);
        match(CLOSE_PAREN);

        matchEnd();
    }

    public void testOperators() throws Exception {
        lexer = new Lexer("&& || == != < <= > >= contains in");

        match(AND);
        match(OR);
        match(EQUAL);
        match(NOT_EQUAL);
        match(LESS_THAN);
        match(LESS_THAN_OR_EQUAL);
        match(GREATER_THAN);
        match(GREATER_THAN_OR_EQUAL);
        match(CONTAINS);
        match(IN);

        matchEnd();
    }

    public void testStrings() throws Exception {
        lexer = new Lexer("\"\"\"a\" \"abc\" \"\\\\\" \"a\\\"b\"");

        match(STRING, "");
        match(STRING, "a");
        match(STRING, "abc");
        match(STRING, "\\");
        match(STRING, "a\"b");

        matchEnd();
    }

    private void match(Token token) throws Exception {
        assertTrue(lexer.read());
        assertEquals(lexer.getToken(), token);
    }

    private void match(Token token, String text) throws Exception {
        assertTrue(lexer.read());
        assertEquals(lexer.getToken(), token);
        assertEquals(lexer.getText(), text);
    }

    private void match(Operator operator) throws Exception {
        assertTrue(lexer.read());
        assertEquals(lexer.getToken(), OPERATOR);
        assertEquals(lexer.getOperator(), operator);
    }

    private void matchEnd() throws Exception {
        assertFalse(lexer.read());
        assertEquals(EOI, lexer.getToken());
    }

}
