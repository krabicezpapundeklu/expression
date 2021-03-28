import './App.css'
import { Eval } from './Eval';
import { Expression, mapExpression } from './Expression';
import { ExpressionList } from './ExpressionList';
import { Operator } from './Operator';
import { Question } from './Question';
import { useEffect, useState } from 'react'

export const App = () => {
  const [questions, setQuestions] = useState<Question[]>([]);
  const [expression, setExpression] = useState<Expression>(new ExpressionList(Operator.AND, []));

  useEffect(() => {
    fetch('/questions').then(data => data.json()).then(questions => setQuestions(questions));
    fetch('/sample-expression').then(data => data.json()).then(expression => setExpression(mapExpression(expression)));
  }, []);

  const changeExpression = (expression: Expression) => {
    setExpression(expression);
  }

  const cleanExpression = expression.clean();

  let expressionString = '';

  if(cleanExpression !== undefined) {
    expressionString = cleanExpression.toString(0);
  }

  return (
    <>
      {expression.render(questions, changeExpression)}
      <div className="border center mono">{expressionString}</div>
      <Eval questions={questions} expression={cleanExpression} />
    </>
  )
}
