
import Models.Dice;
import Models.DiceSides;
import Services.DiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
 
import static org.junit.jupiter.api.Assertions.*;

public class DiceServiceTest {
    private DiceService service;

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
        service = new DiceService();
    }

    @Test
    void rollDice_fixedRnadom_returnsValue() {
        DiceService fixed = new DiceService(new FixedRandom(3));

        Dice dice = new Dice(DiceSides.FOUR);
        assertEquals(4, fixed.rollDice(dice));
    }

        @Test
    void rollDice_lowestAndHighestResult_areOneAndSides() {
        Dice dice = new Dice(DiceSides.TEN);
        assertEquals(1, new DiceService(new FixedRandom(0)).rollDice(dice));
        assertEquals(10, new DiceService(new FixedRandom(9)).rollDice(dice));
    }

    @RepeatedTest(50)
    void rollDice_realRandom_isBetweenOneAndSides() {
        Dice dice = new Dice(DiceSides.EIGHT);
        int result = service.rollDice(dice);
        assertTrue(result >= 1 && result <= 8);
        assertEquals(result, dice.getValue());
    }

    @ParameterizedTest
    @EnumSource(DiceSides.class)
    void rollDice_allSides_staysInRange(DiceSides sides) {
        Dice dice = new Dice(sides);
        for (int i = 0; i < 1000; i++) {
            int result = service.rollDice(dice);
            assertTrue(result >= 1 && result <= sides.getValue(),
                    sides.getValue() + " sides gave " + result);
            assertEquals(result, dice.getValue()); 
        }
    }

    @Test
    void rollDice_nullDice_throws() {
        assertThrows(IllegalArgumentException.class, () -> service.rollDice(null));
    }

        @Test
    void getDieValue_afterRoll_returnsValue() {
        Dice dice = new Dice(DiceSides.SIX);
        dice.setValue(4);
        assertEquals(4, service.getDieValue(dice));
    }

    @Test
    void getDieValue_notRolledYet_throws() {
        assertThrows(IllegalStateException.class, () -> service.getDieValue(new Dice(DiceSides.SIX)));
    }

    @Test
    void getDieValue_nullDice_throws() {
        assertThrows(IllegalArgumentException.class, () -> service.getDieValue(null));
    }
}
