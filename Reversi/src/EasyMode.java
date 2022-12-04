import java.util.ArrayList;
import java.util.List;

public final class EasyMode extends Mode{
    public EasyMode() {
        scriptMode();
    }

    public int returnUserResult() {
        return board.countChips(usersColor);
    }

    @Override
    public void scriptMode() {
        int row;
        int column;

        board.positionAnalysis(opponentsColor, usersColor);
        while (!board.isEnd()) {
            setCopyBoard(board.cells);
            System.out.print("Возможные позиции для вашего хода: \n");
            for (int[] ints : board.potentialPositions) {
                System.out.printf("(%d, %d)\n", ints[0], ints[1]);
            }
            System.out.println(board);

            //ход пользователя
            do {
                row = getRowOrColumn("ряд", 0, "◯");
                column = getRowOrColumn("столбец", 0, "◯");
            } while (!board.checkUserPosition(row, column));
            board.setUserPosition(row, column, usersColor);
            if (board.isEnd()) {
                break;
            }
            //ход компьютера
            if (!setComputerPosition()) {
                break;
            }
            cancelStep(0);
            board.positionAnalysis(opponentsColor, usersColor);
        }
        System.out.println(board);
        results();
    }

    @Override
    public void results() {
        int blackChips = board.countChips(usersColor);
        int whiteChips = board.countChips(opponentsColor);
        if (blackChips > whiteChips) {
            System.out.println("Ура! Вы выйграли!");
        } else if (blackChips < whiteChips) {
            System.out.println("Выйграл противник, но вы не расстраивайтесь!");
        } else {
            System.out.println("Ничья!");
        }
        System.out.printf("Количество ваших фишек: %d\n" +
                "Количество фишек противника: %d\n", blackChips, whiteChips);
    }

    @Override
    public int getRowOrColumn(String rowOrColumn, int num, String colorString) {
        String line;
        do {
            System.out.printf("Выберите %s клетки %s: ", rowOrColumn, colorString);
            line = in.nextLine();
        } while (!lineToInt(line));
        return Integer.parseInt(line);
    }

    @Override
    public void cancelStep(int num) {
        System.out.println(board);
        System.out.println("Хотите выполнить отмену хода? (y/anything)");
        if ("y".equals(in.nextLine())) {
            board.cells.clear();
            for (ArrayList<Cell> cells : copyBoard) {
                ArrayList<Cell> rowCells = new ArrayList<>(cells);
                board.cells.add(rowCells);
            }
        }
    }

    // оценка и ход компьютера
    /**
     * сумма si (ценность замыкающих клеток)
     * @param row    ряд
     * @param column столбец
     * @return сумму всех замкнутых позиций (сумму всех si для этой клетки)
     */
    private int valueOfClosedCell(int row, int column) {
        if (numberOfNeighbors(row, column) == 5) {
            return 2;
        } else {
            return 1;
        }
    }

    /**
     * ss (ценность клетки, на которую совершается ход)
     * @param row    ряд
     * @param column столбец
     * @return значение ss для данной позиции
     */
    private double valueOfPotentialCell(int row, int column) {
        return switch (numberOfNeighbors(row, column)) {
            case 3 -> 0.8;
            case 5 -> 0.4;
            default -> 0;
        };
    }

    /** Колиество соседей у клетки
     * @param row ряд
     * @param column столбец
     * @return Колиество соседей у клетки
     */
    private int numberOfNeighbors(int row, int column) {
        List<int[]> array = board.makeListOfCloseChips(row, column);
        int count = 0;

        for (int[] ints : array) {
            if (ints[0] >= 0 && ints[0] <= 7 && ints[1] >= 0 && ints[1] <= 7) {
                count += 1;
            }
        }
        return count;
    }

    private int findComputerClosedChip(int row, int column, int stepRow, int stepColumn, Color userColor) {
        int copyRow = row + stepRow * 2;
        int copyColumn = column + stepColumn * 2;
        while (copyRow >= 0 && copyRow <= 7 && copyColumn >= 0 && copyColumn <= 7) {
            if (board.hasColorCheap(copyRow, copyColumn, userColor)) {
                return valueOfClosedCell(copyRow, copyColumn);
            }
            copyRow += stepRow;
            copyColumn += stepColumn;
        }
        return 0;
    }

    // найти позиции замыкающих клеток
    private int sumOfSi(int row, int column) {
        int summ = 0;
        int intervalRow;
        int intervalColumn;
        ArrayList<int[]> array = board.haveOpponentsChips(row, column, usersColor);
        if (array.size() != 0) {
            for (int[] ints : array) {
                intervalRow = (row - ints[0]) * (-1);
                intervalColumn = (column - ints[1]) * (-1);
                summ += findComputerClosedChip(row, column, intervalRow, intervalColumn, opponentsColor);
            }
        }
        return summ;
    }

    private boolean setComputerPosition() {
        board.positionAnalysis(usersColor, opponentsColor);
        if (board.isEnd()) {
            return false;
        }
        double si;
        double maxim = 0;
        double currentsum;
        double ss;
        ArrayList<int[]> maximPositions = new ArrayList<>();

        for (int[] ints : board.potentialPositions) {
            ss = valueOfPotentialCell(ints[0], ints[1]);
            si = sumOfSi(ints[0], ints[1]);
            currentsum = ss + si;

            if (currentsum > maxim) {
                maximPositions.clear();
                maxim = currentsum;
                maximPositions.add(new int[]{ints[0], ints[1]});
            } else if (currentsum == maxim) {
                maximPositions.add(new int[]{ints[0], ints[1]});
            }
        }
        if (maximPositions.size() != 0) {
            int[] maximPosition = maximPositions.get((int) (Math.random() * (maximPositions.size())));
            System.out.println("\nХод компьтера: " + maximPosition[0] + " " + maximPosition[1]);
            board.setUserPosition(maximPosition[0], maximPosition[1], opponentsColor);
        }
        return true;
    }
}