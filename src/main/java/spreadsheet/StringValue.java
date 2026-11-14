package spreadsheet;

public final class StringValue extends Value {

    private final String text;

    public StringValue(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
