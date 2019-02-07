package lexing;

public class NumberTok extends Token {

	public int intval;

	public NumberTok(int val) {
		super(Tag.NUM);
		intval = val;
	}

	public String toString() {
		return "<" + tag + ", " + intval + ">";
	}

}
