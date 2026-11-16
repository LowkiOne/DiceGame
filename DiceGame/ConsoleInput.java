import java.util.Scanner;
import java.util.function.Function;

public class ConsoleInput {
    private final Scanner scanner;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public String askName(Function<String, String> nameRule) {
        System.out.println("Welcome to the DiceGame!");
        return askUntilValid("Enter your name: ", nameRule);
    }

    public int askDiceSides() {
        DiceSides[] options = DiceSides.values();

        System.out.println("Choose a die:");
        for (int i = 0; i < options.length; i++) {
        System.out.println((i + 1) + ". " + options[i].getValue() + " sides");
        }

        return askUntilValid("Enter 1-" + options.length + ": ",
            text -> options[InputParser.parseInRange(text, 1, options.length) - 1].getValue());
    }    

    public int askAmountOfRolls() {
        int maxRolls = 20;

        return askUntilValid("How many times do you want to roll the die (1-" + maxRolls + ")? ",
            text -> InputParser.parseInRange(text, 1, maxRolls));
    }
    public int askGuess(int sides) {
        return askUntilValid("Guess a number (1-" + sides + "): ",
            text -> InputParser.parseInRange(text, 1, sides));
    }

    private <T> T askUntilValid(String prompt, Function<String, T> parser) {
        while (true) {
            System.out.print(prompt);
            String text = scanner.nextLine();
            try {
                return parser.apply(text);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
