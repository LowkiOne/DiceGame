package Models;

public class Player {
    private String name;
    private int points;
    private Dice dice;

    public Player(Dice dice) {
        if(dice == null)
            throw new IllegalArgumentException("Dice cannot be null");
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public Dice getDice() {
        return dice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPoints(int amount) {
        this.points += amount;
    }
}
