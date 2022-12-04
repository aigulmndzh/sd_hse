import java.util.ArrayList;

public final class PlayersMode extends Mode {
    public PlayersMode() {
        scriptMode();
    }

    private void playersMove(Color color, int num, String colorString) {
        int row;
        int column;

        System.out.printf("\t\tХОД ИГРОКА %d %s\n", num, colorString);
        System.out.print("Возможные позиции для вашего хода: \n");
        for (int[] ints : board.potentialPositions) {
            System.out.printf("(%d, %d)\n", ints[0], ints[1]);
        }
        System.out.println(board);
        // ход игрока
        do {
            row = getRowOrColumn("ряд", num, colorString);
            column = getRowOrColumn("столбец", num, colorString);
        } while (!board.checkUserPosition(row, column));

        board.setUserPosition(row, column, color);
    }

    @Override
    public void scriptMode() {
        System.out.println("цвет фишек ИГРОКА 1 - черные ◯\n" +
                "цвет фишек ИГРОКА 2 - белые ●");

        board.positionAnalysis(opponentsColor, usersColor);
        while (!board.isEnd()) {
            setCopyBoard(board.cells);
            board.positionAnalysis(opponentsColor, usersColor);
            if (board.isEnd()) {
                break;
            }
            playersMove(usersColor, 1, "◯");
            cancelStep(1);
            if (board.isEnd()) {
                break;
            }
            setCopyBoard(board.cells);
            board.positionAnalysis(usersColor, opponentsColor);
            if (board.isEnd()) {
                break;
            }
            playersMove(opponentsColor, 2, "●");
            cancelStep(2);
        }
        System.out.println(board);
        results();
    }

    @Override
    public void results() {
        int blackChips = board.countChips(usersColor);
        int whiteChips = board.countChips(opponentsColor);
        if (blackChips > whiteChips) {
            System.out.println("Выйграл ИГРОК 1 ◯");
        } else if (blackChips < whiteChips) {
            System.out.println("Выйграл ИГРОК 2 ●");
        } else {
            System.out.println("Ничья!");
        }
        System.out.printf("Количество фишек ИГРОКА 1 ◯: %d\n" +
                "Количество фишек ИГРОКА 2 ●: %d\n\n", blackChips, whiteChips);
    }

    @Override
    public int getRowOrColumn(String rowOrColumn, int num, String colorString) {
        String line;
        do {
            System.out.printf("ИГРОК %d %s - Выберите %s клетки: ", num, colorString, rowOrColumn);
            line = in.nextLine();
        } while (!lineToInt(line));
        return Integer.parseInt(line);
    }

    private String askCancelStep() {
        System.out.println(board);
        System.out.println("Хотите выполнить отмену хода? (y/anything)");
        return in.nextLine();
    }

    @Override
    public void cancelStep(int num) {
        String answer = askCancelStep();
        while (answer.equals("y")) {
            board.cells.clear();
            for (ArrayList<Cell> cells : copyBoard) {
                ArrayList<Cell> rowCells = new ArrayList<>(cells);
                board.cells.add(rowCells);
            }
            switch (num) {
                case 1 -> {
                    setCopyBoard(board.cells);
                    board.positionAnalysis(opponentsColor, usersColor);
                    if (board.isEnd()) {
                        break;
                    }
                    playersMove(usersColor, 1, "◯");
                }
                case 2 -> {
                    setCopyBoard(board.cells);
                    board.positionAnalysis(usersColor, opponentsColor);
                    if (board.isEnd()) {
                        break;
                    }
                    playersMove(opponentsColor, 2, "●");
                }
            }
            answer = askCancelStep();
        }
    }
}
