package translation;

import java.io.*;
import lexing.Tag;
import lexing.Token;
import lexing.Word;
import lexing.NumberTok;
import lexing.Lexer;
import errors.Error;

public class Translator {

	private Lexer lex;
	private BufferedReader pbr;
	private Token look;

	SymbolTable st = new SymbolTable();
	CodeGenerator code = new CodeGenerator();
	int count = 0;

	public Translator(Lexer l, BufferedReader br) {
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
			int lnext_prog = code.newLabel();
			statlist(lnext_prog);
			code.emitLabel(lnext_prog);
			match(Tag.EOF);
			try {
				code.toJasmin();
			} catch (IOException e) {
				e.printStackTrace();
			}
		break;

		default:
			error("error in grammar, prog not recognised @ " + look);
		}
	}

	public void statlist(int lnext) throws Error {
		switch (look.tag) {
		case Tag.ID:
		case Tag.PRINT:
		case Tag.READ:
		case Tag.CASE:
		case Tag.WHILE:
		case '{':
			int lnext_stat = code.newLabel();
			stat(lnext_stat);
			code.emitLabel(lnext_stat);
			statlistp(lnext);
		break;

		default:
			error("error in grammar, statlist not recognised @ " + look);
		}
	}

	private void statlistp(int lnext) throws Error {
		switch (look.tag) {
		case ';':
			match(';');
			int lnext_stat = code.newLabel();
			stat(lnext_stat);
			code.emitLabel(lnext_stat);
			statlistp(lnext);
		break;

		case '}':
		case Tag.EOF:
		break;

		default:
			error("error in grammar, statlist' not recognised @ " + look);
		}
	}

	private void stat(int lnext) throws Error {
		switch (look.tag) {
		case Tag.ID:
			int id_addr = st.lookupAddress(((Word) look).lexeme);
			if (id_addr == -1) {
				id_addr = count;
				st.insert(((Word) look).lexeme, count++);
			}
			match(Tag.ID);
			match(Tag.ASSIGN);
			expr();
			code.emit(OpCode.istore, id_addr);
		break;

		case Tag.PRINT:
			match(Tag.PRINT);
			match('(');
			expr();
			code.emit(OpCode.invokestatic, 1);
			match(')');
		break;

		case Tag.READ:
			match(Tag.READ);
			match('(');
			if (look.tag == Tag.ID) {
				int read_id_addr = st.lookupAddress(((Word) look).lexeme);
				if (read_id_addr == -1) {
					read_id_addr = count;
					st.insert(((Word) look).lexeme, count++);
				}
				match(Tag.ID);
				match(')');
				code.emit(OpCode.invokestatic, 0);
				code.emit(OpCode.istore, read_id_addr);
			} else error("error in grammar, read not recognised @ " + look);
		break;

		case Tag.CASE:
			match(Tag.CASE);
			whenlist(lnext);
			match(Tag.ELSE);
			stat(lnext);
		break;

		case Tag.WHILE:
			match(Tag.WHILE);
			int ltrue = code.newLabel();
			int lwhile_start = code.newLabel();
			code.emitLabel(lwhile_start);

			match('(');
			bexpr(ltrue, lnext);
			match(')');

			code.emitLabel(ltrue);
			stat(lwhile_start);
			code.emit(OpCode.GOto, lwhile_start);
		break;

		case '{':
			match('{');
			statlist(lnext);
			match('}');
		break;

		default:
			error("error in grammar, stat not recognised @ " + look);
		}
	}

	private void whenlist(int lnext) throws Error {
		switch (look.tag) {
		case Tag.WHEN:
			int lnext_when = code.newLabel();
			whenitem(lnext, lnext_when);
			code.emitLabel(lnext_when);
			whenlistp(lnext);
		break;

		default:
			error("error in grammar, whenlist not recognised @ " + look);
		}
	}

	private void whenlistp(int lnext) throws Error {
		switch (look.tag) {
		case Tag.WHEN:
			int lnext_when = code.newLabel();
			whenitem(lnext, lnext_when);
			code.emitLabel(lnext_when);
			whenlistp(lnext);
		break;

		case Tag.ELSE:
		break;

		default:
			error("error in grammar, whenlist' not recognised @ " + look);
		}
	}

	private void whenitem(int lnext, int lnext_when) throws Error {
		switch (look.tag) {
		case Tag.WHEN:
			match(Tag.WHEN);
			int ltrue = code.newLabel();

			match('(');
			bexpr(ltrue, lnext_when);
			match(')');

			code.emitLabel(ltrue);
			stat(lnext);
			code.emit(OpCode.GOto, lnext);
		break;

		default:
			error("error in grammar, whenitem not recognised @ " + look);
		}
	}

	private void bexpr (int ltrue, int lfalse) throws Error {
		switch (look.tag) {
		case '(':
		case '!':
		case Tag.NUM:
		case Tag.ID:
			int lnext_or = code.newLabel();
			bexprs(lnext_or);
			code.emit(OpCode.GOto, ltrue);
			code.emitLabel(lnext_or);
			bexprp(ltrue);
			code.emit(OpCode.GOto, lfalse);
		break;
		}
	}

	private void bexprp(int ltrue) throws Error {
		switch (look.tag) {
		case Tag.OR:
			match(Tag.OR);
			int lnext_or = code.newLabel();
			bexprs(lnext_or);
			code.emit(OpCode.GOto, ltrue);
			code.emitLabel(lnext_or);
			bexprp(ltrue);
		break;

		case ')':
		break;

		default:
			error("error in grammar, bexpr' not recognised @ " + look);
		}
	}

	private void bexprs(int lfalse) throws Error {
		switch (look.tag) {
		case '(':
		case '!':
		case Tag.NUM:
		case Tag.ID:
			bexprf(lfalse);
			bexprt(lfalse);
		break;

		default:
			error("error in grammar, bexpr'' not recognised @ " + look);
		}
	}

	private void bexprt(int lfalse) throws Error {
		switch (look.tag) {
		case Tag.AND:
			match(Tag.AND);
			bexprf(lfalse);
			bexprt(lfalse);
		break;

		case ')':
		case Tag.OR:
		break;

		default:
			error("error in grammar, bexpr''' not recognised @ " + look);
		}
	}

	private void bexprf(int lfalse) throws Error {
		Token tmp;
		OpCode op = null;

		switch (look.tag) {
		case '(':
		case Tag.NUM:
		case Tag.ID:
			expr();
			tmp = look;
			match(Tag.RELOP);
			expr();

			if (tmp == Word.lt) op = OpCode.if_icmpge;
			else if (tmp == Word.gt) op = OpCode.if_icmple;
			else if (tmp == Word.eq) op = OpCode.if_icmpne;
			else if (tmp == Word.le) op = OpCode.if_icmpgt;
			else if (tmp == Word.ne) op = OpCode.if_icmpeq;
			else if (tmp == Word.ge) op = OpCode.if_icmplt;
			else error("error in grammar, RELOP not recognised @ " + look);

			code.emit(op, lfalse);
		break;

		case '!':
			match('!');
			match('(');
			expr();
			tmp = look;
			match(Tag.RELOP);
			expr();
			match(')');

			if (tmp == Word.lt) op = OpCode.if_icmplt;
			else if (tmp == Word.gt) op = OpCode.if_icmpgt;
			else if (tmp == Word.eq) op = OpCode.if_icmpeq;
			else if (tmp == Word.le) op = OpCode.if_icmple;
			else if (tmp == Word.ne) op = OpCode.if_icmpne;
			else if (tmp == Word.ge) op = OpCode.if_icmpge;
			else error("error in grammar, RELOP not recognised @ " + look);

			code.emit(op, lfalse);
		}
	}

	/*private void bexpr(int ltrue, int lfalse) throws Error {
		switch (look.tag) {
		case '(':
		case Tag.NUM:
		case Tag.ID:
			expr();
			Token tmp = look;
			OpCode op = null;
			match(Tag.RELOP);
			expr();

			if (tmp == Word.lt) op = OpCode.if_icmpge;
			else if (tmp == Word.gt) op = OpCode.if_icmple;
			else if (tmp == Word.eq) op = OpCode.if_icmpne;
			else if (tmp == Word.le) op = OpCode.if_icmpgt;
			else if (tmp == Word.ne) op = OpCode.if_icmpeq;
			else if (tmp == Word.ge) op = OpCode.if_icmplt;
			else error("error in grammar, RELOP not recognised @ " + look);

			code.emit(op, lfalse);
		break;

		default:
			error("error in grammar, bexpr not recognised @ " + look);
		}
	}*/

	private void expr() throws Error {
		switch (look.tag) {
		case '(':
		case Tag.NUM:
		case Tag.ID:
			term();
			exprp();
		break;

		default:
			error("error in grammar, expr not recognised @ " + look);
		}
	}

	private void exprp() throws Error {
		switch (look.tag) {
		case '+':
			match('+');
			term();
			code.emit(OpCode.iadd);
			exprp();
		break;

		case '-':
			match('-');
			term();
			code.emit(OpCode.isub);
			exprp();
		break;

		case ')':
		case ';':
		case '}':
		case Tag.AND:
		case Tag.OR:
		case Tag.WHEN:
		case Tag.ELSE:
		case Tag.RELOP:
		case Tag.EOF:
		break;

		default:
			error("error in grammar, expr' not recognised @ " + look);
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
			error("error in grammar, term not recognised @ " + look);
		}
	}

	private void termp() throws Error {
		switch (look.tag) {
		case '*':
			match('*');
			fact();
			code.emit(OpCode.imul);
			termp();
		break;

		case '/':
			match('/');
			fact();
			code.emit(OpCode.idiv);
			termp();
		break;

		case '+':
		case '-':
		case ')':
		case ';':
		case '}':
		case Tag.AND:
		case Tag.OR:
		case Tag.WHEN:
		case Tag.ELSE:
		case Tag.RELOP:
		case Tag.EOF:
		break;

		default:
			error("error in grammar, term' not recognised @ " + look);
		}
	}

	private void fact() throws Error {
		switch(look.tag) {
			case '(':
				match('(');
				expr();
				match(')');
			break;

			case Tag.NUM:
				code.emit(OpCode.ldc, ((NumberTok) look).intval);
				match(Tag.NUM);
			break;

			case Tag.ID:
				int id_addr = st.lookupAddress(((Word) look).lexeme);
				if (id_addr == -1) {
					error("error in grammar, variable not initialized");
					/*
					id_addr = count;
					st.insert(((Word) look).lexeme, count++);
					*/
				}

				code.emit(OpCode.iload, id_addr);
				match(Tag.ID);
			break;

			default:
				error("error in grammar, fact not recognised @ " + look);
		}
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: java translation.Translator <path-to-source>.");
			return;
		}

		Lexer lex = new Lexer();
		String path = ".." + File.separator + "tests" + File.separator + args[0];

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			Translator tran = new Translator(lex, br);
			tran.prog();
			System.out.println("Input OK.");
			br.close();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(Error err) {
			err.printStackTrace();
		}
	}

}
