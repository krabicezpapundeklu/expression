import { ChangeEvent, useEffect, useState } from 'react'
import './App.css'

enum AnswerType {
  BOOLEAN = 'BOOLEAN', NUMBER = 'NUMBER', TEXT = 'TEXT'
}

interface Expression {
  question: string;
  operator: Operator,
  value: string
}

interface ExpressionList {
  operator: Operator;
  expressions: (Expression | ExpressionList)[];
}

enum Operator {
  AND = '&&',
  OR = '||',
  EQUAL = '==',
  NOT_EQUAL = '!=',
  LESS_THAN = '<',
  LESS_THAN_OR_EQUAL = '<=',
  GREATER_THAN = '>',
  GREATER_THAN_OR_EQUAL = '>=',
  CONTAINS = 'contains',
  IN = 'in'
}

interface Question {
  name: string;
  answerType: AnswerType;
}

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

const buildExpression = (expressionList: ExpressionList) => {
  let exp = '(';

  for(let i = 0; i < expressionList.expressions.length; ++i) {
    if(i > 0) {
      exp += ' ' + expressionList.operator + ' ';
    }

    const expression = expressionList.expressions[i];

    if('question' in expression) {
      exp += `${expression.question} ${expression.operator} "${expression.value.replaceAll('"', '\\"')}"`;
    } else {
      exp += buildExpression(expression);
    }
  }

  exp += ')';
  
  return exp;
}

const cleanExpressionList = (expressionList: ExpressionList) => {
  let newExpressionList: ExpressionList = {operator: expressionList.operator, expressions: []};

  for(const expression of expressionList.expressions) {
    if('question' in expression) {
      if(expression.question !== '') {
        newExpressionList.expressions.push(expression);
      }
    } else {
      const list = cleanExpressionList(expression);

      if(list.expressions.length > 0) {
        newExpressionList.expressions.push(list);
      }
    }
  }

  return newExpressionList;
}

const mapOperator = (operator: string): Operator => {
  switch(operator) {
    case '&&': return Operator.AND;
    case '||': return Operator.OR;
    case '==': return Operator.EQUAL;
    case '!=': return Operator.NOT_EQUAL;
    case '<': return Operator.LESS_THAN;
    case '<=': return Operator.LESS_THAN_OR_EQUAL;
    case '>': return Operator.GREATER_THAN;
    case '>=': return Operator.GREATER_THAN_OR_EQUAL;
    case 'contains': return Operator.CONTAINS;
    case 'in': return Operator.IN;
    default: throw Error('Wrong operator');
  }
}

export const App = () => {
  const [questions, setQuestions] = useState<Question[]>([]);
  const [expressionList, setExpressionList] = useState<ExpressionList>({operator: Operator.AND, expressions: []});

  useEffect(() => {
    fetch('/questions').then(data => data.json()).then(questions => setQuestions(questions));
    fetch('/expression').then(data => data.json()).then(expressionList => setExpressionList(expressionList));
  }, []);

  const changeExpressionList = (expressionList: ExpressionList) => {
    setExpressionList(expressionList);
  }

  const cleanEl = cleanExpressionList(expressionList);
  
  let expression = buildExpression(cleanEl);
  expression = expression.substring(1, expression.length - 1);

  return (
    <>
      <h1>Eligibility Demo</h1>
      <h2>Expression Builder</h2>
      <ExpressionListBuilder questions={questions} expressionList={expressionList} onChange={changeExpressionList} />
      <h2>Expression Tree</h2>
      <pre>
        {JSON.stringify(cleanEl, null, 2)}
      </pre>
      <h2>Expression</h2>
      <pre>
        {expression}
      </pre>
    </>
  )
}

interface ExpressionBuilderProps {
  questions: Question[];
  expression: Expression;
  onChange: (expression: Expression) => void;
}

const ExpressionBuilder = (props: ExpressionBuilderProps) => {
  const {questions, expression, onChange} = props;

  const changeQuestion = (event: ChangeEvent<HTMLSelectElement>) => {
    onChange({question: event.target.value, operator: Operator.EQUAL, value: ''})
  }

  const changeOperator = (event: ChangeEvent<HTMLSelectElement>) => {
    onChange({...expression, operator: mapOperator(event.target.value)})
  }

  const changeRadio = (event: ChangeEvent<HTMLInputElement>) => {
    onChange({...expression, value: event.target.checked.toString()})
  }

  const changeValue = (event: ChangeEvent<HTMLInputElement>) => {
    onChange({...expression, value: event.target.value.trim()})
  }

  const renderExpression = (expression: Expression) => {
    const question = questions.find(question => question.name === expression.question);

    if(question === undefined) {
      return;
    }

    if(question.answerType === AnswerType.BOOLEAN) {
      return (
        <input checked={expression.value === 'true'} onChange={changeRadio} type="checkbox" />
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
        <input onChange={changeValue} value={expression.value} />
      </>
    )
  }

  return (
    <>
      <select onChange={changeQuestion} value={expression.question}>
        <option value="">-- Select Question --</option>
        {questions.map((question: Question) =>
          <option key={question.name} value={question.name}>{question.name}</option>
        )}
      </select>
      {renderExpression(expression)}
    </>
  )
}

interface ExpressionListBuilderProps {
  questions: Question[];
  expressionList: ExpressionList;
  onChange: (expressionList: ExpressionList) => void;
}

const ExpressionListBuilder = (props: ExpressionListBuilderProps) => {
  const {questions, expressionList, onChange} = props;

  const addExpression = () => {
    expressionList.expressions.push({question: '', operator: Operator.EQUAL, value: ''});
    onChange({...expressionList});
  }

  const addExpressionList = () => {
    expressionList.expressions.push({operator: Operator.AND, expressions: []});
    onChange({...expressionList});
  }

  const changeOperator = (event: ChangeEvent<HTMLInputElement>) => {
    expressionList.operator = mapOperator(event.target.value);
    onChange({...expressionList});
  }

  const changeExpression = (expression: Expression | ExpressionList, index: number) => {
    expressionList.expressions[index] = expression;
    onChange({...expressionList});
  }

  const removeExpression = (index: number) => {
    expressionList.expressions.splice(index, 1);
    onChange({...expressionList});
  }

  return (
    <div className="expression-list">
      <ol>
        <label><input type="radio" checked={expressionList.operator === Operator.AND} value="&amp;&amp;" onChange={changeOperator} />AND</label>
        <label><input type="radio" checked={expressionList.operator === Operator.OR} value="||" onChange={changeOperator} />OR</label>
        {expressionList.expressions.map((expression: Expression | ExpressionList, index: number) =>
          <li key={index}>
            {'question' in expression
              ? <ExpressionBuilder questions={questions} expression={expression} onChange={(expression: Expression) => changeExpression(expression, index)} />
              : <ExpressionListBuilder questions={questions} expressionList={expression} onChange={(expressionList: ExpressionList) => changeExpression(expressionList, index)} />
            }
            
            <button onClick={() => removeExpression(index)}>Remove</button>
          </li>
        )}
      </ol>
      <button onClick={addExpression}>Add Expression</button>
      <button onClick={addExpressionList}>Add Expression List</button>
    </div>
  )
}
