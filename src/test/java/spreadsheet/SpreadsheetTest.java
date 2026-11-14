package spreadsheet;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SpreadsheetTest {

    @Test
    void coordinateParsesColumnAndRow() {
        Coordinate c = Coordinate.parse("AB12");
        assertEquals("AB", c.getColumn());
        assertEquals(12, c.getRow());
        assertEquals("AB12", c.toString());
    }

    @Test
    void columnNumbersFollowTheLetters() {
        assertEquals(1, Coordinate.parse("A1").getColumnNumber());
        assertEquals(26, Coordinate.parse("Z1").getColumnNumber());
        assertEquals(27, Coordinate.parse("AA1").getColumnNumber());
        assertEquals(28, Coordinate.parse("AB1").getColumnNumber());
    }

    @Test
    void invalidCoordinatesAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> Coordinate.parse("1A"));
        assertThrows(IllegalArgumentException.class, () -> Coordinate.parse("A0"));
        assertThrows(IllegalArgumentException.class, () -> Coordinate.parse("A"));
    }

    @Test
    void sameCoordinateIsEqual() {
        assertEquals(Coordinate.parse("a1"), Coordinate.parse("A1"));
    }

    @Test
    void contentTypeIsDetected() {
        assertInstanceOf(NumericalContent.class, ContentFactory.create("4"));
        assertInstanceOf(NumericalContent.class, ContentFactory.create("1.2"));
        assertInstanceOf(TextContent.class, ContentFactory.create("TOTAL"));
        assertInstanceOf(FormulaContent.class, ContentFactory.create("=1+2"));
    }

    @Test
    void storesNumbersTextAndFormulasWithoutComputing() {
        Spreadsheet s = new Spreadsheet();
        s.setCellContent("A1", "=C1+C2");
        s.setCellContent("B1", "4");
        s.setCellContent("B3", "TOTAL");
        s.setCellContent("C2", "1.5");

        assertEquals("=C1+C2", s.getCellText("A1")); // stored as text, not calculated
        assertEquals("4", s.getCellText("B1"));
        assertEquals("TOTAL", s.getCellText("B3"));
        assertEquals("1.5", s.getCellText("C2"));
        assertEquals("", s.getCellText("Z9"));       // empty cell
    }

    @Test
    void emptyTextClearsTheCell() {
        Spreadsheet s = new Spreadsheet();
        s.setCellContent("A1", "5");
        s.setCellContent("A1", "");
        assertNull(s.getCellContent(Coordinate.parse("A1")));
        assertEquals(0, s.getCells().size());
    }

    @Test
    void sizeOfUsedArea() {
        Spreadsheet s = new Spreadsheet();
        s.setCellContent("A1", "1");
        s.setCellContent("C3", "x");
        assertEquals(3, s.getLastRow());
        assertEquals(3, s.getLastColumnNumber());
    }
}
