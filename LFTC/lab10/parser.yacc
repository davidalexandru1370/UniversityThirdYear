%{
#include <stdio.h>
#include <stdlib.h>

int yyerror(char *s);

int yydebug = 1;
%}
%error-verbose
%token PROGRAM;
%token MYBEGIN;
%token INT;
%token STRING;
%token DISPLAY;
%token READINT;
%token IF;
%token ELSE;
%token WHILE;
%token RETURN;
%token END;

%token IDENTIFIER;
%token INTCONSTANT;
%token STRINGCONSTANT;
%token CHAR;

%token PLUS;
%token MINUS;
%token TIMES;
%token DIV;
%token MOD;
%token EQ;
%token BIGGER;
%token BIGGEREQ;
%token LESS;
%token LESSEQ;
%token EQQ;
%token NEQ;

%token SEMICOLON;
%token OPEN;
%token CLOSE;
%token BRACKETOPEN;
%token BRACKETCLOSE;
%token COMMA;

%start Start 

%%
Start: PROGRAM BRACKETOPEN MYBEGIN CompoundStatement END BRACKETCLOSE {printf("Program ->  program { begin { CompoundStatement } end } \n"); };

CompoundStatement : Statement SEMICOLON CompoundStatement {printf("CompoundStatement -> Statement ; CompoundStatement\n"); }
		  | Statement SEMICOLON {printf("CompoundStatement -> Statement ;\n");}
		  ;

Statement : DeclarationStatement {printf("Statement -> DeclarationStatement \n");}
	|   AssignmentStatement {printf("Statement -> AssignmentStatement \n");}
	|   IfStatement {printf("Statement -> IfStatement \n");}
	|   WhileStatement {printf("Statement -> WhileStatement \n");}
	|   DisplayStatement {printf("Statement -> PrintStatement \n");}
	|   ReadStatement {printf("Statement -> ReadStatement\n");}
	;

DeclarationStatement: Type IDENTIFIER {printf("DeclarationStatement -> TYPE  IDENTIFIER\n");} 
		      | Type IDENTIFIER EQ Expression {printf("DeclarationStatement -> Type IDENTIFIER = Expression");}
			;

AssignmentStatement : IDENTIFIER EQ Expression {printf("AssignmentStatement -> IDENTIFIER = Expression\n");};

Term : Term TIMES Factor {printf("Term -> Term * Factor\n");}
       | Term DIV Factor {printf("Term / Factor\n");}
       | Factor {printf("Term -> Factor\n");}
       ;

Expression :
	|    Expression PLUS Expression {printf("Expression -> Expression + Expression");}
	|    Term PLUS Term {printf("Expression -> Term + Term\n");}
	|    Expression PLUS Term {printf("Expression -> Expression + Term\n");}
	|    Expression MINUS Term {printf("Expression -> Expression - Term\n");}
	|    Term {printf("Expression -> Term\n");}
	;

Factor : OPEN Expression CLOSE {printf("Factor -> ( Expression )\n");}
	| IDENTIFIER {printf("Factor -> IDENTIFIER\n");}
	| INTCONSTANT {printf("Factor -> INTCONSTANT\n");}
	| MINUS IDENTIFIER {printf("Factor -> - IDENTIFIER\n");}
	;

Type :    INT {printf("Type -> int\n");}
	| STRING {printf("Type -> string\n");}
	| CHAR {printf("Type -> char\n");}
      ;

IfStatement : IF OPEN  Condition CLOSE  BRACKETOPEN CompoundStatement BRACKETCLOSE {printf("IfStatement -> if (Expression) {CompoundStatement}\n");}
	| IF OPEN Condition CLOSE BRACKETOPEN CompoundStatement BRACKETCLOSE ELSE BRACKETOPEN CompoundStatement BRACKETCLOSE {printf("IfStatement -> if(Expression) {CompoundStatement} else {CompoundStatement}");}
	;

WhileStatement : WHILE OPEN  Condition CLOSE  BRACKETOPEN CompoundStatement BRACKETCLOSE {printf("WhileStatement -> while (Expression) {CompoundStatement}\n");};

DisplayStatement : DISPLAY OPEN Expression CLOSE {printf("DisplayStatement -> display(Expression)");}
		| DISPLAY OPEN STRINGCONSTANT CLOSE {printf("PrintStatement -> display(STRINGCONSTANT)");}
		;

ReadStatement: READINT OPEN IDENTIFIER CLOSE {printf("ReadStatement -> readInt(IDENTIFIER)");}; 

Condition: Expression Relation Expression {printf("Condition -> Expression Relation Expression\n");};

Relation: LESS {printf("Relation -> Less\n");}
	| LESSEQ {printf("Relation -> LESS OR EQUAL\n");}
	| EQQ {printf("Relation -> EQUAL\n");}
	| NEQ {printf("Relation -> NOT EQUAL\n");}
	| BIGGEREQ {printf("Relation -> BIGGER OR EQUAL\n");}
	| BIGGER {printf("Relation -> BIGGER\n");}
	;

%%
yyerror(char *s)
{	
	printf("%s\n",s);
}

extern FILE *yyin;

main(int argc, char **argv)
{
	if(argc>1) yyin =  fopen(argv[1],"r");
	if(!yyparse()) fprintf(stderr, "\tOK\n");
} 

