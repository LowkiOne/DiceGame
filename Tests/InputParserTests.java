import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class InputParserTests {
    // Positive: valid numbers inside the range are returned
    @ParameterizedTest
    @ValueSource(strings = {"1", "3", "6"})
    void parseInRange_validNumber_returnsIt(String text) {
        assertEquals(Integer.parseInt(text), InputParser.parseInRange(text, 1, 6));
    }

    // Positive: the lowest allowed number is accepted
    @Test
    void parseInRange_lowestAllowed_isAccepted() {
        assertEquals(1, InputParser.parseInRange("1", 1, 6));
    }

    // Positive: the highest allowed number is accepted
    @Test
    void parseInRange_highestAllowed_isAccepted() {
        assertEquals(6, InputParser.parseInRange("6", 1, 6));
    }

    // Positive: spaces around the number are removed
    @Test
    void parseInRange_spacesAroundTheNumber_areIgnored() {
        assertEquals(4, InputParser.parseInRange("  4  ", 1, 6));
    }

    // Positive: a plus sign in front of the number is accepted
    @Test
    void parseInRange_plusSign_isAccepted() {
        assertEquals(5, InputParser.parseInRange("+5", 1, 6));
    }

    // Positive: a range with only one number accepts that number
    @Test
    void parseInRange_oneNumberRange_acceptsThatNumber() {
        assertEquals(1, InputParser.parseInRange("1", 1, 1));
    }

    // Negative: null, empty text, letters, decimals and symbols throw IllegalArgumentException
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "abc", "a1", "1a", "2.5", "2,5", "1 2", "one", "!", "1e1", "0x3"})
    void parseInRange_notAWholeNumber_throws(String text) {
        assertThrows(IllegalArgumentException.class, () -> InputParser.parseInRange(text, 1, 6));
    }

    // Negative: a number too big for int throws IllegalArgumentException
    @Test
    void parseInRange_numberTooBigForInt_throws() {
        assertThrows(IllegalArgumentException.class, () -> InputParser.parseInRange("99999999999", 1, 6));
    }

    // Negative: numbers below the lowest or above the highest throw IllegalArgumentException
    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "-100", "7", "100"})
    void parseInRange_outsideTheRange_throws(String text) {
        assertThrows(IllegalArgumentException.class, () -> InputParser.parseInRange(text, 1, 6));
    }

    // Negative: one below the lowest allowed number throws
    @Test
    void parseInRange_oneBelowLowest_throws() {
        assertThrows(IllegalArgumentException.class, () -> InputParser.parseInRange("0", 1, 6));
    }

    // Negative: one above the highest allowed number throws
    @Test
    void parseInRange_oneAboveHighest_throws() {
        assertThrows(IllegalArgumentException.class, () -> InputParser.parseInRange("7", 1, 6));
    }

    // Positive and negative: 1 and the number of sides are accepted, one more throws, for every die
    @ParameterizedTest
    @ValueSource(ints = {4, 6, 8, 10})
    void parseInRange_guessForEachDie_acceptsOneToSidesOnly(int sides) {
        assertEquals(1, InputParser.parseInRange("1", 1, sides));
        assertEquals(sides, InputParser.parseInRange(String.valueOf(sides), 1, sides));
        assertThrows(IllegalArgumentException.class, () -> InputParser.parseInRange(String.valueOf(sides + 1), 1, sides));
    }

    // Positive and negative: 20 rolls are accepted, 21 and 0 throw
    @Test
    void parseInRange_rollsAreLimitedToOneToTwenty() {
        assertEquals(20, InputParser.parseInRange("20", 1, 20));
        assertThrows(IllegalArgumentException.class, () -> InputParser.parseInRange("21", 1, 20));
        assertThrows(IllegalArgumentException.class, () -> InputParser.parseInRange("0", 1, 20));
    }

    // Negative: the error message tells which numbers are allowed
    @Test
    void parseInRange_error_tellsTheAllowedRange() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> InputParser.parseInRange("abc", 1, 6));
        assertEquals("Enter a whole number between 1 and 6", e.getMessage());
    }
}
