import './App.css'
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
  const expressionString = cleanExpression?.toString();

  return (
    <>
      <h1>Eligibility Demo</h1>
      <h2>Expression Builder</h2>
      {expression.render(questions, changeExpression)}
      <h2>Expression Tree</h2>
      <pre>
        {JSON.stringify(cleanExpression, null, 2)}
      </pre>
      <h2>Expression</h2>
      <pre>
        {expressionString}
      </pre>
      <h2>Evaluation</h2>
      <Eval expression={cleanExpression} />
    </>
  )
}

interface EvalProps {
  expression: Expression | undefined;
}

const Eval = (props: EvalProps) => {
  const { expression } = props;
  const [context, setContext] = useState({});
  const [result, setResult] = useState('');

  const evaluate = () => {
    fetch('/evaluate', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ expression, context })
    })
      .then(data => {
        if(!data.ok) {
          throw new Error(data.statusText);
        }

        return data.json();
      })
      .then(data => setResult(`Result: ${data}`))
      .catch(error => setResult(error.toString()));
  }

  return (
    <>
      <button disabled={expression === undefined} onClick={evaluate}>Evaluate</button>
      <span>{result}</span>
    </>
  )
}
