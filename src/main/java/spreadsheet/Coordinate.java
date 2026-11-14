package spreadsheet;

import java.util.Objects;

/** Column letters and row number, like A1 or AB12. */
public final class Coordinate {

    private final String column;
    private final int row;

    public Coordinate(String column, int row) {
        if (column == null || !column.matches("[A-Za-z]+")) {
            throw new IllegalArgumentException("Invalid column: " + column);
        }
        if (row < 1) {
            throw new IllegalArgumentException("Row must be >= 1: " + row);
        }
        this.column = column.toUpperCase();
        this.row = row;
    }

    /** Builds a coordinate from text like "A1" or "AB12". */
    public static Coordinate parse(String text) {
        if (text == null || !text.matches("[A-Za-z]+[1-9][0-9]*")) {
            throw new IllegalArgumentException("Invalid coordinate: " + text);
        }
        int split = 0;
        while (Character.isLetter(text.charAt(split))) {
            split++;
        }
        return new Coordinate(text.substring(0, split), Integer.parseInt(text.substring(split)));
    }

    public static Coordinate of(int columnNumber, int row) {
        if (columnNumber < 1) {
            throw new IllegalArgumentException("Column number must be >= 1: " + columnNumber);
        }
        StringBuilder letters = new StringBuilder();
        int n = columnNumber;
        while (n > 0) {
            n--;
            letters.insert(0, (char) ('A' + n % 26));
            n /= 26;
        }
        return new Coordinate(letters.toString(), row);
    }

    public String getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public int getColumnNumber() {
        int result = 0;
        for (char c : column.toCharArray()) {
            result = result * 26 + (c - 'A' + 1);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Coordinate other && column.equals(other.column) && row == other.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, row);
    }

    @Override
    public String toString() {
        return column + row;
    }
}
