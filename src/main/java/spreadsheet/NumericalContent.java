package spreadsheet;

public final class NumericalContent implements Content {

    private final double number;

    public NumericalContent(double number) {
        this.number = number;
    }

    public double getNumber() {
        return number;
    }

    @Override
    public String getText() {
        if (number == Math.rint(number) && !Double.isInfinite(number)) {
            return String.valueOf((long) number);
        }
        return String.valueOf(number);
    }
}
