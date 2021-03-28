package expression;

import static expression.Operator.*;
import static expression.Token.*;

public class Lexer {
    private final String expression;

    private int index;

    private Token token;
    private String text;
    private Operator operator;

    public Token getToken() {
        return token;
    }

    public String getText() {
        return text;
    }

    public Operator getOperator() {
        return operator;
    }

    public Lexer(String expression) {
        this.expression = expression;
    }

    public boolean read() throws Exception {
        token = null;
        text = null;
        operator = null;

        char c = peek();

        while(Character.isWhitespace(c)) {
            ++index;
            c = peek();
        }

        int start = index;

        switch(c) {
            case 0:
                token = EOI;
                return false;

            case '(':
                return read(OPEN_PAREN);

            case ')':
                return read(CLOSE_PAREN);

            case '&':
                return read('&', AND);

            case '|':
                return read('|', OR);

            case '=':
                return read('=', EQUAL);

            case '!':
                return read('=', NOT_EQUAL);

            case '<':
                return read('=', LESS_THAN_OR_EQUAL, LESS_THAN);

            case '>':
                return read('=', GREATER_THAN_OR_EQUAL, GREATER_THAN);

            case '"':
                StringBuilder sb = new StringBuilder();

                for(;;) {
                    c = expression.charAt(++index);

                    switch(c) {
                        case 0:
                            throw new Exception("Unterminated string");

                        case '\\':
                            c = peek();

                            if(c != '\\' && c != '"') {
                                throw new Exception("Invalid escape sequence");
                            }

                            sb.append(expression.charAt(++index));
                            break;

                        case '"':
                            text = sb.toString();
                            return read(STRING);

                        default:
                            sb.append(c);
                            break;
                    }
                }

            default:
                if(Character.isJavaIdentifierStart(c)) {
                    do {
                        ++index;
                        c = peek();
                    } while(c != 0 && Character.isJavaIdentifierPart(c));

                    text = expression.substring(start, index);

                    if(text.equals(CONTAINS.getText())) {
                        token = OPERATOR;
                        operator = CONTAINS;
                    } else if(text.equals(IN.getText())) {
                        token = OPERATOR;
                        operator = IN;
                    } else {
                        token = ID;
                    }

                    return true;
                }

                break;
        }

        throw new Exception("Unexpected token " + c);
    }

    private char peek() {
        return index < expression.length() ? expression.charAt(index) : 0;
    }

    private boolean read(Token t) {
        ++index;
        token = t;
        return true;
    }

    private boolean read(char c, Operator o) throws Exception {
        token = OPERATOR;
        operator = o;

        ++index;

        if(peek() != c) {
            throw new Exception("Expected '" + c + "'");
        }

        ++index;

        return true;
    }

    private boolean read(char c, Operator ifMatch, Operator ifNotMatch) {
        token = OPERATOR;

        ++index;

        if(peek() == c) {
            ++index;
            operator = ifMatch;
        } else {
            operator = ifNotMatch;
        }

        return true;
    }
}
