import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTests {
    private Die dice;
    private Player player;

    @BeforeEach
    void setUp() {
        dice = new Die(DiceSides.FOUR);
        player = new Player(dice);
    }

    @Test
    void input_validName_returnsName() {
        assertEquals("Joe", player.input("Joe"));
        assertEquals("Joe", player.getName());
    }

    @Test
    void rollDice_nullDice_throws() {
        assertThrows(IllegalArgumentException.class, () -> player.rollDice(null));
    }
}
