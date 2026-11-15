import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class DiceGameTests {
    @ParameterizedTest
    @ValueSource(strings = {"1", "3", "6"})
    void parseInRange_validNumber_returnsIt(String text) {
        assertEquals(Integer.parseInt(text), DiceGame.parseInRange(text, 1, 6));
    }

    @Test
    void parseInRange_lowestAllowed_isAccepted() {
        assertEquals(1, DiceGame.parseInRange("1", 1, 6));
    }

    @Test
    void parseInRange_highestAllowed_isAccepted() {
        assertEquals(6, DiceGame.parseInRange("6", 1, 6));
    }

    @Test
    void parseInRange_spacesAroundTheNumber_areIgnored() {
        assertEquals(4, DiceGame.parseInRange("  4  ", 1, 6));
    }

    @Test
    void parseInRange_plusSign_isAccepted() {
        assertEquals(5, DiceGame.parseInRange("+5", 1, 6));
    }

    @Test
    void parseInRange_oneNumberRange_acceptsThatNumber() {
        assertEquals(1, DiceGame.parseInRange("1", 1, 1));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "abc", "a1", "1a", "2.5", "2,5", "1 2", "one", "!", "1e1", "0x3"})
    void parseInRange_notAWholeNumber_throws(String text) {
        assertThrows(IllegalArgumentException.class, () -> DiceGame.parseInRange(text, 1, 6));
    }

    @Test
    void parseInRange_numberTooBigForInt_throws() {
        assertThrows(IllegalArgumentException.class, () -> DiceGame.parseInRange("99999999999", 1, 6));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "-100", "7", "100"})
    void parseInRange_outsideTheRange_throws(String text) {
        assertThrows(IllegalArgumentException.class, () -> DiceGame.parseInRange(text, 1, 6));
    }

    @Test
    void parseInRange_oneBelowLowest_throws() {
        assertThrows(IllegalArgumentException.class, () -> DiceGame.parseInRange("0", 1, 6));
    }

    @Test
    void parseInRange_oneAboveHighest_throws() {
        assertThrows(IllegalArgumentException.class, () -> DiceGame.parseInRange("7", 1, 6));
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 6, 8, 10})
    void parseInRange_guessForEachDie_acceptsOneToSidesOnly(int sides) {
        assertEquals(1, DiceGame.parseInRange("1", 1, sides));
        assertEquals(sides, DiceGame.parseInRange(String.valueOf(sides), 1, sides));
        assertThrows(IllegalArgumentException.class, () -> DiceGame.parseInRange(String.valueOf(sides + 1), 1, sides));
    }

    @Test
    void parseInRange_rollsAreLimitedToOneToTwenty() {
        assertEquals(20, DiceGame.parseInRange("20", 1, 20));
        assertThrows(IllegalArgumentException.class, () -> DiceGame.parseInRange("21", 1, 20));
        assertThrows(IllegalArgumentException.class, () -> DiceGame.parseInRange("0", 1, 20));
    }

    @Test
    void parseInRange_error_tellsTheAllowedRange() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> DiceGame.parseInRange("abc", 1, 6));
        assertEquals("Enter a whole number between 1 and 6", e.getMessage());
    }
}
