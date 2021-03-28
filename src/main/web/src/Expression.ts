import { ExpressionList } from './ExpressionList';
import { Question } from './Question'
import { QuestionExpression } from './QuestionExpression'

export interface Expression {
  clean(): Expression | undefined;
  render(questions: Question[], onChange: (expression: Expression) => void, onRemove?: () => void): JSX.Element;
  toString(): string;
}

export const mapExpression = (value: any): Expression => {
  if (value.expressions !== undefined) {
    let expressionList = new ExpressionList(value.operator, []);

    for (let expression of value.expressions) {
      expressionList.expressions.push(mapExpression(expression));
    }

    return expressionList;
  }

  return new QuestionExpression(value.question, value.operator, value.answer);
}
