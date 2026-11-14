package spreadsheet;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class S2VTest {

    // The example of the project statement:
    //      A       B       C
    //  1   =C1+C2  4       1
    //  2                   2
    //  3           TOTAL   =A1+B1
    private static Spreadsheet example() {
        Spreadsheet s = new Spreadsheet();
        s.setCellContent("A1", "=C1+C2");
        s.setCellContent("B1", "4");
        s.setCellContent("C1", "1");
        s.setCellContent("C2", "2");
        s.setCellContent("B3", "TOTAL");
        s.setCellContent("C3", "=A1+B1");
        return s;
    }

    private static final String EXAMPLE_FILE = "=C1+C2;4;1\n;;2\n;TOTAL;=A1+B1\n";

    @Test
    void columnNumberToCoordinate() {
        assertEquals("A3", Coordinate.of(1, 3).toString());
        assertEquals("Z1", Coordinate.of(26, 1).toString());
        assertEquals("AA1", Coordinate.of(27, 1).toString());
        assertEquals("AB1", Coordinate.of(28, 1).toString());
        assertEquals("AZ1", Coordinate.of(52, 1).toString());
        assertEquals("BA1", Coordinate.of(53, 1).toString());
    }

    @Test
    void savesTheProjectExample() {
        assertEquals(EXAMPLE_FILE, S2VWriter.toS2V(example()));
    }

    @Test
    void loadsTheProjectExample() {
        Spreadsheet s = S2VReader.fromS2V(EXAMPLE_FILE);
        assertEquals("=C1+C2", s.getCellText("A1"));
        assertEquals("4", s.getCellText("B1"));
        assertEquals("1", s.getCellText("C1"));
        assertEquals("", s.getCellText("A2"));
        assertEquals("2", s.getCellText("C2"));
        assertEquals("TOTAL", s.getCellText("B3"));
        assertEquals("=A1+B1", s.getCellText("C3"));
        assertEquals(6, s.getCells().size());
    }

    @Test
    void functionSeparatorIsCommaInTheFile() {
        Spreadsheet s = new Spreadsheet();
        s.setCellContent("A1", "=SUMA(1;2;B1:B3)");
        assertEquals("=SUMA(1,2,B1:B3)\n", S2VWriter.toS2V(s));
    }

    @Test
    void functionSeparatorIsSemicolonAgainAfterLoading() {
        Spreadsheet s = S2VReader.fromS2V("=SUMA(1,2,B1:B3);=MAX(A1,A2)\n");
        assertEquals("=SUMA(1;2;B1:B3)", s.getCellText("A1"));
        assertEquals("=MAX(A1;A2)", s.getCellText("B1"));
    }

    @Test
    void commasInTextAreNotTouched() {
        Spreadsheet s = S2VReader.fromS2V("hello, world\n");
        assertEquals("hello, world", s.getCellText("A1"));
    }

    @Test
    void emptyRowsAreKeptAsEmptyLines() {
        Spreadsheet s = new Spreadsheet();
        s.setCellContent("A1", "1");
        s.setCellContent("A3", "3");
        assertEquals("1\n\n3\n", S2VWriter.toS2V(s));
        Spreadsheet back = S2VReader.fromS2V("1\n\n3\n");
        assertEquals("3", back.getCellText("A3"));
        assertEquals(2, back.getCells().size());
    }

    @Test
    void emptySpreadsheetGivesEmptyFileAndBack() {
        assertEquals("", S2VWriter.toS2V(new Spreadsheet()));
        assertEquals(0, S2VReader.fromS2V("").getCells().size());
    }

    @Test
    void saveThenLoadGivesTheSameContents() throws IOException {
        Path file = Files.createTempFile("sheet", ".s2v");
        try {
            S2VWriter.save(example(), file);
            Spreadsheet loaded = S2VReader.load(file);
            assertEquals(S2VWriter.toS2V(example()), S2VWriter.toS2V(loaded));
        } finally {
            Files.deleteIfExists(file);
        }
    }

    @Test
    void loadingAMissingFileFails() {
        assertThrows(IOException.class, () -> S2VReader.load(Path.of("does-not-exist.s2v")));
    }
}
