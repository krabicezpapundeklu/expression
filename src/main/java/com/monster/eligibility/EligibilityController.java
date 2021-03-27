package com.monster.eligibility;

import static com.monster.eligibility.AnswerType.*;
import static com.monster.eligibility.Operator.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EligibilityController {
    @GetMapping("/expression")
    public ExpressionList getExpression() {
        return new ExpressionList(
            AND,
            new Expression("US_CITIZEN", EQUAL, "true"),
            new ExpressionList(
                OR,
                new Expression("AGE", GREATER_THAN_OR_EQUAL, "21"),
                new Expression("NAME", CONTAINS, "old")
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
