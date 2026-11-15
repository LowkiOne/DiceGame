package Models;

public class Dice {
    private int value;
    private DiceSides sides;

    public int getValue() {
        return value;
    }

    public Dice(DiceSides sides) {
        this.sides = sides;
    }

    public DiceSides getSides() {
        return sides;
    }

    public void setValue(int value) {
        this.value = value;
    }   
}
