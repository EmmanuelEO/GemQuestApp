package ca.cmpt276.as3.model;

// Singleton Class to store the number of rows, columns, and mines across separate activites
public class GameOptions {
    private int rows, columns, mines;

    private static GameOptions instance;
    private GameOptions(){};

    public static GameOptions getInstance() {
        if (instance == null)
            return new GameOptions();
        return instance;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getMines() {
        return mines;
    }

    public void setMines(int mines) {
        this.mines = mines;
    }
}
