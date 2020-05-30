import java.util.Scanner;
import calculator.PreservedKeywordException;
import calculator.CalculatorProcessor;

/**
 * The command-lines version is used for debugging purpose only.
 */
public class CalculatorCommandLine {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("    This is a calculator program.\n" +
                           "    Enter /help for help.\n" +
                           "    Enter /exit to exit.");
        System.out.println("=================================================");
        Scanner scanner = new Scanner(System.in);
        CalculatorProcessor calculator = new CalculatorProcessor();
        while (true) {
            System.out.print(">> ");
            String nextLine = scanner.nextLine();
            if ("/exit".equals(nextLine)) {
                System.out.println("Bye!");
                scanner.close();
                return;
            }
            if ("".equals(nextLine)) {
                continue;
            }
            if ("/help".equals(nextLine)) {
                System.out.println("================================================================================");
                System.out.println("Help Menu\n" +
                                   "\n" +
                                   "This is a simple calculator program that allows variable definition, and\n" +
                                   "supports basic functions like exponential, logarithm, and square root.\n" +
                                   "\n" +
                                   "Variable names must start with a latin letter, and follows latin letters or\n" +
                                   "numbers and underscores. Examples: a1, number_of_apples, alpha__.\n" +
                                   "\n" +
                                   "Supported operators (in descending order of priority): () (parenthesis), !,\n" +
                                   "+, - (logical_not, unary positive, unary negative), ^ (power) *, /, %\n" +
                                   "(multiply, divide, remainder), +, - (plus, minus), > < >= <= (greater, less,\n" +
                                   "greater equal, less equal), ==, != (equals, not equals), & (logical_and), |\n" +
                                   "logical_or.\n" +
                                   "\n" +
                                   "Supported assignments: =, +=, -=, *=, /=, %= assignments\n" +
                                   "\n" +
                                   "Supported functions: sqrt() (square root), exp() (exponent), log() (natural\n" +
                                   "logarithm), log10() (logarithm base 10),\n" +
                                   "\n" +
                                   "Predefined variables: e and pi, which can be reassigned if needed.\n");
                System.out.println("================================================================================");
                continue;
            }
            if (nextLine.charAt(0) == '/') {
                System.out.println("Unknown command");
                continue;
            }
            try {
                String result = calculator.expression(nextLine);
                if (result != null) {
                    System.out.println(result);
                }
            } catch (IllegalArgumentException | PreservedKeywordException | ArithmeticException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
