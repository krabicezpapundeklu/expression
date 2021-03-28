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
    fetch('/expression').then(data => data.json()).then(expression => setExpression(mapExpression(expression)));
  }, []);

  const changeExpression = (expression: Expression) => {
    setExpression(expression);
  }

  const cleanExpression = expression.clean();

  let expressionString = '';

  if(cleanExpression !== undefined) {
    expressionString = cleanExpression.toString();

    if(expressionString[0] === '(') {
      expressionString = expressionString.substring(1, expressionString.length - 1);
    }
  }

  return (
    <>
      <h1>Eligibility Demo</h1>
      <h2>Expression Builder</h2>
      {expression.render(questions, changeExpression)}
      <h2>Expression</h2>
      <pre>
        {expressionString}
      </pre>
      <h2>Evaluation</h2>
      <Eval questions={questions} expression={cleanExpression} />
    </>
  )
}
