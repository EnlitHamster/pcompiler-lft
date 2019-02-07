package evaluation;

import java.io.*;
import lexing.Tag;
import lexing.Token;
import lexing.NumberTok;
import lexing.Lexer;
import errors.Error;

public class ArithmeticEvaluator {

	private Lexer lex;
	private BufferedReader pbr;
	private Token look;

	public ArithmeticEvaluator(Lexer l, BufferedReader br) {
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

	public int start() throws Error {
		int result = expr();
		match(Tag.EOF);
		return result;
	}

	private int expr() throws Error {
		switch (look.tag) {
			case '(':
			case Tag.NUM:
				return exprp(term());

			default:
				error("Expression not recognised");
		}

		return 0;
	}

	private int exprp(int exprp_i) throws Error {
		switch (look.tag) {
			case '+':
				match('+');
				return exprp(exprp_i + term());

			case '-':
				match('-');
				return exprp(exprp_i - term());

			case ')':
			case Tag.EOF:
				return exprp_i;

			default:
				error("Expression' not recognised");
		}

		return 0;
	}

	private int term() throws Error {
		switch (look.tag) {
			case '(':
			case Tag.NUM:
				return termp(fact());

			default:
				error("Term not recognised");
		}

		return 0;
	}

	private int termp(int termp_i) throws Error {
		switch (look.tag) {
			case '*':
				match('*');
				return termp(termp_i * fact());

			case '/':
				match('/');
				return termp(termp_i / fact());

			case ')':
			case '+':
			case '-':
			case Tag.EOF:
				return termp_i;

			default:
				error("Term' not recognised");
		}

		return 0;
	}

	private int fact() throws Error {
		int fact_val = 0;

		switch (look.tag) {
		case '(':
			match('(');
			fact_val = expr();
			match(')');
		break;

		case Tag.NUM:
			fact_val = ((NumberTok) look).intval;
			match(Tag.NUM);
		break;

		default:
			error("Factor not recognised");
		}

		return fact_val;
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: java evaluation.ArithmeticEvaluator <path-to-source>.");
			return;
		}

		Lexer lex = new Lexer();
		String path = ".." + File.separator + "tests" + File.separator + args[0];

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			ArithmeticEvaluator eval = new ArithmeticEvaluator(lex, br);

			System.out.println("Input OK. Result = " + eval.start());
			br.close();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(Error err) {
			err.printStackTrace();
		}
	}

}
