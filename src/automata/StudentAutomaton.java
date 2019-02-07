package automata;

public class StudentAutomaton {

  public static boolean scan(String word, boolean whitespaces, boolean invert) {
    int state = 0;
    int i = 0;

    while (state >= 0 && i < word.length()) {
      final char symbol = word.charAt(i++);

      switch (state) {
      case 0:
        if (!invert) {
          if (Character.isDigit(symbol)) {
            if (Character.getNumericValue(symbol) % 2 == 0) state = 1;
            else state = 2;
          } else if (!whitespaces || (whitespaces && symbol != ' ')) state = -1;
        } else {
          if (symbol >= 'A' && symbol <= 'K') state = 1;
          else if (symbol >= 'L' && symbol <= 'Z') state = 2;
          else if (!whitespaces || (whitespaces && symbol != ' ')) state = -1;
        }
      break;

      case 1:
      case 2:
        if (!invert) {
          if (Character.isDigit(symbol)) {
            if (Character.getNumericValue(symbol) % 2 == (state == 1 ? 1 : 0))
              state = (state == 1 ? 2 : 1);
          } else if (symbol >= (state == 1 ? 'A' : 'L') &&
                     symbol <= (state == 1 ? 'K' : 'Z')) state = 3;
          else if (whitespaces && symbol == ' ') state = (state == 1 ? 4 : 5);
          else state = -1;
        } else {
          if (whitespaces && symbol == ' ') state = (state == 1 ? 4 : 5);
          else if (Character.isDigit(symbol)) {
            if (Character.getNumericValue(symbol) % 2 == (state == 1 ? 0 : 1))
              state = (state == 1 ? 7 : 8);
            else state = (state == 1 ? 9 : 10);
          } else if (!(symbol >= 'a' && symbol <= 'z') &&
                     !(symbol >= 'A' && symbol <= 'Z')) state = -1;
        }
      break;

      case 3:
        if (whitespaces && symbol == ' ') state = 6;
        else if (!(symbol >= 'a' && symbol <= 'z') &&
                 !(symbol >= 'A' && symbol <= 'Z')) state = -1;
      break;

      case 4:
      case 5:
        if (!invert) {
          if (whitespaces && symbol >= (state == 4 ? 'A' : 'L') &&
              symbol <= (state == 4 ? 'K' : 'Z')) state = 3;
          else if (!whitespaces || (whitespaces && symbol != ' ')) state = -1;
        } else {
          if (Character.isDigit(symbol)) {
            if (Character.getNumericValue(symbol) % 2 == (state == 4 ? 0 : 1))
              state = (state == 4 ? 7 : 8);
            else state = (state == 4 ? 9 : 10);
          } else if (symbol >= 'A' && symbol <= 'Z') state = (state == 4 ? 1 : 2);
          else if (!whitespaces || (whitespaces && symbol != ' ')) state = -1;
        }
      break;

      case 6:
        if (whitespaces && symbol >= 'A' && symbol <= 'Z') state = 3;
        else if (!whitespaces || (whitespaces && symbol != ' ')) state = -1;
      break;

      case 7:
      case 8:
        if (invert && Character.isDigit(symbol)) {
          if (Character.getNumericValue(symbol) % 2 == (state == 7 ? 1 : 0))
            state = (state == 7 ? 9 : 10);
        } else if (whitespaces && symbol == ' ') state = 11;
        else state = -1;
      break;

      case 9:
      case 10:
        if (invert && Character.isDigit(symbol)) {
          if (Character.getNumericValue(symbol) % 2 == (state == 9 ? 0 : 1))
            state = (state == 9 ? 7 : 8);
        } else state = -1;
      break;

      case 11:
        if (!whitespaces || (whitespaces && symbol != ' ')) state = -1;
      break;
      }
    }

    return invert ?
           (state == 7 || state == 8 || state == 11) :
           (state == 3 || (whitespaces && state == 6));
  }

  public static void main(String[] args) {
    if (args.length > 2 && args.length % 2 == 0) {
      boolean whitespaces = Boolean.parseBoolean(args[0]);
      boolean invert = Boolean.parseBoolean(args[1]);
			System.out.println("Considering whitespaces " + (whitespaces ? "enabled" : "not enabled") + ".");
			System.out.println("Automaton inversion " + (invert ? "enabled" : "not enabled") + ".\n");

      for (int i = 2; i < args.length; ++i)
        System.out.println("Automa returned as expected @ '" + args[i] + "': "
				 								+ (scan(args[i], whitespaces, invert) == Boolean.parseBoolean(args[++i]) ? '\u2713' : '\u2717'));
    } else System.out.println("Usage: java StudentAutomaton <consider spaces> <inverted> <strings to check..>\n\n"
                            + "For the different exercises:\n"
                            + "\t1.3: java StudentAutomaton false false <strings to check..>\n"
                            + "\t1.4: java StudentAutomaton true false <strings to check..>\n"
                            + "\t1.5: java StudentAutomaton false true <strings to check..>");
  }

}
