package spreadsheet;

/** Operation with a left and a right operand. */
public abstract class Operator extends Operand {

    private final Operand left;
    private final Operand right;

    protected Operator(Operand left, Operand right) {
        this.left = left;
        this.right = right;
    }

    public Operand getLeft() {
        return left;
    }

    public Operand getRight() {
        return right;
    }
}
