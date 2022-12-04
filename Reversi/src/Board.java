import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Board {
    final ArrayList<ArrayList<Cell>> cells = new ArrayList<>(8);
    final ArrayList<int[]> potentialPositions = new ArrayList<>();

    Board() {
        createBoard();
        createStartPosition();
    }

    private void createBoard() {
        int count = 8;
        while (count != 0) {
            ArrayList<Cell> array = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                array.add(new Cell());
            }
            cells.add(array);
            count -= 1;
        }
    }

    private void createStartPosition() {
        setChip(3, 3, Color.WHITE);
        setChip(4, 4, Color.WHITE);
        setChip(3, 4, Color.BLACK);
        setChip(4, 3, Color.BLACK);
    }

    int countChips(Color color) {
        int count = 0;
        for (int i = 0; i < cells.size(); i++) {
            for (int j = 0; j < cells.get(i).size(); j++) {
                if (hasColorCheap(i, j, color)) {
                    count += 1;
                }
            }
        }
        return count;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(" ");
        for (int i = 0; i < 8; i++) {
            result.append("  ").append(i).append(" ");
        }
        result.append("\n");
        int count = 0;

        for (int i = 0; i < cells.size(); i++) {
            result.append(count).append(" ");
            for (int j = 0; j < cells.get(i).size(); j++) {
                result.append(cells.get(i).get(j)).append(" ");
                if (i == 3 && j == 7) {
                    result.append("\tчерных ◯ фишек: ").append(countChips(Color.BLACK));
                } else if (i == 4 && j == 7) {
                    result.append("\tбелых ● фишек: ").append(countChips(Color.WHITE));
                }
            }
            result.append("\n");
            count += 1;
        }
        return result.toString();
    }

    /**
     * приведение каждой фишки к неприоритетному виду
     */
    void changeChipsPriority() {
        for (ArrayList<Cell> cells : cells) {
            for (Cell cell : cells) {
                if (cell.getIsPriority()) {
                    cell.setIsPriority(false);
                }
            }
        }
    }

    /**
     * ставим фишку на заданную позицию
     *
     * @param row    ряд
     * @param column столбец
     * @param color  цвет фишки
     */
    void setChip(int row, int column, Color color) {
        cells.get(row).get(column).setIsEmpty(false);
        cells.get(row).get(column).setColor(color);
    }

    /**
     * проверка на наличии фишки в клетке(позиции)
     *
     * @param row    ряд
     * @param column столбец
     * @return наличие фишки в позиции
     */
    boolean isEmptyPosition(int row, int column) {
        return cells.get(row).get(column).getIsEmpty();
    }

    /**
     * @param row    ряд
     * @param column столбец
     * @return список координат позиций которые находятся рядом
     */
    List<int[]> makeListOfCloseChips(int row, int column) {
        return new ArrayList<>(Arrays.asList(
                new int[]{row - 1, column - 1},
                new int[]{row - 1, column},
                new int[]{row - 1, column + 1},
                new int[]{row, column - 1},
                new int[]{row, column + 1},
                new int[]{row + 1, column - 1},
                new int[]{row + 1, column},
                new int[]{row + 1, column + 1}
        ));
    }

    /**
     * @param row    ряд
     * @param column столбец
     * @param color  цвет
     * @return проверка фишки на соответствующий цвет
     */
    boolean hasColorCheap(int row, int column, Color color) {
        if (cells.get(row).get(column).getIsEmpty()) {
            return false;
        }
        return cells.get(row).get(column).getColor().equals(color);
    }

    // нахождение потенциальных поизиций

    /**
     * нахождение потенциальных позиций для хода-1
     */
    void positionAnalysis(Color oppositeColor, Color userColor) {
        changeChipsPriority();
        potentialPositions.clear();

        for (int i = 0; i < cells.size(); i++) {
            for (int j = 0; j < cells.get(i).size(); j++) {
                if (isEmptyPosition(i, j)) {
                    ArrayList<int[]> array = haveOpponentsChips(i, j, oppositeColor);
                    if (array.size() != 0) {
                        getPositions(array, i, j, userColor);
                    }
                }
            }
        }

        for (int[] ints : potentialPositions) {
            cells.get(ints[0]).get(ints[1]).setIsPriority(true);
        }
    }

    /**
     * нахождение потенциальных позиций для хода-2
     *
     * @param row    ряд
     * @param column столбец
     * @return возвращает список позиций, где на месте этих позиций есть фишки противника
     */
    ArrayList<int[]> haveOpponentsChips(int row, int column, Color oppositeColor) {
        ArrayList<int[]> result = new ArrayList<>();
        List<int[]> array = makeListOfCloseChips(row, column);

        for (int[] ints : array) {
            if (ints[0] >= 0 && ints[1] >= 0 && ints[0] <= 7 && ints[1] <= 7 &&
                    hasColorCheap(ints[0], ints[1], oppositeColor)) {
                result.add(ints);
            }
        }
        array.clear();
        return result;
    }

    /**
     * нахождение потенциальных позиций для хода-3
     * проверяет фишку из array, есть ли у нее замыкающая,
     * если есть то добавляем в potentialPositions
     *
     * @param array  список позиций, где на месте этих позиций есть фишки противника
     * @param row    ряд
     * @param column столбец
     */
    private void getPositions(ArrayList<int[]> array, int row, int column, Color userColor) {
        int[] interval = new int[2];
        for (int[] ints : array) {
            interval[0] = row - ints[0];
            interval[1] = column - ints[1];
            findClosedChip(row, column, interval[0] * (-1), interval[1] * (-1), userColor);
        }
    }

    /**
     * нахождение потенциальных позиций для хода-4
     * нахождение замыкающией фишки
     *
     * @param row        ряд
     * @param column     столбец
     * @param stepRow    направление (куда двигаться)
     * @param stepColumn направление
     */
    private void findClosedChip(int row, int column, int stepRow, int stepColumn, Color userColor) {
        int copyRow = row + stepRow * 2;
        int copyColumn = column + stepColumn * 2;
        while (copyRow >= 0 && copyRow <= 7 && copyColumn >= 0 && copyColumn <= 7) {
            if (isEmptyPosition(copyRow, copyColumn)) {
                break;
            }
            if (hasColorCheap(copyRow, copyColumn, userColor)) {
                if (!isExist(row, column)) {
                    potentialPositions.add(new int[]{row, column});
                    break;
                }
            }
            copyRow += stepRow;
            copyColumn += stepColumn;
        }
    }

    private boolean isExist(int row, int column) {
        for (int[] ints : potentialPositions) {
            if (ints[0] == row && ints[1] == column) {
                return true;
            }
        }
        return false;
    }

    private boolean BoardisFull() {
        for (ArrayList<Cell> cells : cells) {
            for (Cell cell : cells) {
                if (cell.getIsEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean AllChipsSameColor() {
        Color currentColor = null;
        for (ArrayList<Cell> cells : cells) {
            for (Cell cell : cells) {
                if (!cell.getIsEmpty()) {
                    currentColor = cell.getColor();
                    break;
                }
            }
        }
        for (ArrayList<Cell> cells : cells) {
            for (Cell cell : cells) {
                if (!cell.getIsEmpty()) {
                    if (!cell.getColor().equals(currentColor)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    boolean isEnd() {
        if (potentialPositions.isEmpty()) {
            System.out.println("\nИгра окончена! " +
                    "Больше нет возможных позиций для совершения хода!");
            return true;
        } else if (BoardisFull()) {
            System.out.println("\nИгра окончена! " +
                    "Доска заполнена!");
            return true;
        } else if (AllChipsSameColor()) {
            System.out.println("\nИгра окончена! " +
                    "На доске фишки только одного цвета!");
            return true;
        }
        return false;
    }

    // ход пользователя

    /**
     * ход пользователя-1
     * (устанавливаем фишку, меняем цвета фишек котрые замкнутые)
     *
     * @param row    ряд
     * @param column столбец
     */
    void setUserPosition(int row, int column, Color color) {
        setChip(row, column, color);
        inspectionOfChips(row, column);
        changeChipsPriority();
    }

    /**
     * ход пользователя-2
     *
     * @param row    ряд
     * @param column столбец
     * @return проверка на корректность введенных координат и
     * наличие такой позиции в potentialPositions
     */
    boolean checkUserPosition(int row, int column) {
        if (row >= 0 && row <= 7 && column >= 0 && column <= 7) {
            for (int[] ints : potentialPositions) {
                if (ints[0] == row && ints[1] == column && cells.get(row).get(column).getIsEmpty()) {
                    return true;
                }
            }
        }
        System.out.println("Пожалуйста выберите позиции, из списка наверху");
        return false;
    }

    /**
     * ход пользователя-3 (ищем близлежащие фишки противника (направление),
     * которые будут замкнуты)
     *
     * @param row    ряд
     * @param column столбец
     */
    private void inspectionOfChips(int row, int column) {
        List<int[]> array = makeListOfCloseChips(row, column);
        int[] interval = new int[2];

        for (int[] ints : array) {
            if (ints[0] >= 0 && ints[0] <= 7 && ints[1] >= 0 && ints[1] <= 7) {
                if (hasColorCheap(ints[0], ints[1], cells.get(row).get(column).getOppositeColor())) {
                    interval[0] = row - ints[0];
                    interval[1] = column - ints[1];
                    changeColorUserChips(row, column, interval[0] * (-1), interval[1] * (-1));
                }
            }
        }
    }

    /**
     * проверяем если есть замкнутая фишка, то меняем цвета
     *
     * @param row        ряд
     * @param column     столбец
     * @param stepRow    напрвление
     * @param stepColumn напрвление
     */
    private void changeColorUserChips(int row, int column, int stepRow, int stepColumn) {
        int copyRow = row + stepRow * 2;
        int copyColumn = column + stepColumn * 2;
        while (copyRow >= 0 && copyRow <= 7 && copyColumn >= 0 && copyColumn <= 7) {
            if (hasColorCheap(copyRow, copyColumn, cells.get(row).get(column).getColor())) {
                copyRow -= stepRow;
                copyColumn -= stepColumn;
                while (copyRow != row || copyColumn != column) {
                    cells.get(copyRow).get(copyColumn).changeColor();
                    copyRow -= stepRow;
                    copyColumn -= stepColumn;
                }
                break;
            } else if (hasColorCheap(copyRow, copyColumn, cells.get(row).get(column).getOppositeColor())) {
                copyRow += stepRow;
                copyColumn += stepColumn;
            } else {
                break;
            }
        }
    }
}