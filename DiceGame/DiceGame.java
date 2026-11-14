import java.util.Scanner;
import java.util.function.Function;

import Models.Dice;
import Models.Player;
import Models.DiceSides;
import Services.PlayerService;

public class DiceGame {
    private static final PlayerService playerService = new PlayerService();
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        askName();
    }

    private static String askName() {
        System.out.println("Welcome to the DiceGame!");
        Player temp = new Player(new Dice(DiceSides.SIX));
        return askNameUntilValid("Enter your name: ", text -> playerService.input(temp, text));
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