import { ChangeEvent } from "react";
import { Expression } from "./Expression";
import { Operator, mapOperator } from "./Operator";
import { Question } from "./Question";
import { QuestionExpression } from "./QuestionExpression";

export class ExpressionList implements Expression {
  type = '.ExpressionList';

  constructor(public operator: Operator, public expressions: Expression[]) {
  }

  clean() {
    let cleanExpressionList = new ExpressionList(this.operator, []);

    for (const expression of this.expressions) {
      const cleanExpression = expression.clean();

      if (cleanExpression !== undefined) {
        cleanExpressionList.expressions.push(cleanExpression);
      }
    }

    if(cleanExpressionList.expressions.length === 0) {
      return undefined;
    }

    return cleanExpressionList;
  }

  render(questions: Question[], onChange: (expression: Expression) => void, onRemove?: () => void) {
    const addQuestionExpression = () => {
      this.expressions.push(new QuestionExpression('', Operator.EQUAL, ''));
      onChange(new ExpressionList(this.operator, this.expressions));
    }

    const addExpressionList = () => {
      this.expressions.push(new ExpressionList(Operator.AND, []));
      onChange(new ExpressionList(this.operator, this.expressions));
    }

    const changeOperator = (event: ChangeEvent<HTMLInputElement>) => {
      this.operator = mapOperator(event.target.value);
      onChange(new ExpressionList(this.operator, this.expressions));
    }

    const changeExpression = (expression: Expression, index: number) => {
      this.expressions[index] = expression;
      onChange(new ExpressionList(this.operator, this.expressions));
    }

    const removeExpression = (index: number) => {
      this.expressions.splice(index, 1);
      onChange(new ExpressionList(this.operator, this.expressions));
    }

    return (
      <div className="border">
        <div>
          <label><input type="radio" checked={this.operator === Operator.AND} value="&amp;&amp;" onChange={changeOperator} />AND</label>
          <label><input type="radio" checked={this.operator === Operator.OR} value="||" onChange={changeOperator} />OR</label>
          {onRemove ? <button className="fr" onClick={onRemove}>Remove</button> : ''}
        </div>
        <div className="border">
          {this.expressions.map((expression: Expression, index: number) =>
            <div key={index}>
              {expression.render(questions, expression => changeExpression(expression, index), () => removeExpression(index))}
            </div>
          )}
          <div>
            <button onClick={addQuestionExpression}>Add Question Expression</button>
            <button onClick={addExpressionList}>Add Expression List</button>
          </div>
        </div>
      </div>
    )
  }

  toString(level: number) {
    if(this.expressions.length === 0) {
      return '';
    }

    let s = level > 0 ? '(' : '';

    for (let i = 0; i < this.expressions.length; ++i) {
      if (i > 0) {
        s += ` ${this.operator} `;
      }

      s += this.expressions[i].toString(level + 1);
    }

    s += level > 0 ? ')' : '';

    return s;
  }
}
