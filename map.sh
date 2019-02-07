#!/bin/sh

function map_dir {
	dir="-1"
	if [ "$#" -eq 1 ]; then
		case "$1" in
			"All")
				dir=""
				;;
			"Automata" | "AThreeAutomaton" | "BinaryAutomaton" | "CommentsAutomaton" | "IdAutomaton" | "ModThreeAutomaton" | "NameAutomaton" | "NumAutomaton" | "StudentAutomaton")
				dir="automata"
				;;
			"Tag" | "Token" | "Word" | "NumberTok" | "Lexer")
				dir="lexing"
				;;
			"ArithmeticParser" | "Parser")
				dir="parsing"
				;;
			"ArithmeticEvaluator")
				dir="evaluation"
				;;
			"SymbolTable" | "OpCode" | "Instruction" | "CodeGenerator" | "Translator")
				dir="translation"
				;;
			"Error")
				dir="errors"
				;;
			*)
				;;
		esac
	fi
}

function map_make {
	mkcall="-1"
	if [ "$#" -eq 2 ]; then
		case "$1" in
			"All")
				mkcall=""
				;;
			"AThreeAutomaton" | "BinaryAutomaton" | "CommentsAutomaton" | "IdAutomaton" | "ModThreeAutomaton" | "NameAutomaton" | "NumAutomaton" | "StudentAutomaton" | "Tag" | "Token" | "Word" | "NumberTok" | "SymbolTable" | "OpCode" | "Instruction" | "CodeGenerator" | "Error")
				mkcall="bin/$2/$1.class"
				;;
			"Automata")
				mkcall="automata"
				;;
			"Lexer")
				mkcall="lexer"
				;;
			"ArithmeticParser")
				mkcall="apars"
				;;
			"Parser")
				mkcall="pars"
				;;
			"ArithmeticEvaluator")
				mkcall="aeval"
				;;
			"Translator")
				mkcall="trans"
				;;
			*)
				;;
		esac
	fi
}

function map_exec {
	exec="-1"
	if [ "$#" -eq 2 ]; then
		case "$1" in
			"All" | "Automata")
				exec=""
				;;
			"AThreeAutomaton" | "BinaryAutomaton" | "CommentsAutomaton" | "IdAutomaton" | "ModThreeAutomaton" | "NameAutomaton" | "NumAutomaton" | "StudentAutomaton" | "Lexer" | "ArithmeticParser" | "Parser" | "ArithmeticEvaluator" | "Translator")
				exec="java $2.$1"
				;;
			*)
				;;
		esac
	fi
}

function map_clr {
	clr="-1"
	if [ "$#" -eq 2 ]; then
		case "$1" in
			"All")
				clr="make clear"
				;;
			"Automata")
				clr="make clear-$2/"
				;;
			"AThreeAutomaton" | "BinaryAutomaton" | "CommentsAutomaton" | "IdAutomaton" | "ModThreeAutomaton" | "NameAutomaton" | "NumAutomaton" | "StudentAutomaton" | "Tag" | "SymbolTable" | "OpCode" | "Error")
				clr="rm -f bin/$2/$1.class"
				;;
			"Lexer")
				clr="rm -f bin/automata/NumAutomaton.class & rm -f bin/automata/IdAutomaton.class & make clear-$2/"
				;;
			"ArithmeticParser" | "Parser" | "ArithmeticEvaluator")
				clr="rm -f bin/automata/NumAutomaton.class & rm -f bin/automata/IdAutomaton.class & make clear-lexing/ & rm -f bin/$2/$1.class & rm -f bin/errors/Error.class"
				;;
			"Token")
				clr="rm -f bin/$2/Tag.class & rm -f bin/$2/$1.class"
				;;
			"Word" | "NumberTok")
				clr="rm -f bin/$2/Tag.class & rm -f bin/$2/Token.class & rm -f bin/$2/$1.class"
				;;
			"Translator")
				clr="rm -f bin/automata/NumAutomaton.class & rm -f bin/automata/IdAutomaton.class & make clear-lexing/ & make clear-$2/ & rm -f bin/errors/Error.class"
				;;
			"Instruction")
				clr="rm -f bin/$2/OpCode.class & rm -f bin/$2/$1.class"
				;;
			"CodeGenerator")
				clr="rm -f bin/$2/OpCode.class & rm -f bin/$2/Instruction.class & rm -f bin/$2/$1.class"
				;;
			*)
				;;
		esac
	fi
}
