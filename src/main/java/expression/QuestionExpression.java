package expression;

import static expression.Operator.IN;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;

public class QuestionExpression implements Expression {
    private final String question;
    private final Operator operator;
    private final String answer;

    @JsonCreator
    public QuestionExpression(String question, Operator operator, String answer) {
        this.question = question;
        this.operator = operator;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public Operator getOperator() {
        return operator;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public boolean evaluate(Map<String, String> context, List<Question> questions) throws Exception {
        AnswerType answerType = questions.stream().filter(q -> q.getName().equals(question)).findFirst().get().getAnswerType();
        Object q = map(answerType, context.getOrDefault(question, ""));

        if(q == null) {
            return false;
        }

        if(operator == IN) {
            String[] values = answer.split("[ ,;]");
            return Arrays.stream(values).anyMatch(v -> !v.isEmpty() && q.equals(map(answerType, v)));
        }

        Object a = map(answerType, answer);

        switch(operator) {
            case EQUAL:
                return q.equals(a);

            case NOT_EQUAL:
                return !q.equals(a);

            case LESS_THAN:
                return ((Comparable)q).compareTo(a) < 0;

            case LESS_THAN_OR_EQUAL:
                return ((Comparable)q).compareTo(a) <= 0;

            case GREATER_THAN:
                return ((Comparable)q).compareTo(a) > 0;

            case GREATER_THAN_OR_EQUAL:
                return ((Comparable)q).compareTo(a) >= 0;

            case CONTAINS:
                return ((String)q).contains((String)a);

            default:
                throw new Exception("Invalid operator " + operator);
        }
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }

        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        QuestionExpression that = (QuestionExpression) o;
        return Objects.equals(question, that.question) && operator == that.operator && Objects.equals(answer, that.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, operator, answer);
    }

    @Override
    public String toString() {
        return question + ' ' + operator.getText() + " \"" + answer.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"") + '"';
    }

    private static Object map(AnswerType answerType, String value) {
        value = value == null ? null : value.trim();

        try {
            switch (answerType) {
                case BOOLEAN:
                    return "true".equals(value);

                case NUMBER:
                    return Long.parseLong(value);

                default:
                    return value;
            }
        } catch(Exception e) {
            return null;
        }
    }
}
