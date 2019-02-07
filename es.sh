#!/bin/sh

if [[ "$#" -ge 2 && "$#" -le 3 && "$1" -eq "1" ]]; then
  p=""
  case "$2" in
  "1")
    p="Binary"
    ;;
  "2")
    p="Id"
    ;;
  "3" | "4" | "5")
    p="Student"
    ;;
  "6")
    p="ModThree"
    ;;
  "7" | "8")
    p="AThree"
    ;;
  "9")
    p="Name"
    ;;
  "10" | "11")
    p="Comments"
    ;;
  *)
    echo "Wrong arguments."
    echo "Usage: ./es.sh 1 <exercise number> [f = rebuild]"
		exit
    ;;
  esac
  ./exec.sh build ${p}Automaton $3
  ./exec.sh test ${p}Automaton
elif [[ "$#" -ge 1 && "$#" -le 2 && "$1" -eq "2" ]]; then
	./exec.sh build Lexer $2
	./exec.sh test Lexer
elif [[ "$#" -ge 2 && "$#" -le 3 && "$1" -eq "3" ]]; then
	p=""
	case "$2" in
	"1")
		p="Arithmetic"
		;;
	"2")
		;;
	*)
    echo "Wrong arguments."
    echo "Usage: ./es.sh 3 <exercise number> [f = rebuild]"
		exit
    ;;
  esac
	./exec.sh build ${p}Parser $3
	./exec.sh test ${p}Parser
elif [[ "$#" -ge 1 && "$#" -le 2 && "$1" -eq "4" ]]; then
	./exec.sh build ArithmeticEvaluator $2
	./exec.sh test ArithmeticEvaluator
elif [[ "$#" -ge 1 && "$#" -le 2 && "$1" -eq "5" ]]; then
	./exec.sh build Translator $2
	./exec.sh test Translator
else
	echo "Wrong arguments."
	echo "Usage: ./es.sh <paragraph> <exercise number> [f = rebuild]"
fi
