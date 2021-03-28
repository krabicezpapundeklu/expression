import { AnswerType } from "./AnswerType";
import { ChangeEvent, useState } from "react";
import { Expression } from "./Expression";
import { Question } from "./Question";

export interface EvalProps {
  questions: Question[];
  expression: Expression | undefined;
}

export const Eval = (props: EvalProps) => {
  const { questions, expression } = props;
  const [context, setContext] = useState<any>({});
  const [result, setResult] = useState('');

  const changeRadio = (name: string, event: ChangeEvent<HTMLInputElement>) => {
    context[name] = event.target.checked.toString();
    setContext({...context});
  }

  const changeValue = (name: string, event: ChangeEvent<HTMLInputElement>) => {
    context[name] = event.target.value;
    setContext({...context});
  }

  const trimValue = (name: string, event: ChangeEvent<HTMLInputElement>) => {
    context[name] = event.target.value.trim();
    setContext({...context});
  }

  const evaluate = () => {
    fetch('/evaluate', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ expression, context })
    })
      .then(data => {
        if (!data.ok) {
          throw new Error(data.statusText);
        }

        return data.json();
      })
      .then(data => setResult(`Result: ${data}`))
      .catch(error => setResult(error.toString()));
  }

  return (
    <>
      <ol>
        {questions.map(question =>
          <li key={question.name}>
            <label>
              {question.name}
              {question.answerType === AnswerType.BOOLEAN
                ? <input onChange={event => changeRadio(question.name, event)} type="checkbox" checked={context[question.name] === 'true'} />
                : <input onBlur={event => trimValue(question.name, event)} onChange={event => changeValue(question.name, event)} value={context[question.name] || ''} />
              }
            </label>
          </li>
        )}
      </ol>
      <button disabled={expression === undefined} onClick={evaluate}>Evaluate</button>
      <span>{result}</span>
    </>
  )
}
