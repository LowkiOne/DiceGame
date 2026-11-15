package Services;

import Models.Player;

public class PlayerService {
    public String validateName(String input) {
        if (input == null)
            throw new IllegalArgumentException("Name cannot be null");
     
        String cleaned = input.trim();

        if(cleaned.isEmpty())
            throw new IllegalArgumentException("Name cannot be empty");

        if(cleaned.length() > 15)
            throw new IllegalArgumentException("Name cannot be longer than 15 characters");

        if(!cleaned.matches("[\\p{L}\\p{N} _-]+"))
            throw new IllegalArgumentException("Name contains invalid characters, must be only letters");

        return cleaned;
    }

    public String input(Player player, String input) {
        String cleaned = validateName(input);
        player.setName(cleaned);

        return cleaned;
    }
}
