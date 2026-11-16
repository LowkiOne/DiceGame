import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTests {
    private Player player;

    // A Random that always gives the same number, so the roll is known
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
        player = new Player();
    }

    // ---------- input / validateName (positive) ----------

    // Positive: a valid name is returned and stored
    @Test
    void input_validName_returnsName() {
        assertEquals("Joe", player.input("Joe"));
        assertEquals("Joe", player.getName());
    }

    // Positive: spaces around the name are removed
    @Test
    void input_spacesAroundName_areTrimmed() {
        assertEquals("Joe", player.input("  Joe  "));
        assertEquals("Joe", player.getName());
    }

    // Positive: names with only letters (also åäö and accents) up to 15 letters are accepted
    @ParameterizedTest
    @ValueSource(strings = {"A", "Åsa", "Éva", "Zoë", "Björn", "abcdefghijklmno"})
    void input_namesWithOnlyLetters_areAccepted(String name) {
        assertEquals(name, player.input(name));
    }

    // Positive: a second valid name replaces the first one
    @Test
    void input_secondValidName_replacesTheFirst() {
        player.input("Anna");
        player.input("Joe");
        assertEquals("Joe", player.getName());
    }

    // ---------- input / validateName (negative) ----------

    // Negative: null, empty and blank names throw IllegalArgumentException
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "     "})
    void input_nullEmptyOrBlank_throws(String name) {
        assertThrows(IllegalArgumentException.class, () -> player.input(name));
    }

    // Negative: a name with 16 letters is too long and throws
    @Test
    void input_sixteenLetters_throws() {
        assertThrows(IllegalArgumentException.class, () -> player.input("abcdefghijklmnop"));
    }

    // Negative: digits, symbols and spaces inside the name throw
    @ParameterizedTest
    @ValueSource(strings = {"Joe1", "123", "Joe!", "Joe@", "Joe.", "Joe_", "Joe-Lee", "Joe Lee", "<b>"})
    void input_notOnlyLetters_throws(String name) {
        assertThrows(IllegalArgumentException.class, () -> player.input(name));
    }

    // Negative: a rejected name does not change the stored name
    @Test
    void input_invalidName_keepsThePreviousName() {
        player.input("Anna");

        assertThrows(IllegalArgumentException.class, () -> player.input("Anna123"));

        assertEquals("Anna", player.getName());
    }

    // Negative: a rejected first name leaves the name empty (null)
    @Test
    void input_invalidNameFirst_leavesNameEmpty() {
        assertThrows(IllegalArgumentException.class, () -> player.input(""));
        assertNull(player.getName());
    }

    // ---------- addDie (positive) ----------

    // Positive: 4, 6, 8 and 10 create a die with that number of sides
    @ParameterizedTest
    @ValueSource(ints = {4, 6, 8, 10})
    void addDie_validNumberOfSides_createsDieWithThoseSides(int sides) {
        player.addDie(sides);

        assertNotNull(player.getDie());
        assertEquals(sides, player.getDie().getSides().getValue());
    }

    // Positive: a new die has not been rolled (value 0)
    @Test
    void addDie_newDie_hasNotBeenRolled() {
        player.addDie(6);
        assertEquals(0, player.getDie().getValue());
    }

    // Positive: a second call replaces the die with a new one
    @Test
    void addDie_secondCall_replacesTheDie() {
        player.addDie(6);
        Die first = player.getDie();

        player.addDie(10);

        assertEquals(10, player.getDie().getSides().getValue());
        assertNotSame(first, player.getDie());
    }

    // ---------- addDie (negative) ----------

    // Negative: any number except 4, 6, 8 and 10 throws IllegalArgumentException
    @ParameterizedTest
    @ValueSource(ints = {-4, -1, 0, 1, 2, 3, 5, 7, 9, 11, 12, 20, 100})
    void addDie_invalidNumberOfSides_throws(int sides) {
        assertThrows(IllegalArgumentException.class, () -> player.addDie(sides));
    }

    // Negative: a rejected number creates no die
    @Test
    void addDie_invalidNumberOfSides_leavesNoDie() {
        assertThrows(IllegalArgumentException.class, () -> player.addDie(7));
        assertNull(player.getDie());
    }

    // Negative: a rejected number keeps the old die
    @Test
    void addDie_invalidNumberOfSides_keepsTheOldDie() {
        player.addDie(6);
        Die old = player.getDie();

        assertThrows(IllegalArgumentException.class, () -> player.addDie(5));

        assertSame(old, player.getDie());
    }

    // ---------- getDieValue (positive) ----------

    // Positive: returns the value after the die has been rolled
    @Test
    void getDieValue_afterRoll_returnsTheValue() {
        Die die = new Die(DiceSides.SIX, new FixedRandom(3));
        die.roll();

        assertEquals(4, player.getDieValue(die));
    }

    // Positive: returns 1 for the lowest roll and 10 for the highest on a 10-sided die
    @Test
    void getDieValue_lowestAndHighestValue_areReturned() {
        Die lowest = new Die(DiceSides.TEN, new FixedRandom(0));
        lowest.roll();
        assertEquals(1, player.getDieValue(lowest));

        Die highest = new Die(DiceSides.TEN, new FixedRandom(9));
        highest.roll();
        assertEquals(10, player.getDieValue(highest));
    }

    // Positive: returns the new value after the die is rolled again
    @Test
    void getDieValue_rolledAgain_returnsTheNewValue() {
        Die twice = new Die(DiceSides.SIX, new Random() {
            private int calls = 0;

            @Override
            public int nextInt(int bound) {
                return calls++ == 0 ? 0 : 5;
            }
        });

        twice.roll();
        assertEquals(1, player.getDieValue(twice));

        twice.roll();
        assertEquals(6, player.getDieValue(twice));
    }

    // ---------- getDieValue (negative) ----------

    // Negative: a die that has not been rolled yet throws IllegalStateException
    @Test
    void getDieValue_notRolledYet_throws() {
        Die die = new Die(DiceSides.SIX);
        assertThrows(IllegalStateException.class, () -> player.getDieValue(die));
    }

    // Negative: a null die throws IllegalArgumentException
    @Test
    void getDieValue_nullDice_throws() {
        assertThrows(IllegalArgumentException.class, () -> player.getDieValue(null));
    }

    // ---------- increaseScore (positive) ----------

    // Positive: the score goes up by exactly 1
    @Test
    void increaseScore_increasesByOne() {
        assertEquals(5, player.increaseScore(4, 10));
    }

    // Positive: from 0 the score becomes 1
    @Test
    void increaseScore_fromZero_givesOne() {
        assertEquals(1, player.increaseScore(0, 3));
    }

    // Positive: the score can go up to the max score
    @Test
    void increaseScore_canReachMaxScore() {
        assertEquals(10, player.increaseScore(9, 10));
    }

    // Positive: in a one-roll game the score can reach 1
    @Test
    void increaseScore_oneRollGame_canReachOne() {
        assertEquals(1, player.increaseScore(0, 1));
    }

    // Positive: counting up step by step ends exactly on the max score
    @Test
    void increaseScore_countingUpToMax_endsOnMax() {
        int score = 0;
        for (int i = 0; i < 5; i++) {
            score = player.increaseScore(score, 5);
        }
        assertEquals(5, score);
    }

    // ---------- increaseScore (negative) ----------

    // Negative: a score that is already at the max throws
    @Test
    void increaseScore_alreadyAtMax_throws() {
        assertThrows(IllegalArgumentException.class, () -> player.increaseScore(10, 10));
    }

    // Negative: one more call after reaching the max throws
    @Test
    void increaseScore_oneMoreCallAfterReachingMax_throws() {
        int score = 0;
        for (int i = 0; i < 5; i++) {
            score = player.increaseScore(score, 5);
        }
        int atMax = score;
        assertThrows(IllegalArgumentException.class, () -> player.increaseScore(atMax, 5));
    }

    // Negative: a score above the max throws
    @Test
    void increaseScore_scoreAboveMax_throws() {
        assertThrows(IllegalArgumentException.class, () -> player.increaseScore(11, 10));
    }

    // Negative: a negative score throws
    @Test
    void increaseScore_negativeScore_throws() {
        assertThrows(IllegalArgumentException.class, () -> player.increaseScore(-1, 5));
    }

    // Negative: a max score of 0 or lower throws
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -3})
    void increaseScore_maxScoreBelowOne_throws(int maxScore) {
        assertThrows(IllegalArgumentException.class, () -> player.increaseScore(0, maxScore));
    }
}
