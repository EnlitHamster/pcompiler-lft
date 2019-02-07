package automata;

public class AThreeAutomaton {

  public static boolean scan(String word, boolean ending) {
    int state = 0;
    int i = 0;

    while (state >= 0 && i < word.length()) {
      final char symbol = word.charAt(i++);

      switch (state) {
      case 0:
        if (!ending) {
          if (symbol == 'a') state = 3;
          else if (symbol == 'b') state = 1;
          else state = -1;
        } else {
          if (symbol == 'a') state = 1;
          else if (symbol != 'b') state = -1;
        }
      break;

      case 1:
      case 2:
        if (!ending) {
          if (symbol == 'a') state = 3;
          else if (symbol == 'b') state = (state == 2 ? -1 : 2);
          else state = -1;
        } else {
          if (symbol == 'a') state = 1;
          else if (symbol == 'b') ++state;
          else state = -1;
        }
      break;

      case 3:
        if (!ending) {
          if (symbol != 'a' && symbol != 'b') state = -1;
        } else {
          if (symbol == 'a') state = 1;
          else if (symbol == 'b') state = 0;
          else state = -1;
        }
      break;
      }
    }

    return (!ending ? state == 3 : state >= 1);
  }

  public static void main(String[] args) {
    if (args.length > 1 && args.length % 2 == 1) {
      boolean ending = Boolean.parseBoolean(args[0]);
			System.out.println("'a' positioning @ " + (ending ? "ending" : "beginning") + ".\n");

      for (int i = 1; i < args.length; ++i)
        System.out.println("Automa returned as expected @ '" + args[i] + "': "
												+ (scan(args[i], ending) == Boolean.parseBoolean(args[++i]) ? '\u2713' : '\u2717'));
    } else System.out.println("Usage: java AThreeAutomaton <a ending> <strings to check..>");
  }

}
