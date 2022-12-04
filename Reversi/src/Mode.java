import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

abstract class Mode {
    final Scanner in = new Scanner(System.in);
    final Color usersColor = Color.BLACK;
    final Color opponentsColor = Color.WHITE;
    final Board board = new Board();

    public abstract void scriptMode();
    public abstract void results();
    protected boolean lineToInt(String line) {
        List<String> digits = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7");
        return digits.contains(line);
    }

    public abstract int getRowOrColumn(String rowOrColumn, int num, String colorString);
}
