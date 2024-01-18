/* A Bison parser, made by GNU Bison 3.8.2.  */

/* Bison interface for Yacc-like parsers in C

   Copyright (C) 1984, 1989-1990, 2000-2015, 2018-2021 Free Software Foundation,
   Inc.

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <https://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* DO NOT RELY ON FEATURES THAT ARE NOT DOCUMENTED in the manual,
   especially those whose name start with YY_ or yy_.  They are
   private implementation details that can be changed or removed.  */

#ifndef YY_YY_Y_TAB_H_INCLUDED
# define YY_YY_Y_TAB_H_INCLUDED
/* Debug traces.  */
#ifndef YYDEBUG
# define YYDEBUG 0
#endif
#if YYDEBUG
extern int yydebug;
#endif

/* Token kinds.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
  enum yytokentype
  {
    YYEMPTY = -2,
    YYEOF = 0,                     /* "end of file"  */
    YYerror = 256,                 /* error  */
    YYUNDEF = 257,                 /* "invalid token"  */
    PROGRAM = 258,                 /* PROGRAM  */
    MYBEGIN = 259,                 /* MYBEGIN  */
    INT = 260,                     /* INT  */
    STRING = 261,                  /* STRING  */
    DISPLAY = 262,                 /* DISPLAY  */
    READINT = 263,                 /* READINT  */
    IF = 264,                      /* IF  */
    ELSE = 265,                    /* ELSE  */
    WHILE = 266,                   /* WHILE  */
    RETURN = 267,                  /* RETURN  */
    END = 268,                     /* END  */
    IDENTIFIER = 269,              /* IDENTIFIER  */
    INTCONSTANT = 270,             /* INTCONSTANT  */
    STRINGCONSTANT = 271,          /* STRINGCONSTANT  */
    CHAR = 272,                    /* CHAR  */
    PLUS = 273,                    /* PLUS  */
    MINUS = 274,                   /* MINUS  */
    TIMES = 275,                   /* TIMES  */
    DIV = 276,                     /* DIV  */
    MOD = 277,                     /* MOD  */
    EQ = 278,                      /* EQ  */
    BIGGER = 279,                  /* BIGGER  */
    BIGGEREQ = 280,                /* BIGGEREQ  */
    LESS = 281,                    /* LESS  */
    LESSEQ = 282,                  /* LESSEQ  */
    EQQ = 283,                     /* EQQ  */
    NEQ = 284,                     /* NEQ  */
    SEMICOLON = 285,               /* SEMICOLON  */
    OPEN = 286,                    /* OPEN  */
    CLOSE = 287,                   /* CLOSE  */
    BRACKETOPEN = 288,             /* BRACKETOPEN  */
    BRACKETCLOSE = 289,            /* BRACKETCLOSE  */
    COMMA = 290                    /* COMMA  */
  };
  typedef enum yytokentype yytoken_kind_t;
#endif
/* Token kinds.  */
#define YYEMPTY -2
#define YYEOF 0
#define YYerror 256
#define YYUNDEF 257
#define PROGRAM 258
#define MYBEGIN 259
#define INT 260
#define STRING 261
#define DISPLAY 262
#define READINT 263
#define IF 264
#define ELSE 265
#define WHILE 266
#define RETURN 267
#define END 268
#define IDENTIFIER 269
#define INTCONSTANT 270
#define STRINGCONSTANT 271
#define CHAR 272
#define PLUS 273
#define MINUS 274
#define TIMES 275
#define DIV 276
#define MOD 277
#define EQ 278
#define BIGGER 279
#define BIGGEREQ 280
#define LESS 281
#define LESSEQ 282
#define EQQ 283
#define NEQ 284
#define SEMICOLON 285
#define OPEN 286
#define CLOSE 287
#define BRACKETOPEN 288
#define BRACKETCLOSE 289
#define COMMA 290

/* Value type.  */
#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef int YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define YYSTYPE_IS_DECLARED 1
#endif


extern YYSTYPE yylval;


int yyparse (void);


#endif /* !YY_YY_Y_TAB_H_INCLUDED  */
