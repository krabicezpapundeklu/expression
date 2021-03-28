package expression;

import static expression.AnswerType.*;

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
        Expression expression;

        if(evaluateDTO.isUseTree()) {
            expression = evaluateDTO.getExpression();
        } else {
            expression = Parser.parse(evaluateDTO.getExpressionString());
        }

        return expression.evaluate(evaluateDTO.getContext(), getQuestions());
    }

    @GetMapping("/questions")
    public List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();

        questions.add(new Question("AGE", NUMBER));
        questions.add(new Question("NAME", TEXT));
        questions.add(new Question("US_CITIZEN", BOOLEAN));

        return questions;
    }

    @GetMapping("/sample-expression")
    public Expression getSampleExpression() throws Exception {
        return Parser.parse("US_CITIZEN == \"true\" && (AGE >= \"21\" || NAME contains \"old\")");
    }
}
