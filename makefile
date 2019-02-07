B=bin/
S=src/
A=automata/
L=lexing/
P=parsing/
E=errors/
V=evaluation/
T=translation/
SD=cd ${S};
BD=-d ../${B}

C=.class
J=.java

CC=${SD} javac ${BD}

# BUILD TARGETS
all: automata lexer apars pars aeval trans
automata: ${B}${A}AThreeAutomaton${C} ${B}${A}BinaryAutomaton${C} ${B}${A}CommentsAutomaton${C} ${B}${A}IdAutomaton${C} ${B}${A}ModThreeAutomaton${C} ${B}${A}NameAutomaton${C} ${B}${A}NumAutomaton${C} ${B}${A}StudentAutomaton${C}
lexer: ${B}${A}NumAutomaton${C} ${B}${A}IdAutomaton${C} ${B}${L}NumberTok${C} ${B}${L}Word${C} ${B}${L}Token${C} ${B}${L}Tag${C} ${B}${L}Lexer${C}
apars: ${B}${E}Error${C} lexer ${B}${P}ArithmeticParser${C}
pars: ${B}${E}Error${C} lexer ${B}${P}Parser${C}
aeval: ${B}${E}Error${C} lexer ${B}${V}ArithmeticEvaluator${C}
trans: ${B}${E}Error${C} lexer ${B}${T}OpCode${C} ${B}${T}Instruction${C} ${B}${T}CodeGenerator${C} ${B}${T}SymbolTable${C} ${B}${T}Translator${C}

# AUTOMATA
${B}${A}AThreeAutomaton${C}:
	${CC} ${A}AThreeAutomaton${J}
${B}${A}BinaryAutomaton${C}:
	${CC} ${A}BinaryAutomaton${J}
${B}${A}CommentsAutomaton${C}:
	${CC} ${A}CommentsAutomaton${J}
${B}${A}IdAutomaton${C}:
	${CC} ${A}IdAutomaton${J}
${B}${A}ModThreeAutomaton${C}:
	${CC} ${A}ModThreeAutomaton${J}
${B}${A}NameAutomaton${C}:
	${CC} ${A}NameAutomaton${J}
${B}${A}NumAutomaton${C}:
	${CC} ${A}NumAutomaton${J}
${B}${A}StudentAutomaton${C}:
	${CC} ${A}StudentAutomaton${J}

# LEXING
${B}${L}Lexer${C}:
	${CC} ${L}Lexer${J}
${B}${L}NumberTok${C}:
	${CC} ${L}NumberTok${J}
${B}${L}Word${C}:
	${CC} ${L}Word${J}
${B}${L}Token${C}:
	${CC} ${L}Token${J}
${B}${L}Tag${C}:
	${CC} ${L}Tag${J}

# PARSING
${B}${P}ArithmeticParser${C}:
	${CC} ${P}ArithmeticParser${J}
${B}${P}Parser${C}:
	${CC} ${P}Parser${J}

# EVALUATION
${B}${V}ArithmeticEvaluator${C}:
	${CC} ${V}ArithmeticEvaluator${J}

# TRANSLATION
${B}${T}Translator${C}:
	${CC} ${T}Translator${J}
${B}${T}CodeGenerator${C}:
	${CC} ${T}CodeGenerator${J}
${B}${T}Instruction${C}:
	${CC} ${T}Instruction${J}
${B}${T}OpCode${C}:
	${CC} ${T}OpCode${J}
${B}${T}SymbolTable${C}:
	${CC} ${T}SymbolTable${J}

# ERRORS
${B}${E}Error${C}:
	${CC} ${E}Error${J}

clear: clear-${A} clear-${L} clear-${P} clear-${E} clear-${V} clear-${T}

clear-${A}:
	rm -f ${B}${A}*${C}
clear-${L}:
	rm -f ${B}${L}*${C}
clear-${P}:
	rm -f ${B}${P}*${C}
clear-${E}:
	rm -f ${B}${E}*${C}
clear-${V}:
	rm -f ${B}${V}*${C}
clear-${T}:
	rm -f ${B}${T}*${C}
