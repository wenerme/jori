grammar MiniQuery;

query: expression EOF;

expression
    : '(' expression ')'
    | expression LogicalOperator expression
    | term operator term
    ;

term
    : Integer
    | Identifier
    | Float
    | Null
    | Boolean
    | arrayTerm
    | jsonTerm
    ;

arrayTerm: '(' (term (',' term)*)? ','? ')' ;
jsonTerm: Identifier jsonTermPath+ ;
jsonTermPath: '->>' jsonTermField ;
jsonTermField: (String | Identifier) jsonTermFieldIndex* ;
jsonTermFieldIndex: '[' '*'|Integer ']';

operator: Comparator | 'is' 'not'? | 'not'? 'in' | 'not'? 'like' | JsonComparator;

LogicalOperator: 'and' | 'or';

JsonComparator: '@>';
Comparator: '>=' | '<=' | '>' | '<' | '=' | '<>' | '!=';

Integer: '-'?[0-9]+;
Float: '-'?[0-9]+[.][0-9]+;
Strin: '\'' ~[']* '\'';
Boolean: 'true' | 'false';
Null: 'null';
Identifier: [a-zA-Z][a-zA-Z0-9_]*;

WS: [ \t\u000C\r\n]+ -> skip;
