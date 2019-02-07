package parsing;

import java.io.*;
import lexing.Tag;
import lexing.Token;
import lexing.Lexer;
import errors.Error;

public class ArithmeticParser {

	private Lexer lex;
	private BufferedReader pbr;
	private Token look;

	public ArithmeticParser(Lexer l, BufferedReader br) {
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

	public void start() throws Error {
		expr();
		match(Tag.EOF);
	}

	private void expr() throws Error {
		switch (look.tag) {
			case '(':
			case Tag.NUM:
				term();
				exprp();
			break;

			default:
				error("Expression not recognised");
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
			case Tag.EOF:
			break;

			default:
				error("Expression' not recognised");
		}
	}

	private void term() throws Error {
		switch (look.tag) {
			case '(':
			case Tag.NUM:
				fact();
				termp();
			break;

			default:
				error("Term not recognised");
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

			case ')':
			case '+':
			case '-':
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

		default:
			error("Factor not recognised");
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
			ArithmeticParser parser = new ArithmeticParser(lex, br);
			parser.start();
			System.out.println("Input OK");
			br.close();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(Error err) {
			err.printStackTrace();
		}
	}

}
