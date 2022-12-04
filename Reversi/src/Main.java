import java.util.Scanner;

public class Main {
    private static final Scanner in = new Scanner(System.in);
    private static int bestResult = 0;
    private static final String greeting = "Здравствуйте! Это игра Reversi!\n Вот что она умеет:\n";
    private static final String selectMode =
                    "1. /easy - начать игру в легком режиме\n" +
                    "2. /players - начать игру в режиме 'игрок против игрока'\n" +
                    "3. /exit - выйти из игры\n" +
                    "4. /res - узнать текущий лучший результат (за одну сессию)\n";

    private static String getCommand() {
        System.out.print("Введите команду: ");
        return in.nextLine();
    }

    public static void main(String[] args) {
        int currentResult;
        System.out.print(greeting);
        System.out.println(selectMode);

        String command = getCommand();
        while (!command.equals("/exit")) {
            switch (command) {
                case "/easy":
                    EasyMode easyMode = new EasyMode();
                    currentResult = easyMode.returnUserResult();
                    if (bestResult < currentResult) {
                        bestResult = currentResult;
                    }
                    break;
                case "/players":
                    PlayersMode playersMode = new PlayersMode();
                    break;
                case "/res":
                    System.out.printf("Лучший результат " +
                            "(max число очков/фишек): %d\n", bestResult);
                    break;
                default:
                    break;
            }
            System.out.print(selectMode);
            command = getCommand();
        }
        System.out.print("\nДо свидания!");
    }
}