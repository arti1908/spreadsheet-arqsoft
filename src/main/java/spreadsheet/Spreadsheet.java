package spreadsheet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** Only the cells with content are stored. */
public class Spreadsheet {

    private final Map<Coordinate, Cell> cells = new HashMap<>();

    public void setCellContent(Coordinate coordinate, String text) {
        if (text == null || text.isEmpty()) {
            cells.remove(coordinate);
            return;
        }
        Cell cell = cells.computeIfAbsent(coordinate, Cell::new);
        cell.setContent(ContentFactory.create(text));
    }

    public void setCellContent(String coordinate, String text) {
        setCellContent(Coordinate.parse(coordinate), text);
    }

    /** Null if the cell is empty. */
    public Content getCellContent(Coordinate coordinate) {
        Cell cell = cells.get(coordinate);
        return cell == null ? null : cell.getContent();
    }

    public String getCellText(Coordinate coordinate) {
        Cell cell = cells.get(coordinate);
        return cell == null ? "" : cell.getText();
    }

    public String getCellText(String coordinate) {
        return getCellText(Coordinate.parse(coordinate));
    }

    public Collection<Cell> getCells() {
        return Collections.unmodifiableCollection(cells.values());
    }

    // 0 when the spreadsheet is empty
    public int getLastRow() {
        return cells.keySet().stream().mapToInt(Coordinate::getRow).max().orElse(0);
    }

    public int getLastColumnNumber() {
        return cells.keySet().stream().mapToInt(Coordinate::getColumnNumber).max().orElse(0);
    }
}
