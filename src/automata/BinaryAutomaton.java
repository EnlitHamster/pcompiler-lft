package automata;

public class BinaryAutomaton {

	public static boolean scan(String word, boolean complementary) {
		int state = 0;
		int i = 0;

		while (state >= 0 && i < word.length()) {
			final char symbol = word.charAt(i++);

			switch (state) {
			case 0:
				if (symbol == '0') state = 1;
				else if (symbol != '1') state = -1;
			break;

			case 1:
				if (symbol == '0') state = 2;
				else if (symbol == '1') state = 0;
				else state = -1;
			break;

			case 2:
				if (symbol == '0') state = 3;
				else if (symbol == '1') state = 0;
				else state = -1;
			break;

			case 3:
				if (symbol != '0' && symbol != '1') state = -1;
			break;
			}
		}

		return (!complementary) ? (state == 3) : (state >= 0 && state <= 2);
	}

	public static void main(String[] args) {
		if (args.length > 1 && args.length % 2 == 1) {
			boolean complementarity = Boolean.parseBoolean(args[0]);
			System.out.println("Complementarity " + (complementarity ? "enabled" : "not enabled") + ".\n");

			for (int i = 1; i < args.length; ++i)
				System.out.println("Automa returned as expected @ '" + args[i] + "': "
				 								+ (scan(args[i], complementarity) == Boolean.parseBoolean(args[++i]) ? '\u2713' : '\u2717'));
		} else System.out.println("Usage: java BinaryAutomaton <complementarity> <strings to check + expected result..>");
	}

}
