package spreadsheet;

/** Program entry point: starts the console menu. */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        new ConsoleUI(System.in, System.out).run();
    }
}
