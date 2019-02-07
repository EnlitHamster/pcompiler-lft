package parsing;

import java.io.*;
import lexing.Tag;
import lexing.Token;
import lexing.Lexer;
import errors.Error;

public class Parser {

	private Lexer lex;
	private BufferedReader pbr;
	private Token look;

	public Parser(Lexer l, BufferedReader br) {
		lex = l;
		pbr = br;
		move();
	}

	void move() {
		look = lex.lexical_scan(pbr);
		System.out.println("token = " + look);
	}

	void error(String s) throws Error {
		throw new Error("near line " + lex.line + ": " + s);
	}

	void match(int t) throws Error {
		if (look.tag == t) {
			if (look.tag != Tag.EOF) move();
		} else error("syntax error");
	}

	//-------------------------------
	// NON-TERMINALS IMPLEMENTATIONS
	//-------------------------------

	public void prog() throws Error {
		switch (look.tag) {
		case Tag.ID:
		case Tag.PRINT:
		case Tag.READ:
		case Tag.CASE:
		case Tag.WHILE:
		case '{':
			statlist();
			match(Tag.EOF);
		break;

		default:
			error("prog not recognised @ " + look);
		}
	}

	private void statlist() throws Error {
		switch (look.tag) {
		case Tag.ID:
		case Tag.PRINT:
		case Tag.READ:
		case Tag.CASE:
		case Tag.WHILE:
		case '{':
			stat();
			statlistp();
		break;

		default:
			error("statlist not recognised @ " + look);
		}
	}

	private void statlistp() throws Error {
		switch (look.tag) {
		case ';':
			match(';');
			stat();
			statlistp();
		break;

		case '}':
		case Tag.EOF:
		break;

		default:
			error("statlist' not recognised @ " + look);
		}
	}

	private void stat() throws Error {
		switch (look.tag) {
		case Tag.ID:
			match(Tag.ID);
			match(Tag.ASSIGN);
			expr();
		break;

		case Tag.PRINT:
			match(Tag.PRINT);
			match('(');
			expr();
			match(')');
		break;

		case Tag.READ:
			match(Tag.READ);
			match('(');
			match(Tag.ID);
			match(')');
		break;

		case Tag.CASE:
			match(Tag.CASE);
			whenlist();
			match(Tag.ELSE);
			stat();
		break;

		case Tag.WHILE:
			match(Tag.WHILE);
			match('(');
			bexpr();
			match(')');
			stat();
		break;

		case '{':
			match('{');
			statlist();
			match('}');
		break;

		default:
			error("stat not recognised @ " + look);
		}
	}

	private void whenlist() throws Error {
		switch (look.tag) {
		case Tag.WHEN:
			whenitem();
			whenlistp();
		break;

		default:
			error("whenlist not recognised @ " + look);
		}
	}

	private void whenlistp() throws Error {
		switch (look.tag) {
		case Tag.WHEN:
			whenitem();
			whenlistp();
		break;

		case Tag.ELSE:
		break;

		default:
			error("whenlist' not recognised @ " + look);
		}
	}

	private void whenitem() throws Error {
		switch (look.tag) {
		case Tag.WHEN:
			match(Tag.WHEN);
			match('(');
			bexpr();
			match(')');
			stat();
		break;

		default:
			error("whenitem not recognised @ " + look);
		}
	}

	private void bexpr() throws Error {
		switch (look.tag) {
		case '(':
		case Tag.NUM:
		case Tag.ID:
			expr();
			match(Tag.RELOP);
			expr();
		break;

		default:
			error("bexpr not recognised @ " + look);
		}
	}

	private void expr() throws Error {
		switch (look.tag) {
		case '(':
		case Tag.NUM:
		case Tag.ID:
			term();
			exprp();
		break;

		default:
			error("expr not recognised @ " + look);
		}
	}

	private void exprp() throws Error {
		switch (look.tag) {
		case '+':
			match('+');
			term();
			exprp();
		break;

		case '-':
			match('-');
			term();
			exprp();
		break;

		case ')':
		case ';':
		case '}':
		case Tag.WHEN:
		case Tag.ELSE:
		case Tag.RELOP:
		case Tag.EOF:
		break;

		default:
			error("expr' not recognised @ " + look);
		}
	}

	private void term() throws Error {
		switch (look.tag) {
		case '(':
		case Tag.NUM:
		case Tag.ID:
			fact();
			termp();
		break;

		default:
			error("term not recognised @ " + look);
		}
	}

	private void termp() throws Error {
		switch (look.tag) {
		case '*':
			match('*');
			fact();
			termp();
		break;

		case '/':
			match('/');
			fact();
			termp();
		break;

		case '+':
		case '-':
		case ')':
		case ';':
		case '}':
		case Tag.WHEN:
		case Tag.ELSE:
		case Tag.RELOP:
		case Tag.EOF:
		break;

		default:
			error("Term' not recognised");
		}
	}

	private void fact() throws Error {
		switch (look.tag) {
		case '(':
			match('(');
			expr();
			match(')');
		break;

		case Tag.NUM:
			match(Tag.NUM);
		break;

		case Tag.ID:
			match(Tag.ID);
		break;

		default:
			error("fact not recognised @ " + look);
		}
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: java parsing.ArithmeticParser <path-to-source>.");
			return;
		}

		Lexer lex = new Lexer();
		String path = ".." + File.separator + "tests" + File.separator + args[0];

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			Parser parser = new Parser(lex, br);
			parser.prog();
			System.out.println("Input OK");
			br.close();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(Error err) {
			err.printStackTrace();
		}
	}

}
