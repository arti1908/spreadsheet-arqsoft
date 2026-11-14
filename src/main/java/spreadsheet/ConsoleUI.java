package spreadsheet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Text menu. The streams come from the constructor so the tests can use their own. */
public final class ConsoleUI {

    private final BufferedReader in;
    private final PrintStream out;
    private Spreadsheet spreadsheet = new Spreadsheet();

    public ConsoleUI(InputStream in, PrintStream out) {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.out = out;
    }

    public void run() {
        while (true) {
            printMenu();
            String choice = ask("Option: ");
            if (choice == null) {
                return;
            }
            switch (choice.trim()) {
                case "1" -> createSpreadsheet();
                case "2" -> setCellContent();
                case "3" -> viewCellContent();
                case "4" -> viewSpreadsheet();
                case "5" -> save();
                case "6" -> load();
                case "0" -> {
                    out.println("Bye.");
                    return;
                }
                default -> out.println("Error: unknown option '" + choice.trim() + "'.");
            }
        }
    }

    private void printMenu() {
        out.println();
        out.println("=== Spreadsheet ===");
        out.println("1. Create new spreadsheet");
        out.println("2. Set cell content");
        out.println("3. View cell content");
        out.println("4. View spreadsheet contents");
        out.println("5. Save spreadsheet to S2V file");
        out.println("6. Load spreadsheet from S2V file");
        out.println("0. Exit");
    }

    private void createSpreadsheet() {
        spreadsheet = new Spreadsheet();
        out.println("New empty spreadsheet created.");
    }

    private void setCellContent() {
        Coordinate coordinate = askCoordinate();
        if (coordinate == null) {
            return;
        }
        String text = ask("Content (number, text or =formula; empty to clear): ");
        if (text == null) {
            return;
        }
        spreadsheet.setCellContent(coordinate, text);
        out.println(text.isEmpty() ? coordinate + " cleared." : coordinate + " set.");
    }

    private void viewCellContent() {
        Coordinate coordinate = askCoordinate();
        if (coordinate == null) {
            return;
        }
        Content content = spreadsheet.getCellContent(coordinate);
        if (content == null) {
            out.println(coordinate + " is empty.");
        } else {
            out.println(coordinate + ": " + content.getText() + "  (" + kindOf(content) + ")");
        }
    }

    private void viewSpreadsheet() {
        // sorted by row first and then by column
        List<Cell> cells = new ArrayList<>(spreadsheet.getCells());
        cells.sort(Comparator.comparingInt((Cell c) -> c.getCoordinate().getRow())
                .thenComparingInt(c -> c.getCoordinate().getColumnNumber()));
        if (cells.isEmpty()) {
            out.println("The spreadsheet is empty.");
            return;
        }
        for (Cell cell : cells) {
            out.println(cell.getCoordinate() + ": " + cell.getText());
        }
    }

    private void save() {
        String path = ask("File path to save: ");
        if (path == null) {
            return;
        }
        try {
            S2VWriter.save(spreadsheet, Path.of(path.trim()));
            out.println("Saved to " + path.trim() + ".");
        } catch (IOException | IllegalArgumentException e) {
            out.println("Error: cannot save the file (" + e.getMessage() + ").");
        }
    }

    private void load() {
        String path = ask("File path to load: ");
        if (path == null) {
            return;
        }
        try {
            // the current spreadsheet is only replaced if the load works
            spreadsheet = S2VReader.load(Path.of(path.trim()));
            out.println("Loaded " + path.trim() + ".");
        } catch (IOException | IllegalArgumentException e) {
            out.println("Error: cannot load the file (" + e.getMessage() + "). The current spreadsheet was not changed.");
        }
    }

    private String ask(String question) {
        out.print(question);
        out.flush();
        try {
            return in.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    private Coordinate askCoordinate() {
        String text = ask("Cell (e.g. A1): ");
        if (text == null) {
            return null;
        }
        try {
            return Coordinate.parse(text.trim());
        } catch (IllegalArgumentException e) {
            out.println("Error: '" + text.trim() + "' is not a valid cell. Use letters then a row number, like A1 or AB12.");
            return null;
        }
    }

    private static String kindOf(Content content) {
        if (content instanceof FormulaContent) {
            return "formula";
        }
        if (content instanceof NumericalContent) {
            return "number";
        }
        return "text";
    }
}
