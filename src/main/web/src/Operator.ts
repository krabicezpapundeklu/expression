export enum Operator {
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

export const mapOperator = (operator: string): Operator => {
  switch (operator) {
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
