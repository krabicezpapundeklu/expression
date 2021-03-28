package expression;

import java.util.ArrayList;
import java.util.List;

import static expression.Operator.*;
import static expression.Token.*;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) throws Exception {
        this.lexer = lexer;
        lexer.read();
    }

    public static Expression parse(String expression) throws Exception {
        return new Parser(new Lexer(expression)).parse();
    }

    public Expression parse() throws Exception {
        if(lexer.getToken() == EOI) {
            return new ExpressionList(AND);
        }

        ExpressionList expression = parseExpressionList();

        if(expression.getExpressions().length == 1 && expression.getExpressions()[0] instanceof ExpressionList) {
            return expression.getExpressions()[0];
        }

        expect(EOI);

        return expression;
    }

    private ExpressionList parseExpressionList() throws Exception {
        List<Expression> expressions = new ArrayList<>();
        Operator operator = null;

        for(;;) {
            if(match(OPEN_PAREN)) {
                expressions.add(parseExpressionList());
                expect(CLOSE_PAREN);
            } else {
                expressions.add(parseQuestionExpression());
            }

            if(lexer.getToken() == EOI || lexer.getToken() == CLOSE_PAREN) {
                break;
            }

            Operator o = expectOperator(true);

            if(operator != null && operator != o) {
                throw new Exception("Wrong operator '" + o.getText() + "', only '" + operator.getText() + "' allowed here.");
            }

            operator = o;
        }

        return new ExpressionList(operator == null ? AND : operator, expressions.toArray(new Expression[0]));
    }

    private QuestionExpression parseQuestionExpression() throws Exception {
        String question = expectID();
        Operator operator = expectOperator(false);
        String answer = expectString();

        return new QuestionExpression(question, operator, answer);
    }

    private void expect(Token token) throws Exception {
        if(!match(token)) {
            throw new Exception("Expected " + token + ", but found " + lexer.getToken());
        }
    }

    private String expectID() throws Exception {
        if(lexer.getToken() == ID) {
            String id = lexer.getText();
            lexer.read();
            return id;
        }

        if(lexer.getToken() == OPERATOR && (lexer.getOperator() == CONTAINS || lexer.getOperator() == IN)) {
            String id = lexer.getOperator().getText();
            lexer.read();
            return id;
        }

        throw new Exception("Expected ID, but found " + lexer.getToken());
    }

    private Operator expectOperator(boolean inExpressionList) throws Exception {
        if(lexer.getToken() != OPERATOR) {
            throw new Exception("Expected operator, but found " + lexer.getToken());
        }

        Operator operator = lexer.getOperator();
        boolean isListOperator = operator == AND || operator == OR;

        if(inExpressionList ^ isListOperator) {
            throw new Exception("Operator '" + operator.getText() + "' is not valid here");
        }

        lexer.read();

        return operator;
    }

    private String expectString() throws Exception {
        if(lexer.getToken() == STRING) {
            String string = lexer.getText();
            lexer.read();
            return string;
        }

        throw new Exception("Expected string, but found " + lexer.getToken());
    }

    private boolean match(Token token) throws Exception {
        if(lexer.getToken() == token) {
            lexer.read();
            return true;
        }

        return false;
    }
}
