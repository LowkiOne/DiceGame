public class InputParser {
        public static int parseInRange(String text, int min, int max) {
        if (text == null)
            throw new IllegalArgumentException("Enter a whole number between " + min + " and " + max);

        int number;
        try {
        number = Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Enter a whole number between " + min + " and " + max);
        }

        if (number < min || number > max)
            throw new IllegalArgumentException("Enter a whole number between " + min + " and " + max);

        return number;
    }
}
