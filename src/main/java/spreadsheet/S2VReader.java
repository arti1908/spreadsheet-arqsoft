package spreadsheet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Reads an S2V file. Inside formulas the commas are changed back to ;. Nothing is computed. */
public final class S2VReader {

    private S2VReader() {
    }

    public static Spreadsheet load(Path file) throws IOException {
        try (BufferedReader in = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            return read(in);
        }
    }

    public static Spreadsheet read(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        Spreadsheet spreadsheet = new Spreadsheet();
        String line;
        int row = 1;
        while ((line = in.readLine()) != null) {
            // the -1 keeps the empty fields at the end of the line
            String[] fields = line.split(";", -1);
            for (int i = 0; i < fields.length; i++) {
                spreadsheet.setCellContent(Coordinate.of(i + 1, row), fromFileText(fields[i]));
            }
            row++;
        }
        return spreadsheet;
    }

    public static Spreadsheet fromS2V(String s2v) {
        try {
            return read(new StringReader(s2v));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String fromFileText(String text) {
        return text.startsWith("=") ? text.replace(',', ';') : text;
    }
}
