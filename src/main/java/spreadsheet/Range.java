package spreadsheet;

/** Cells between two corners, like A1:B3. */
public final class Range extends Operand {

    private final Coordinate upperLeft;
    private final Coordinate bottomRight;

    public Range(Coordinate upperLeft, Coordinate bottomRight) {
        this.upperLeft = upperLeft;
        this.bottomRight = bottomRight;
    }

    public Coordinate getUpperLeft() {
        return upperLeft;
    }

    public Coordinate getBottomRight() {
        return bottomRight;
    }
}
