public final class Cell {
    private boolean isPriority = false;
    private boolean isEmpty;
    private Color color;

    public Cell() {
        isEmpty = true;
    }

    public Cell(boolean isEmpty, Color color) {
        if (!isEmpty) {
            this.isEmpty = false;
            this.color = color;
        } else {
            this.isEmpty = true;
        }
    }

    public boolean getIsPriority() {
        return isPriority;
    }

    public void setIsPriority(boolean isPriority) {
        if (isEmpty) {
            this.isPriority = isPriority;
        }
    }

    public boolean getIsEmpty() {
        return isEmpty;
    }

    public void setIsEmpty(boolean isEmpty) {
        if (isEmpty && !this.isEmpty) {
            System.out.println("Нельзя убирать фишку, только менять цвет");
        } else {
            this.isEmpty = isEmpty;
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void changeColor() {
        if (isEmpty) {
            System.out.println("клетка пустая фишки нет");
        } else {
            if (color == Color.BLACK) {
                color = Color.WHITE;
            } else {
                color = Color.BLACK;
            }
        }
    }

    public Color getOppositeColor() {
        if (color.equals(Color.WHITE)) {
            return Color.BLACK;
        }
        return Color.WHITE;
    }

    @Override
    public String toString() {
        if (isEmpty) {
            if (isPriority) {
                return " + ";
            }
            return " - ";
        } else {
            if (this.color == Color.BLACK) {
                return " ◯ ";
            } else {
                return " ● ";
            }
        }
    }
}