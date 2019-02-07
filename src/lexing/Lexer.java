package lexing;

import java.io.*;
import java.util.*;
import automata.IdAutomaton;
import automata.NumAutomaton;

public class Lexer {

	public static int line = 1;
	private char peek = ' ';

	public char lastch() {
		return peek;
	}

	private void readch(BufferedReader br) {
		try {
			peek = (char) br.read();
		} catch (IOException exc) {
			peek = (char) -1;	// ERROR
		}
	}

	public Token lexical_scan(BufferedReader br) {
		while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
			if (peek == '\n') ++line;
			readch(br);
		}

		switch (peek) {
			case (char) -1:
				return new Token(Tag.EOF);

			case '!':
				peek = ' ';
				return Token.not;
			case '(':
				peek = ' ';
				return Token.lpt;
			case ')':
				peek = ' ';
				return Token.rpt;
			case '{':
				peek = ' ';
				return Token.lpg;
			case '}':
				peek = ' ';
				return Token.rpg;
			case '+':
				peek = ' ';
				return Token.plus;
			case '-':
				peek = ' ';
				return Token.minus;
			case '*':
				peek = ' ';
				return Token.mult;
			case ';':
				peek = ' ';
				return Token.semicolon;

			case '/':
				readch(br);
				if (peek == '/') {
					while (peek != '\n' && peek != (char) -1) readch(br);

					if (peek == '\n') {
						++line;
						peek = ' ';
						return lexical_scan(br);
					} else return new Token(Tag.EOF);
				} else if (peek == '*') {
					boolean ended = false;
					while (peek != (char) -1 && !ended) {
						readch(br);
						if (peek == '*') {
							readch(br);
							ended = peek == '/';
						} else if (peek == '\n') ++line;
					}

					peek = ' ';
					if (!ended) {
						System.err.println("Opened multi-line comment never closed.");
						return null;
					} else return lexical_scan(br);
				} else return Token.div;

			case '&':
				readch(br);
				if (peek == '&') {
					peek = ' ';
					return Word.and;
				} else {
					System.err.println("Erroneous character after &: " + peek);
					return null;
				}

			case '|':
				readch(br);
				if (peek == '|') {
					peek = ' ';
					return Word.or;
				} else {
					System.err.println("Erroneous character after |: " + peek);
					return null;
				}

			case '<':
				readch(br);
				if (peek == '=') {
					peek = ' ';
					return Word.le;
				} else if (peek == '>') {
					peek = ' ';
					return Word.ne;
				} else return Word.lt;

			case '=':
				readch(br);
				if (peek == '=') {
					peek = ' ';
					return Word.eq;
				} else {
					System.err.println("Erroneous character after =: " + peek);
					return null;
				}

			case '>':
				readch(br);
				if (peek == '=') {
					peek = ' ';
					return Word.ge;
				} else return Word.gt;

			case ':':
				readch(br);
				if (peek == '=') {
					peek = ' ';
					return Word.assign;
				} else {
					System.err.println("Erroneous character after :: " + peek);
				}

			default:
				if (Character.isLetter(peek)) {
					String lexval = "";
					while (!(peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r' ||
									 peek == '>' || peek == '<' || peek == '=' || peek == ':' ||
									 peek == '(' || peek == ')' || peek == '{' || peek == '}' ||
									 peek == '!' || peek == '&' || peek == '|' || peek == '+' ||
									 peek == '-' || peek == '*' || peek == '/' || peek == ';')) {
						lexval += peek;
						readch(br);
					}

					if (lexval.equals(Word.casetok.lexeme)) return Word.casetok;
					else if (lexval.equals(Word.when.lexeme)) return Word.when;
					else if (lexval.equals(Word.then.lexeme)) return Word.then;
					else if (lexval.equals(Word.elsetok.lexeme)) return Word.elsetok;
					else if (lexval.equals(Word.whiletok.lexeme)) return Word.whiletok;
					else if (lexval.equals(Word.dotok.lexeme)) return Word.dotok;
					else if (lexval.equals(Word.print.lexeme)) return Word.print;
					else if (lexval.equals(Word.read.lexeme)) return Word.read;
					else if (IdAutomaton.scan(lexval)) return new Word(Tag.ID, lexval);
					else {
						System.err.println("Unrecognised lexeme: " + lexval);
						return null;
					}
				} else if (Character.isDigit(peek)) {
					String lexval = "";
					while (!(peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r' ||
									 peek == '>' || peek == '<' || peek == '=' || peek == ':' ||
									 peek == '(' || peek == ')' || peek == '{' || peek == '}' ||
									 peek == '!' || peek == '&' || peek == '|' || peek == '+' ||
									 peek == '-' || peek == '*' || peek == '/' || peek == ';')) {
						lexval += peek;
						readch(br);
					}

					if (NumAutomaton.scan(lexval)) return new NumberTok(Integer.parseInt(lexval));
					else {
						System.err.println("Unrecognised lexeme: " + lexval);
						return null;
					}
				} else {
					System.err.println("Erroneous character: " + peek);
					return null;
				}
		}
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: java lexing.Lexer <path-to-source>.");
			return;
		}

		Lexer lex = new Lexer();
		String path = ".." + File.separator + "tests" + File.separator + args[0];
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			Token tok;
			do {
				tok = lex.lexical_scan(br);
				System.out.println("Scan: " + tok);
			} while (tok.tag != Tag.EOF);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("\nFile length: " + lex.line);
	}

}
