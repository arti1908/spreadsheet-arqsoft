package spreadsheet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Function call like SUMA(A1:B3;4). Arguments are kept in the order of the formula. */
public abstract class Function extends Operand {

    private final String name;
    private final List<Operand> arguments = new ArrayList<>();

    protected Function(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addArgument(Operand argument) {
        arguments.add(argument);
    }

    public List<Operand> getArguments() {
        return Collections.unmodifiableList(arguments);
    }
}
