import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

abstract class Mode {
    final Scanner in = new Scanner(System.in);
    final Color usersColor = Color.BLACK;
    final Color opponentsColor = Color.WHITE;
    final Board board = new Board();
    final ArrayList<ArrayList<Cell>> copyBoard = new ArrayList<>();

    public abstract void scriptMode();
    public abstract void results();
    protected boolean lineToInt(String line) {
        List<String> digits = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7");
        return digits.contains(line);
    }

    public abstract int getRowOrColumn(String rowOrColumn, int num, String colorString);

    protected void setCopyBoard(ArrayList<ArrayList<Cell>> cells) {
        if (!copyBoard.isEmpty()) {
            copyBoard.clear();
        }

        for (ArrayList<Cell> items : cells) {
            ArrayList<Cell> rowCells = new ArrayList<>();
            for (Cell cell:items) {
                Cell newcell = new Cell(cell.getIsEmpty(), cell.getColor());
                rowCells.add(newcell);
            }
            copyBoard.add(rowCells);
        }
    }

    public abstract void cancelStep(int num);

    // ход пользователя
    /**
     * ход пользователя-1
     * (устанавливаем фишку, меняем цвета фишек котрые замкнутые)
     *
     * @param row    ряд
     * @param column столбец
     */
    void setUserPosition(int row, int column, Color color) {
        board.setChip(row, column, color);
        inspectionOfChips(row, column);
        board.changeChipsPriority();
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
            for (int[] ints : board.potentialPositions) {
                if (ints[0] == row && ints[1] == column && board.cells.get(row).get(column).getIsEmpty()) {
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
        List<int[]> array = board.makeListOfCloseChips(row, column);
        int[] interval = new int[2];

        for (int[] ints : array) {
            if (ints[0] >= 0 && ints[0] <= 7 && ints[1] >= 0 && ints[1] <= 7) {
                if (board.hasColorCheap(ints[0], ints[1], board.cells.get(row).get(column).getOppositeColor())) {
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
            if (board.hasColorCheap(copyRow, copyColumn, board.cells.get(row).get(column).getColor())) {
                copyRow -= stepRow;
                copyColumn -= stepColumn;
                while (copyRow != row || copyColumn != column) {
                    board.cells.get(copyRow).get(copyColumn).changeColor();
                    copyRow -= stepRow;
                    copyColumn -= stepColumn;
                }
                break;
            } else if (board.hasColorCheap(copyRow, copyColumn, board.cells.get(row).get(column).getOppositeColor())) {
                copyRow += stepRow;
                copyColumn += stepColumn;
            } else {
                break;
            }
        }
    }

    private boolean BoardisFull() {
        for (ArrayList<Cell> cells : board.cells) {
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
        for (ArrayList<Cell> cells : board.cells) {
            for (Cell cell : cells) {
                if (!cell.getIsEmpty()) {
                    currentColor = cell.getColor();
                    break;
                }
            }
        }
        for (ArrayList<Cell> cells : board.cells) {
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
        if (board.potentialPositions.isEmpty()) {
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
}
