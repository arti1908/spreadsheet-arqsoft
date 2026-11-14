package spreadsheet;

public final class NumberValue extends Value {

    private final double number;

    public NumberValue(double number) {
        this.number = number;
    }

    public double getNumber() {
        return number;
    }
}
