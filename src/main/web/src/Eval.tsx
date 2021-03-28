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

  const evaluate = (useTree: boolean) => {
    fetch('/evaluate', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        useTree,
        expression,
        expressionString: expression?.toString(0),
        context})
    })
      .then(data => {
        if (!data.ok) {
          throw new Error(data.statusText);
        }

        return data.json();
      })
      .then(data => alert(`Result: ${data}`))
      .catch(error => alert(error.toString()));
  }

  return (
    <div className="border">
      <table className="center left mb">
        <thead>
          <th>Answer</th>
          <th>Answer Type</th>
          <th>Value</th>
        </thead>
        <tbody>
          {questions.map(question =>
            <tr key={question.name}>
              <td>{question.name}</td>
              <td>{question.answerType}</td>
              <td>
                {question.answerType === AnswerType.BOOLEAN
                  ? <input onChange={event => changeRadio(question.name, event)} type="checkbox" checked={context[question.name] === 'true'} />
                  : <input onBlur={event => trimValue(question.name, event)} onChange={event => changeValue(question.name, event)} value={context[question.name] || ''} />
                }
              </td>
            </tr>
          )}
        </tbody>
      </table>
      <div className="center">
        <button disabled={expression === undefined} onClick={() => evaluate(true)}>Evaluate Tree</button>
        <button disabled={expression === undefined} onClick={() => evaluate(false)}>Evaluate String</button>
      </div>
    </div>
  )
}
