package automata;

public class IdAutomaton {

  public static boolean scan(String word) {
    int state = 0;
    int i = 0;

    while (state >= 0 && i < word.length()) {
      final char symbol = word.charAt(i++);

      switch( state) {
      case 0:
        if (Character.isLetter(symbol)) state = 2;
        else if (symbol == '_') state = 1;
        else state = -1;
      break;

      case 1:
        if (Character.isLetter(symbol) || Character.isDigit(symbol)) state = 2;
        else if (symbol != '_') state = -1;
      break;

      case 2:
        if (!Character.isLetter(symbol) &&
            !Character.isDigit(symbol) &&
            symbol != '_') state = -1;
      break;
      }
    }

    return state == 2;
  }

  public static void main(String[] args) {
    if (args.length > 0 && args.length % 2 == 0)
      for (int i = 0; i < args.length; ++i)
        System.out.println("Automa returned as expected @ '" + args[i] + "': "
				 								+ (scan(args[i]) == Boolean.parseBoolean(args[++i]) ? '\u2713' : '\u2717'));
    else System.out.println("Usage: java IdAutomaton <strings to check..>");
  }

}
