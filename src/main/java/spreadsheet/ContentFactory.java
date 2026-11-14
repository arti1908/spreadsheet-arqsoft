package spreadsheet;


public final class ContentFactory {

    private ContentFactory() {
    }

    // formula if it starts with =, number if it parses, text otherwise
    public static Content create(String text) {
        if (text.startsWith("=")) {
            return new FormulaContent(text);
        }
        try {
            return new NumericalContent(Double.parseDouble(text.trim()));
        } catch (NumberFormatException e) {
            return new TextContent(text);
        }
    }
}
