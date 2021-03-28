package expression;

public class Question {
    private final String name;
    private final AnswerType answerType;

    public Question(String name, AnswerType answerType) {
        this.name = name;
        this.answerType = answerType;
    }

    public String getName() {
        return name;
    }

    public AnswerType getAnswerType() {
        return answerType;
    }
}
