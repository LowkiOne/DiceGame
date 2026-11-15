import java.util.Scanner;
import java.util.function.Function;

import Models.Die;
import Models.Player;
import Models.DiceSides;

public class DiceGame {
    private static final Player player = new Player();
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        askName();
        Die dice = new Die(DiceSides.TEN);
        player.rollDice(dice);

        int getDie = player.getDieValue(dice);
        String getName = player.getName();

        System.out.println(getName + getDie);
    }

    private static String askName() {
        System.out.println("Welcome to the DiceGame!");
        return askNameUntilValid("Enter your name: ", text -> player.input(text));
    }

    private static <T> T askNameUntilValid(String prompt, Function<String, T> parser) {
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