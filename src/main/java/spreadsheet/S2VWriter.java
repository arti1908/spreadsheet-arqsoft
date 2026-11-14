package spreadsheet;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Saves a spreadsheet as S2V. Inside formulas the ; between arguments is written as a comma. */
public final class S2VWriter {

    private S2VWriter() {
    }

    public static void save(Spreadsheet spreadsheet, Path file) throws IOException {
        try (BufferedWriter out = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            write(spreadsheet, out);
        }
    }

    public static void write(Spreadsheet spreadsheet, Writer out) throws IOException {
        for (int row = 1; row <= spreadsheet.getLastRow(); row++) {
            out.write(rowToLine(spreadsheet, row));
            out.write("\n");
        }
    }

    public static String toS2V(Spreadsheet spreadsheet) {
        StringWriter out = new StringWriter();
        try {
            write(spreadsheet, out);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return out.toString();
    }

    private static String rowToLine(Spreadsheet spreadsheet, int row) {
        int lastColumn = 0;
        for (int column = 1; column <= spreadsheet.getLastColumnNumber(); column++) {
            if (!spreadsheet.getCellText(Coordinate.of(column, row)).isEmpty()) {
                lastColumn = column;
            }
        }
        StringBuilder line = new StringBuilder();
        for (int column = 1; column <= lastColumn; column++) {
            if (column > 1) {
                line.append(';');
            }
            line.append(toFileText(spreadsheet.getCellText(Coordinate.of(column, row))));
        }
        return line.toString();
    }

    private static String toFileText(String text) {
        return text.startsWith("=") ? text.replace(';', ',') : text;
    }
}
