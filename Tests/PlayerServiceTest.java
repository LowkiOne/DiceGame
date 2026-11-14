import Models.Dice;
import Models.DiceSides;
import Models.Player;
import Services.PlayerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerServiceTest {
    private PlayerService service;
    private FakeDice dice;
    private Player player;
    private static class FakeDice extends Dice {
        private int value;

        FakeDice() {
            super(DiceSides.SIX);
        }

        void setValue(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }
    }
    
    @BeforeEach
    void setUp() {
        service = new PlayerService();
        dice = new FakeDice();
        player = new Player(dice);
    }

    @Test
    void input_validName_returnsName() {
        assertEquals("Joe", service.input(player, "Joe"));
        assertEquals("Joe", player.getName());
    }
}
