package spreadsheet;

public final class Cell {

    private final Coordinate coordinate;
    private Content content;

    public Cell(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public boolean isEmpty() {
        return content == null;
    }

    public String getText() {
        return isEmpty() ? "" : content.getText();
    }
}
