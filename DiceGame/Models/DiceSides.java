package Models;

public enum DiceSides {
    FOUR(4),
    SIX(6),
    EIGHT(8),
    TEN(10);

    private final int value;

    DiceSides(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
