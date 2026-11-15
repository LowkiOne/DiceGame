package Models;

public class Player {
    private String name;
    private int points;
    private Die die;

    public Player(Die die) {
        if(die == null)
            throw new IllegalArgumentException("Dice cannot be null");
    }

    public Player() {

    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public Die getDie() {
        return die;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPoints(int amount) {
        this.points += amount;
    }

    public int rollDice(Die die) {
        requireDice(die);

        die.roll();

        return die.getValue();
    }

    public int getDieValue(Die dice) {
        requireDice(dice);

        int value = dice.getValue();

        if (value <= 0) throw new IllegalStateException("Dice has not been rolled yet");

        return value;
    }

    private void requireDice(Die dice) {
        if (dice == null) throw new IllegalArgumentException("Dice cannot be null");
    }

    public int increaseScore(int score, int maxScore) {
        if (score < 0) throw new IllegalArgumentException("Score cannot be negative");

        if (maxScore < 1) throw new IllegalArgumentException("Max Score must be at least 1");

        if (score > maxScore) throw new IllegalArgumentException("Score cannot be higher than cast die");

        if (score == maxScore) throw new IllegalArgumentException("Score has already reached the max score (" + maxScore + ")");

        return score + 1;
    }

     private String validateName(String input) {
        if (input == null)
            throw new IllegalArgumentException("Name cannot be null");
     
        String cleaned = input.trim();

        if(cleaned.isEmpty())
            throw new IllegalArgumentException("Name cannot be empty");

        if(cleaned.length() > 15)
            throw new IllegalArgumentException("Name cannot be longer than 15 characters");

        if(!cleaned.matches("[\\p{L}\\p{N} _-]+"))
            throw new IllegalArgumentException("Name contains invalid characters, must be only letters");

        return cleaned;
    }

    public String input(String input) {
        String cleaned = validateName(input);
        setName(cleaned);

        return cleaned;
    }

}
