# Projects
The compiler is divided in 6 different projects:
- Automata
- Lexer
- ArithmeticParser
- Parser
- ArithmeticEvaluator
- Translator

## Automata
This project contains a collection of automata, some useful to the Compiler, some not. These are mere exercises.

## Lexer
The lexer is an important part of the compiler, used by the Parsers, Evaluator and Translator to check if the input code respects the CFG of P.

## ArithmeticParser
The arithmetic parser controls that the syntax of the input code is an acceptable arithmetic expression.

## Parser
The parser controls that the syntax of the input code is an acceptable program written in P.

## ArithmeticEvaluator
The arithmetic evaluator calculates the actual value of the input code, given it being an acceptable arithmetic expression.

## Translator
The translator transforms the input code in actual Java ByteCode generating a .j file that **JASMIN** could easily compile in a .class file.

# Exercises

## 1 &#9210; Automata
- 1: `Binary` automaton
- 2: `Id` automaton
- 3, 4, 5: `Student` automaton
- 6: `ModThree` automaton
- 7, 8: `AThree` automaton
- 9: `Name` automaton
- 10, 11: `Comments` automaton

## 2 &#9210; Lexing
See Projects &#8658; Lexer

## 3 &#9210; Parsing
- 1: `Arithmetic` parser
- 2: `Parser`

## 4 &#9210; Evaluation
See Projects &#8658; ArithmeticEvaluator

## 5 &#9210; Translation
See Projects &#8658; Translator

# Usage
To compile a single exercise, use `./es.sh <paragraph> [exercise] [force build]`  
To compile a project use `./exec.sh build <project> [force build]`
To compile a single file (with due dependencies) use `./exec.sh build <file name> [force build]`  
To test a project use `./exec.sh test <project>`  
To compile the entire project use `make`  
To clear the executables use `make clear`  

# Context-Free Grammar of P

```
P    →    SL $

SL   →    S SL'

SL'  →    ; S SL'
     |    ε

S    →    id := E
     |    print ( E )
     |    read ( id )
     |    case WL else S
     |    while ( BO ) S
     |    { SL }
     
WL   →    W WL'

WL'  →    W WL'
     |    ε

W    →    when ( BO ) S

BO   →    BA BO'

BO'  →    or BA BO'
     |    ε

BA   →    B BA'

BA'  →    and B BA'
     |    ε

B    →    E RELOP E
     |    ! ( E RELOP E )
     
E    →    T E'

E'   →    + T E'
     |    - T E'
     |    ε
     
T    →    F T'

T'   →    * F T'
     |    / F T'
     |    ε

F    →    ( E )
     |    num
     |    id
```
