package expression;

import static expression.AnswerType.*;
import static expression.Operator.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @PostMapping("/evaluate")
    public boolean evaluate(@RequestBody EvaluateDTO evaluateDTO) throws Exception {
        return evaluateDTO.getExpression().evaluate(evaluateDTO.getContext(), getQuestions());
    }

    @GetMapping("/expression")
    public ExpressionList getExpression() {
        return new ExpressionList(
            AND,
            new QuestionExpression("US_CITIZEN", EQUAL, "true"),
            new ExpressionList(
                OR,
                new QuestionExpression("AGE", GREATER_THAN_OR_EQUAL, "21"),
                new QuestionExpression("NAME", CONTAINS, "old")
            )
        );
    }

    @GetMapping("/questions")
    public List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();

        questions.add(new Question("AGE", NUMBER));
        questions.add(new Question("NAME", TEXT));
        questions.add(new Question("US_CITIZEN", BOOLEAN));

        return questions;
    }
}
