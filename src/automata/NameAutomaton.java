package automata;

public class NameAutomaton {

  public static boolean scan(String word) {
    int state = 0;
    int i = 0;

    String name = "Sandro";

    while (state >= 0 && i < word.length()) {
      final char symbol = word.charAt(i++);

      switch (state) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
        if (symbol == name.charAt(state)) ++state;
        else state += (state == 5 ? 1 : 7);
      break;

      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
        if (symbol == name.charAt(state - 6)) state += (state == 11 ? -5 : 1);
        else state = -1;
      break;

      case 6:
        state = -1;
      break;
      }
    }

    return state == 6;
  }

  public static void main(String[] args) {
    if (args.length > 0 && args.length % 2 == 0)
      for (int i = 0; i < args.length; ++i)
        System.out.println("Automa returned as expected @ '" + args[i] + "': "
				 								+ (scan(args[i]) == Boolean.parseBoolean(args[++i]) ? '\u2713' : '\u2717'));
    else System.out.println("Usage: java NameAutomaton <strings to check..>");
  }

}
