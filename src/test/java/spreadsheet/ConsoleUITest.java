package spreadsheet;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ConsoleUITest {

    // Types the given lines into the menu and returns everything the menu printed
    private static String run(String... lines) {
        String input = String.join("\n", lines) + "\n";
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        new ConsoleUI(new ByteArrayInputStream(input.getBytes()), new PrintStream(captured)).run();
        return captured.toString();
    }

    @Test
    void setThenViewShowsTheContentNotTheValue() {
        String output = run("2", "A1", "=1+2", "3", "A1", "0");
        assertTrue(output.contains("A1: =1+2  (formula)"));
    }

    @Test
    void viewShowsNumbersAndText() {
        String output = run("2", "B1", "4", "2", "B2", "TOTAL", "3", "B1", "3", "B2", "0");
        assertTrue(output.contains("B1: 4  (number)"));
        assertTrue(output.contains("B2: TOTAL  (text)"));
    }

    @Test
    void emptyCellIsReportedAsEmpty() {
        assertTrue(run("3", "Z9", "0").contains("Z9 is empty."));
    }

    @Test
    void invalidCoordinateShowsAnErrorAndKeepsGoing() {
        String output = run("2", "1A", "3", "A1", "0");
        assertTrue(output.contains("is not a valid cell"));
        assertTrue(output.contains("A1 is empty."));
        assertTrue(output.contains("Bye."));
    }

    @Test
    void unknownOptionShowsAnError() {
        assertTrue(run("9", "0").contains("Error: unknown option '9'."));
    }

    @Test
    void createNewSpreadsheetEmptiesIt() {
        String output = run("2", "A1", "5", "1", "3", "A1", "0");
        assertTrue(output.contains("A1 is empty."));
    }

    @Test
    void viewSpreadsheetListsCellsInOrder() {
        String output = run("2", "B1", "x", "2", "A2", "y", "2", "A1", "z", "4", "0");
        int a1 = output.indexOf("A1: z");
        int b1 = output.indexOf("B1: x");
        int a2 = output.indexOf("A2: y");
        assertTrue(a1 >= 0 && a1 < b1 && b1 < a2);
    }

    @Test
    void saveThenLoadInANewSpreadsheet() throws IOException {
        Path file = Files.createTempFile("ui", ".s2v");
        try {
            run("2", "A1", "=SUMA(1;2)", "2", "B1", "hello", "5", file.toString(), "0");
            // The file uses ',' for the function separator
            assertEquals("=SUMA(1,2);hello\n", Files.readString(file));
            String output = run("6", file.toString(), "3", "A1", "0");
            assertTrue(output.contains("A1: =SUMA(1;2)  (formula)"));
        } finally {
            Files.deleteIfExists(file);
        }
    }

    @Test
    void loadingAMissingFileShowsAnErrorAndKeepsTheSpreadsheet() {
        String output = run("2", "A1", "7", "6", "no-such-file.s2v", "3", "A1", "0");
        assertTrue(output.contains("Error: cannot load the file"));
        assertTrue(output.contains("A1: 7  (number)"));
    }

    @Test
    void endOfInputStopsTheProgram() {
        run(); // no input at all: must not hang or crash
    }
}
