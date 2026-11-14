package spreadsheet;

/** A cell used inside a formula. */
public final class CellReference extends Operand {

    private final Coordinate coordinate;

    public CellReference(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
}
