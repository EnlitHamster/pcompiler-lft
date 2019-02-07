package automata;

public class ModThreeAutomaton {

  public static boolean scan(String word) {
    int state = 0;
    int i = 0;

    while (state >= 0 && i < word.length()) {
      final char symbol = word.charAt(i++);

      switch (state) {
      case 0:
        if (symbol == '1') state = 1;
        else if (symbol != '0') state = -1;
      break;

      case 1:
        if (symbol == '0') state = 2;
        else if (symbol == '1') state = 0;
        else state = -1;
      break;

      case 2:
        if (symbol == '0') state = 1;
        else if (symbol != '1') state = -1;
      break;
      }
    }

    return state == 0;
  }

  public static void main(String[] args) {
    if (args.length > 0 && args.length % 2 == 0)
      for (int i = 0; i < args.length; ++i)
        System.out.println("Automa returned as expected @ '" + args[i] + "': "
				 								+ (scan(args[i]) == Boolean.parseBoolean(args[++i]) ? '\u2713' : '\u2717'));
    else System.out.println("Usage: java ModThreeAutomaton <strings to check..>");
  }

}
