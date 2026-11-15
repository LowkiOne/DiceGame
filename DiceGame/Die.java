import java.util.Random;

public class Die {
    private int value;
    private DiceSides sides;
    private Random random;

    public Die(DiceSides sides) {
        this(sides, new Random());
    }

    public Die() {
        
    }

    public Die(DiceSides sides, Random random) {
        if (sides == null)
            throw new IllegalArgumentException("Die sides cannot be null");

        if (random == null)
            throw new IllegalArgumentException("Random cannot be null");

        this.sides = sides;
        this.random = random;
    }

    public int getValue() {
        return value;
    }

    public DiceSides getSides() {
        return sides;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void roll() {
        value = random.nextInt(sides.getValue()) + 1;
    }
}
