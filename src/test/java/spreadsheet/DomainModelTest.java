package spreadsheet;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DomainModelTest {

    @Test
    void operatorKeepsItsTwoOperands() {
        Number one = new Number(1);
        CellReference a1 = new CellReference(Coordinate.parse("A1"));
        Addition sum = new Addition(one, a1);
        assertSame(one, sum.getLeft());
        assertSame(a1, sum.getRight());
    }

    @Test
    void operatorsCanBeNested() {
        // =1+2*3
        Multiplication product = new Multiplication(new Number(2), new Number(3));
        Addition sum = new Addition(new Number(1), product);
        assertInstanceOf(Multiplication.class, sum.getRight());
    }

    @Test
    void functionKeepsItsArgumentsInOrder() {
        // =SUMA(4;A1:B3;C1)
        SUMA suma = new SUMA();
        Number four = new Number(4);
        Range range = new Range(Coordinate.parse("A1"), Coordinate.parse("B3"));
        CellReference c1 = new CellReference(Coordinate.parse("C1"));
        suma.addArgument(four);
        suma.addArgument(range);
        suma.addArgument(c1);
        assertEquals("SUMA", suma.getName());
        assertEquals(3, suma.getArguments().size());
        assertSame(four, suma.getArguments().get(0));
        assertSame(range, suma.getArguments().get(1));
        assertSame(c1, suma.getArguments().get(2));
    }

    @Test
    void functionCanHaveOnlyARange() {
        // =SUMA(A1:B3)
        SUMA suma = new SUMA();
        suma.addArgument(new Range(Coordinate.parse("A1"), Coordinate.parse("B3")));
        assertEquals(1, suma.getArguments().size());
    }

    @Test
    void rangeKeepsItsCorners() {
        Range range = new Range(Coordinate.parse("A1"), Coordinate.parse("B3"));
        assertEquals("A1", range.getUpperLeft().toString());
        assertEquals("B3", range.getBottomRight().toString());
        assertInstanceOf(Operand.class, range);
    }

    @Test
    void functionsCanBeNested() {
        // =MAX(1;MIN(2;3))
        MAX max = new MAX();
        MIN min = new MIN();
        min.addArgument(new Number(2));
        min.addArgument(new Number(3));
        max.addArgument(new Number(1));
        max.addArgument(min);
        assertSame(min, max.getArguments().get(1));
    }

    @Test
    void functionNames() {
        assertEquals("MIN", new MIN().getName());
        assertEquals("MAX", new MAX().getName());
        assertEquals("PROMEDIO", new PROMEDIO().getName());
    }

    @Test
    void valuesHoldANumberOrAString() {
        assertEquals(2.5, new NumberValue(2.5).getNumber());
        assertEquals("hi", new StringValue("hi").getText());
    }

    @Test
    void formulaContentHasNoTreeUntilParsed() {
        assertNull(new FormulaContent("=1+2").getRoot());
        Operand tree = new Addition(new Number(1), new Number(2));
        assertSame(tree, new FormulaContent("=1+2", tree).getRoot());
    }
}
