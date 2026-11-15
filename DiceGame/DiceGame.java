import java.util.Scanner;
import java.util.function.Function;

public class DiceGame {
    private static final Player player = new Player();
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        playTheGame();
    }
    
    private static String askName() {
        System.out.println("Welcome to the DiceGame!");
        return askUntilValid("Enter your name: ", text -> player.input(text));
    }

    private static int askDiceSides() {
        DiceSides[] options = DiceSides.values();

        System.out.println("Choose a die:");
        for (int i = 0; i < options.length; i++) {
        System.out.println((i + 1) + ". " + options[i].getValue() + " sides");
        }

        int sides = askUntilValid("Enter 1-" + options.length + ": ",
            text -> options[parseInRange(text, 1, options.length) - 1].getValue());

        player.addDie(sides);

        return sides;
    }

    private static int askAmountOfRolls() {
        int maxRolls = 20;

        return askUntilValid("How many times do you want to roll the die (1-" + maxRolls + ")? ",
            text -> parseInRange(text, 1, maxRolls));
    }

    private static int askGuess(int sides) {
        return askUntilValid("Guess a number (1-" + sides + "): ",
            text -> parseInRange(text, 1, sides));
    }

    private static void playTheGame() {
        String name = askName();
        int sides = askDiceSides();
        int rolls = askAmountOfRolls();
        Die die = player.getDie();

        for (int i = 1; i <= rolls; i++) {
            System.out.println();
            System.out.println("--- Roll " + i + " of " + rolls + " ---");

            int guess = askGuess(sides);

            player.rollDice(die);
            int result = player.getDieValue(die);

            int pointsForRoll = 0;
            if (guess == result) {
                int newPoints = player.increaseScore(player.getPoints(), rolls);
                pointsForRoll = newPoints - player.getPoints();
                player.addPoints(pointsForRoll);
            }

            System.out.println("Your guess:       " + guess);
            System.out.println("Die result:       " + result);
            System.out.println(guess == result ? "Correct!" : "Wrong.");
            System.out.println("Points this roll: " + pointsForRoll);
            System.out.println("Total points:     " + player.getPoints());
        }

        System.out.println();
        System.out.println(name + "! Game finished! Total points: " + player.getPoints() + " of " + rolls);
    }

    private static <T> T askUntilValid(String prompt, Function<String, T> parser) {
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

    static int parseInRange(String text, int min, int max) {
        if (text == null)
            throw new IllegalArgumentException("Enter a whole number between " + min + " and " + max);

        int number;
        try {
        number = Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Enter a whole number between " + min + " and " + max);
        }

        if (number < min || number > max)
            throw new IllegalArgumentException("Enter a whole number between " + min + " and " + max);

        return number;
    }
}