#!/bin/bash
yacc -d parser.yacc
flex -i scanner.lxi
gcc -o a.exe y.tab.c lex.yy.c
