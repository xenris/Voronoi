public class Log {
    private static final boolean DEBUG = false;

    public static void debug(String s) {
        if(DEBUG) {
            System.out.println(s);
        }
    }

    public static void log(String s) {
        System.out.println(s);
    }

    public static void error(String s) {
        System.err.println(s);
    }
}
