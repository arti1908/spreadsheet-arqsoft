package spreadsheet;

public final class Number extends Operand {

    private final double number;

    public Number(double number) {
        this.number = number;
    }

    public double getNumber() {
        return number;
    }
}
