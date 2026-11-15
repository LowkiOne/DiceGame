package Services;

import Models.Dice;
import Models.DiceSides;

import java.util.Random;

public class DiceService {
    private final Random random;

    public DiceService() {
        this(new Random());
    }

    public DiceService(Random random) {
        if (random == null) throw new IllegalArgumentException("Random cannot be null");

        this.random = random;
    }

    public int rollDice(Dice dice) {
        requireDice(dice);

        int value = random.nextInt(dice.getSides().getValue()) + 1;
        dice.setValue(value);

        return value;
    }

    public int getDieValue(Dice dice) {
        requireDice(dice);

        int value = dice.getValue();

        if (value <= 0) throw new IllegalStateException("Dice has not been rolled yet");

        return value;
    }

    private void requireDice(Dice dice) {
        if (dice == null) throw new IllegalArgumentException("Dice cannot be null");
    }

    public int increaseScore(int score, int maxScore) {
        if (score < 0) throw new IllegalArgumentException("Score cannot be negative");

        if (maxScore < 1) throw new IllegalArgumentException("Max Score must be at least 1");

        if (score > maxScore) throw new IllegalArgumentException("Score cannot be higher than cast die");

        if (score == maxScore) throw new IllegalArgumentException("Score has already reached the max score (" + maxScore + ")");

        return score + 1;
    }
}
