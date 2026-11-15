
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

    @Test
    void roll_fixedRandom_returnsValue() {
        Die fixed = new Die(DiceSides.FOUR, new FixedRandom(3));

        fixed.roll();
        assertEquals(4, fixed.getValue());
    }

    @Test
    void roll_lowestAndHighestResult_areOneAndSides() {
        Die lowest = new Die(DiceSides.TEN, new FixedRandom(0));
        lowest.roll();
        assertEquals(1, lowest.getValue());

        Die highest = new Die(DiceSides.TEN, new FixedRandom(9));
        highest.roll();
        assertEquals(10, highest.getValue());
    }

    @RepeatedTest(50)
    void roll_realRandom_isBetweenOneAndSides() {
        Die die = new Die(DiceSides.EIGHT);
        die.roll();
        assertTrue(die.getValue() >= 1 && die.getValue() <= 8);
    }

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

    @Test
    void getDieValue_afterRoll_returnsValue() {
        Die die = new Die(DiceSides.SIX);
        die.setValue(4);
        assertEquals(4, player.getDieValue(die));
    }

    @Test
    void getDieValue_notRolledYet_throws() {
        assertThrows(IllegalStateException.class, () -> player.getDieValue(new Die(DiceSides.SIX)));
    }

    @Test
    void getDieValue_nullDice_throws() {
        assertThrows(IllegalArgumentException.class, () -> player.getDieValue(null));
    }
}
