package spreadsheet;

/** Formula text. The operand tree stays null until we have a parser. */
public final class FormulaContent implements Content {

    private final String expression;
    private final Operand root;

    public FormulaContent(String expression) {
        this(expression, null);
    }

    public FormulaContent(String expression, Operand root) {
        if (expression == null || !expression.startsWith("=")) {
            throw new IllegalArgumentException("A formula must start with '='");
        }
        this.expression = expression;
        this.root = root;
    }

    public Operand getRoot() {
        return root;
    }

    @Override
    public String getText() {
        return expression;
    }
}
