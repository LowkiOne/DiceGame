
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
 
import java.util.Random;
 
import static org.junit.jupiter.api.Assertions.*;

public class DieTests {
    private Player player;

    private static class FixedRandom extends Random {
        private final int result;

        FixedRandom(int result) {
            this.result = result;
        }

            @Override 
            public int nextInt(int bound) {
                return result;
        }
    }

    @BeforeEach
    void setUp() {
        player = new Player(new Die(DiceSides.SIX));
    }

    // Positive: a fixed random 3 on a 4-sided die gives the value 4
    @Test
    void roll_fixedRandom_returnsValue() {
        Die fixed = new Die(DiceSides.FOUR, new FixedRandom(3));

        fixed.roll();
        assertEquals(4, fixed.getValue());
    }

    // Positive: the lowest roll is 1 and the highest roll is the number of sides
    @Test
    void roll_lowestAndHighestResult_areOneAndSides() {
        Die lowest = new Die(DiceSides.TEN, new FixedRandom(0));
        lowest.roll();
        assertEquals(1, lowest.getValue());

        Die highest = new Die(DiceSides.TEN, new FixedRandom(9));
        highest.roll();
        assertEquals(10, highest.getValue());
    }

    // Positive: a real roll on an 8-sided die is always between 1 and 8 (repeated 50 times)
    @RepeatedTest(50)
    void roll_realRandom_isBetweenOneAndSides() {
        Die die = new Die(DiceSides.EIGHT);
        die.roll();
        assertTrue(die.getValue() >= 1 && die.getValue() <= 8);
    }

    // Positive: 1000 rolls on every die size stay between 1 and the number of sides
    @ParameterizedTest
    @EnumSource(DiceSides.class)
    void roll_allSides_staysInRange(DiceSides sides) {
        Die die = new Die(sides);
        for (int i = 0; i < 1000; i++) {
            die.roll();
            assertTrue(die.getValue() >= 1 && die.getValue() <= sides.getValue(),
                    sides.getValue() + " sides gave " + die.getValue());
        }
    }

    // Negative: a die that has not been rolled yet throws IllegalStateException
    @Test
    void getDieValue_notRolledYet_throws() {
        assertThrows(IllegalStateException.class, () -> player.getDieValue(new Die(DiceSides.SIX)));
    }

    // Negative: a null die throws IllegalArgumentException
    @Test
    void getDieValue_nullDice_throws() {
        assertThrows(IllegalArgumentException.class, () -> player.getDieValue(null));
    }
}
