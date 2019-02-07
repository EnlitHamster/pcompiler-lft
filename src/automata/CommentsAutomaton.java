package automata;

public class CommentsAutomaton {

  public static boolean scan(String word, boolean exclusive) {
    int state = 0;
    int i = 0;

    while (state >= 0 && i < word.length()) {
      final char symbol = word.charAt(i++);

      switch (state) {
      case 0:
        if (symbol == '/') state = 1;
        else if (exclusive || (!exclusive && symbol != 'a' && symbol != '*')) state = -1;
      break;

      case 1:
        if (symbol == '*') state = 2;
        else if (!exclusive && symbol == 'a') state = 0;
        else if (exclusive || (!exclusive && symbol != '/')) state = -1;
      break;

      case 2:
        if (symbol == '*') state = 3;
        else if (symbol == '/' || symbol == 'a') state = 4;
        else state = -1;
      break;

      case 3:
        if (symbol == 'a') state = 4;
        else if (symbol == '/') state = (exclusive ? 5 : 0);
        else if (symbol != '*') state = -1;
      break;

      case 4:
        if (symbol == '*') state = 3;
        else if (symbol != '/' && symbol != 'a') state = -1;
      break;

      case 5:
        state = -1;
      break;
      }
    }

    return (exclusive ? state == 5 : state == 0 || state == 1);
  }

  public static void main(String[] args) {
    if (args.length > 1 && args.length % 2 == 1) {
      boolean exclusive = Boolean.parseBoolean(args[0]);
			System.out.println("Only one comment " + (exclusive ? "enabled" : "not enabled") + ".\n");

      for (int i = 1; i < args.length; ++i)
        System.out.println("Automa returned as expected @ '" + args[i] + "': "
												+ (scan(args[i], exclusive) == Boolean.parseBoolean(args[++i]) ? '\u2713' : '\u2717'));
    } else System.out.println("Usage: java CommentsAutomaton <strings to check..>");
  }

}
