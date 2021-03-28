import { AnswerType } from "./AnswerType";
import { ChangeEvent } from "react";
import { Expression } from "./Expression";
import { Operator, mapOperator } from "./Operator";
import { Question } from "./Question";

const NUMBER_OPERATORS: Operator[] = [
  Operator.EQUAL,
  Operator.NOT_EQUAL,
  Operator.LESS_THAN,
  Operator.LESS_THAN_OR_EQUAL,
  Operator.GREATER_THAN,
  Operator.GREATER_THAN_OR_EQUAL,
  Operator.IN
]

const TEXT_OPERATORS: Operator[] = [
  Operator.EQUAL,
  Operator.NOT_EQUAL,
  Operator.CONTAINS
]

export class QuestionExpression implements Expression {
  type = '.QuestionExpression';

  constructor(public question: string, public operator: Operator, public answer: string) {
  }

  clean() {
    return this.question === '' ? undefined : this;
  }

  render(questions: Question[], onChange: (expression: Expression) => void, onRemove?: () => void) {
    const changeQuestion = (event: ChangeEvent<HTMLSelectElement>) => {
      onChange(new QuestionExpression(event.target.value, Operator.EQUAL, ''));
    }

    const changeOperator = (event: ChangeEvent<HTMLSelectElement>) => {
      onChange(new QuestionExpression(this.question, mapOperator(event.target.value), this.answer));
    }

    const changeRadio = (event: ChangeEvent<HTMLInputElement>) => {
      onChange(new QuestionExpression(this.question, this.operator, event.target.checked.toString()));
    }

    const changeValue = (event: ChangeEvent<HTMLInputElement>) => {
      onChange(new QuestionExpression(this.question, this.operator, event.target.value));
    }

    const trimValue = (event: ChangeEvent<HTMLInputElement>) => {
      onChange(new QuestionExpression(this.question, this.operator, event.target.value.trim()));
    }

    const renderExpression = (expression: QuestionExpression) => {
      const question = questions.find(question => question.name === expression.question);

      if (question === undefined) {
        return;
      }

      if (question.answerType === AnswerType.BOOLEAN) {
        return (
          <input checked={expression.answer === 'true'} onChange={changeRadio} type="checkbox" />
        )
      }

      const operators = question.answerType === AnswerType.NUMBER ? NUMBER_OPERATORS : TEXT_OPERATORS;

      return (
        <>
          <select value={expression.operator} onChange={changeOperator}>
            {operators.map((operator: Operator) =>
              <option key={operator} value={operator}>{operator}</option>
            )}
          </select>
          <input onBlur={trimValue} onChange={changeValue} value={expression.answer} />
        </>
      )
    }

    return (
      <>
        <select onChange={changeQuestion} value={this.question}>
          <option value="">-- Select Question --</option>
          {questions.map((question: Question) =>
            <option key={question.name} value={question.name}>{question.name}</option>
          )}
        </select>
        {renderExpression(this)}
        {onRemove ? <button onClick={onRemove}>Remove</button> : ''}
      </>
    )
  }

  toString() {
    return `${this.question} ${this.operator} "${this.answer.replaceAll('\\', '\\\\').replaceAll('"', '\\"').trim()}"`;
  }
}
