import java.util.Scanner;

public class DiceGame {
    private static final Player player = new Player();
    private static final ConsoleInput input = new ConsoleInput(new Scanner(System.in));
    public static void main(String[] args) {
        playTheGame();
    }

    private static void playTheGame() {
        input.askName(player::input);
        player.addDie(input.askDiceSides());
        int sides = player.getDie().getSides().getValue();
        int rolls = input.askAmountOfRolls();
        Die die = player.getDie();

        for (int i = 1; i <= rolls; i++) {
            System.out.println();
            System.out.println("--- Roll " + i + " of " + rolls + " ---");

            int guess = input.askGuess(sides);

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
        System.out.println(player.getName() + "! Game finished! Total points: " + player.getPoints() + " of " + rolls);
    }
}